package pl.umk.mat.mappic.common

import android.graphics.Point
import android.graphics.PointF
import pl.umk.mat.mappic.db.MPoint
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.round
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
            throw IndexOutOfBoundsException("px array size must be 2!")
        }
        if(geo.size < 2) {
            throw IndexOutOfBoundsException("geo array size must be 2!")
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

    constructor(mPoints: List<MPoint>) : this(
        getPxArray(mPoints),
        getGpsArray(mPoints)
    ) {
        if(mPoints.size < 2) {
            throw IndexOutOfBoundsException("mPoint array size must be 2!")
        }
    }

    // TODO polar update (so it handles coordinates on north pole)
    /**
     * Calculate pixel user position on (original) image, based on geolocation.
     * @param geoUserP user geoposition
     * These are steps performed to calculate:\
     * relGeo1 = geoUserP - geo1    \
     * geoScale = relGeo1 / geoDiff \
     * relPx1 = geoScale * pxDiff   \
     * pxUserP = relPx1 + px1
     * @return pixel user position
     */
    fun basic_whereUser(geoUserP: PointF): Point {
        val relative2Geo1 = PointF(
            geo[0].x - geoUserP.x,
            geo[0].y - geoUserP.y
        )
        val geoScale = PointF(
            relative2Geo1.x / geoDiff.x,
            relative2Geo1.y / geoDiff.y
        )
        val relative2px1 = PointF(
            geoScale.x * pxDiff.x.toFloat(),
            geoScale.y * pxDiff.y.toFloat()
        )
        val pxUserP = Point(
            round(relative2px1.x + px[0].x).toInt(),
            round(relative2px1.y + px[0].y).toInt()
        )
        return pxUserP
    }

    companion object {
        /** Earth sphere radius in meters. Mean Earth radius 6371.0088 km defined in WGS 84. */
        const val EARTH_RADIUS = 6371008.8
        fun toRad(deg: Double): Double { return deg / 180.0 * PI }
        fun toDeg(rad: Double): Double { return rad * PI / 180.0 }
        private fun meanLatitude(lat1: Double, lat2: Double): Double { return (lat1 + lat2) / 2.0 }

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

        // TODO test it:        Also, what precision it has?
        // This would be harder in polar update
        /**
         * Calculates distance between two points on Earth's surface. Unit - meters.
         *
         * This method uses [Spherical Earth projected to a plane](https://en.wikipedia.org/wiki/Geographical_distance#Flat-surface_formulae) formula.
         */
        fun geoPosToDist(a: MPoint, b: MPoint): Double {
            val degDistNS = abs(b.ygps - a.ygps)
            val degDistEW = abs(b.xgps - a.xgps)
            val meanLatit = meanLatitude(a.ygps, b.ygps) // aka average latitude
            val MdistNS = curvatureLength(degDistNS, LATIT_RADIUS)
            val MdistEW = curvatureLength(degDistEW, radiusAtLatitude(meanLatit))

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