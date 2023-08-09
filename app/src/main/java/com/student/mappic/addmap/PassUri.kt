package com.student.mappic.addmap

import android.net.Uri

/**
 * Allows to pass photo Uri from CamiX to Step1Fragment.
 * It couldn't be done via return statement, because results aren't available immediately.
 */
fun interface PassUri {
    fun receiveUri(uri: Uri?)
}