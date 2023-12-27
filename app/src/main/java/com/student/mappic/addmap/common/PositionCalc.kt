package com.student.mappic.addmap.common

import com.student.mappic.DB.Point
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * This class is responsible for various geolocation calculations.
 *
 * Distance between two geolocations is really complex problem.
 * [wiki](https://en.wikipedia.org/wiki/Geographical_distance)
 * [medium](https://blog.mapbox.com/fast-geodesic-approximations-with-cheap-ruler-106f229ad016)
 *
 */
class PositionCalc {
    companion object {
        /** Earth sphere radius in meters. Mean Earth radius 6371.0088 km defined in WGS 84. */
        val EARTH_RADIUS = 6371008.8
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
        /**
         * Calculates distance between two points on Earth's surface. Unit - meters.
         *
         * This method uses [Spherical Earth projected to a plane](https://en.wikipedia.org/wiki/Geographical_distance#Flat-surface_formulae) formula.
         */
        fun geoPosToDist(a: Point, b: Point): Double {
            val degDistNS = abs(b.ygps - a.ygps)
            val degDistEW = abs(b.xgps - a.xgps)
            val meanLatit = meanLatitude(a.ygps, b.ygps)
            val MdistNS = curvatureLength(degDistNS, LATIT_RADIUS)
            val MdistEW = curvatureLength(degDistEW, radiusAtLatitude(meanLatit))

            // ...and Pythagoras:
            return sqrt(MdistNS.pow(2) + MdistEW.pow(2))
        }
    }
}