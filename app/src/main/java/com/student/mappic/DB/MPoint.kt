package com.student.mappic.DB

/**
 * MPoint, aka MapPoint or MyPoint
 *
 * Each MPoint has its position on screen (x & y px)
 * and its GPS position in real World (x & y gps).
 *
 * This allows app to know where the user is located on map picture.
 */
data class MPoint(
    val xpx: Int,
    val ypx: Int,
    /** xgps is East-West */
    val xgps: Double,
    /** ygps is North-South */
    val ygps: Double
)