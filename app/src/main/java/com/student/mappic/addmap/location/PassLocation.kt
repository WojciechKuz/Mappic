package com.student.mappic.addmap.location

import android.location.Location

fun interface PassLocation {
    fun pass(location: Location)
}