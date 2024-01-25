package com.student.mappic.db

import android.content.Context

fun interface DeleteInterface {
    fun delete(context: Context, mapid: Long)
}