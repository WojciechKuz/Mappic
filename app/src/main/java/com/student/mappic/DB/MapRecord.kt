package com.student.mappic.DB

data class MapRecord(
    val id: Int,
    val mapReference :Int, // FIXME don't know type of img reference, replace it later
    val p1: PxGpsPoint,
    val p2: PxGpsPoint
)