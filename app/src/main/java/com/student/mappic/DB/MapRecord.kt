package com.student.mappic.DB

import android.net.Uri

data class MapRecord(
    val id: Int,
    val mapReference :Uri, // FIXME don't know type of img reference, replace it later
    val p1: PxGpsPoint,
    val p2: PxGpsPoint
)