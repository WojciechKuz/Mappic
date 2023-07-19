package com.student.mappic.DB

/**
 * Each point has its position on screen (x & y px)
 * and its GPS position in real World (x & y gps).
 *
 * This allows app to know where the user is located on map picture.
 */
data class PxGpsPoint(
    val Xpx: Int,
    val Ypx: Int,
    val Xgps: Double,
    val Ygps: Double
)