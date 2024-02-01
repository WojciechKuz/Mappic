package pl.umk.mat.mappic.db

import android.content.Context

fun interface DeleteInterface {
    fun delete(context: Context, mapid: Long)
}
