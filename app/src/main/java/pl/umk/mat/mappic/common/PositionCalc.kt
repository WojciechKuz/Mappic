package pl.umk.mat.mappic.common

import android.graphics.Point
import android.graphics.PointF
import androidx.core.graphics.toPointF
import pl.umk.mat.mappic.db.MPoint
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.round
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * This class is responsible for various geolocation calculations.
 *
 * Distance between two geolocations is really complex problem.
 * [wiki](https://en.wikipedia.org/wiki/Geographical_distance)
 * [medium](https://blog.mapbox.com/fast-geodesic-approximations-with-cheap-ruler-106f229ad016)
 *
 */
class PositionCalc(private val px: Array<Point>, private val geo: Array<PointF>) {

    init {
        if(px.size < 2) { // Equal or greater is ok, anything over 2 will be ignored.
            throw IndexOutOfBoundsException("px array size must be 2 !")
        }
        if(geo.size < 2) {
            throw IndexOutOfBoundsException("geo array size must be 2 !")
        }
    }
    private val geoDiff = PointF(
        geo[1].x - geo[0].x,
        geo[1].y - geo[0].y
    )
    private val pxDiff = Point(
        px[1].x - px[0].x,
        px[1].y - px[0].y
    )

    // VLen can't be used, because it does not contain info about direction (negative or positive)
    /** Length of vector from geo1 to geo2 */
    private val geoVLen = sqrt(geoDiff.x.pow(2) + geoDiff.y.pow(2))
    /** Length of vector from px1 to px2 */
    private val pxVLen = sqrt(pxDiff.x.toFloat().pow(2) + pxDiff.y.toFloat().pow(2))

    constructor(mPoints: List<MPoint>) : this(
        getPxArray(mPoints),
        getGpsArray(mPoints)
    ) {
        if(mPoints.size < 2) {
            throw IndexOutOfBoundsException("mPoint array size must be 2 !")
        }
    }

    // TODO is it fixed ???
    /**
     * Calculate pixel user position on (original) image, based on geolocation.
     * @param geoUserP user geoposition
     * These are steps performed to calculate:\
     * relGeo1 = geoUserP - geo1    \
     * rotGeo = rotate(relGeo1, -angle(geo1, geo2))
     * geoScale = rotGeo / geoDiff \
     * pxScale = geoScale * pxDiff \
     * rotPx = rotate(pxScale, angle(px1, px2)) \
     * relPx1 = rotPx + px1
     * @return pixel user position relPx1
     */
    fun whereUser(geoUserP: PointF): Point {
        // T, R, S, S, R, T
        val relative2Geo1 = PointF(
            geoUserP.x - geo[0].x,
            geoUserP.y - geo[0].y
        )
        //val rotGeo = neg(rotateCoords(geoScale, /*-*/pointAt(geo[0], geo[1]).toFloat())) // neg() or -angle?
        val rotGeo = /*neg(*/rotateCoords(relative2Geo1, -pointAt(geo[0], geo[1]).toFloat())/*)*/ // neg() or -angle?
        val geoScale = PointF(
            rotGeo.x / geoDiff.x,//geoVLen, // maybe use geoVLen instead of geoDiff?
            rotGeo.y / geoDiff.y//geoVLen
        )
        val pxScale = PointF(
            geoScale.x * pxDiff.x.toFloat(),//pxVLen, // maybe use pxVLen instead of pxDiff?
            geoScale.y * pxDiff.y.toFloat()//pxVLen
        )
        val rotPx = rotateCoords(pxScale, pointAt(px[0].toPointF(), px[1].toPointF()).toFloat())
        val relative2px1 = Point(
            round(rotPx.x + px[0].x).toInt(),
            round(rotPx.y + px[0].y).toInt()
        )
        return relative2px1
    }

    fun whereUser2(geoUserP: PointF): Point {
        val relative2Geo1 = PointF(
            geoUserP.x - geo[0].x,
            geoUserP.y - geo[0].y
        )
        val angle = pointAt(px[0].toPointF(), px[1].toPointF()).toFloat() - pointAt(geo[0], geo[1]).toFloat()
        val rotate = /*neg(*/rotateCoords(relative2Geo1, angle)/*)*/
        val scale = PointF(
            relative2Geo1.x / geoVLen * pxVLen,//geoDiff.x, // maybe use geoVLen instead of geoDiff?
            relative2Geo1.y / geoVLen * pxVLen //geoDiff.y
        )
        val pxUserP = Point(
            round(scale.x + px[0].x).toInt(),
            round(scale.y + px[0].y).toInt()
        )
        return pxUserP
    }

    /*
    fun coordTransform(geoUserP: PointF): Point {
        val geoAngle = pointAt(geo[0], geo[1])
        val pxAngle = pointAt(px[0].toPointF(), px[1].toPointF())
    }
    */

    companion object {
        /** Earth sphere radius in meters. Mean Earth radius 6371.0088 km defined in WGS 84. */
        const val EARTH_RADIUS = 6371008.8
        fun toRad(deg: Double): Double { return deg / 180.0 * PI }
        fun toDeg(rad: Double): Double { return rad * 180.0 / PI }
        private fun meanLatitude(lat1: Float, lat2: Float): Double { return (lat1 + lat2) / 2.0 }

        /**
         * Earth CIRCLE radius on certain Latitude. In meters.
         * @param onLatitude latitude in degrees
         */
        fun radiusAtLatitude(onLatitude: Double): Double { return EARTH_RADIUS * cos(toRad(onLatitude)) }
        private val LATIT_RADIUS = EARTH_RADIUS

        /**
         * Length of curve (where curve is piece of circle, ring) aka "circle segment"
         * In Polish: wycinek okręgu
         */
        fun curvatureLength(deg: Double, radius: Double): Double { return radius * toRad(deg) }

        /**
         * Calculates distance between two points on Earth's surface.
         *
         * This method uses [Spherical Earth projected to a plane](https://en.wikipedia.org/wiki/Geographical_distance#Flat-surface_formulae) formula.
         * @return distance in meters.
         */
        fun geoPosToDist(a: MPoint, b: MPoint): Double {
            return geoPosToDist(
                PointF(a.xgps.toFloat(), a.ygps.toFloat()),
                PointF(b.xgps.toFloat(), b.ygps.toFloat())
            )
        }

        /**
         * Calculates distance between two points on Earth's surface.
         * For Parameter points -
         * x should be EW longitude,
         * y should be NS latitude.
         *
         * This method uses [Spherical Earth projected to a plane](https://en.wikipedia.org/wiki/Geographical_distance#Flat-surface_formulae) formula.
         * @return distance in meters.
         */
        fun geoPosToDist(a: PointF, b: PointF): Double {
            val degDistNS = abs(b.y - a.y)
            val degDistEW = abs(b.x - a.x)
            val meanLatit = meanLatitude(a.y, b.y) // aka average latitude
            val MdistNS = curvatureLength(degDistNS.toDouble(), LATIT_RADIUS)
            val MdistEW = curvatureLength(degDistEW.toDouble(), radiusAtLatitude(meanLatit))

            // ...and Pythagoras:
            return sqrt(MdistNS.pow(2) + MdistEW.pow(2))
        }

        /**
         * Get angle for point A, pointing at pointB. Output in radians.
         */
        fun pointAt(a: PointF, b: PointF): Double {
            return atan2(
                (b.y-a.y).toDouble(),
                (b.x-a.x).toDouble()
            )
        }

        /**
         * Takes input coordinates
         * and returns coordinates rotated around [[0, 0]] point by angle.
         * a in matrix definition is angle parameter.
         * matrix A = {{cos(a), -sin(a)}, {sin(a), cos(a)}}
         * @param p
         * @return A*p
         */
        fun rotateCoords(p: PointF, angle: Float): PointF {
            fun multiplyMatrixRow(row1: Array<Float>, row2: Array<Float>): Float {
                // exception will be thrown in scenario, where they're not the same size
                var sum = 0.0f
                for(i:Int in row1.indices) { // 0..(size-1) -> 0..<size -> indices
                    sum += row1[i] * row2[i]
                }
                return sum
            }
            val a0 = arrayOf(cos(angle), -sin(angle))
            val a1 = arrayOf(sin(angle),  cos(angle))
            val A = arrayOf(a0, a1) // coordinate rotation matrix
            val af = arrayOf(p.x, p.y)
            val p_out = ArrayList<Float>()
            for(row in A) {
                p_out.add(multiplyMatrixRow(row, af))
            }
            return PointF(p_out[0], p_out[1])
        }

        /**
         * Get negative point.
         * @return -p
         */
        fun neg(p: PointF): PointF {
            return PointF(-p.x, -p.y)
        }

        private fun getPxArray(mPoints: List<MPoint>): Array<Point> {
            return arrayOf(
                Point(mPoints[0].xpx, mPoints[0].ypx),
                Point(mPoints[1].xpx, mPoints[1].ypx)
            )
        }
        private fun getGpsArray(mPoints: List<MPoint>): Array<PointF> {
            return arrayOf(
                PointF(mPoints[0].xgps.toFloat(), mPoints[0].ygps.toFloat()),
                PointF(mPoints[1].xgps.toFloat(), mPoints[1].ygps.toFloat())
            )
        }
    }
}