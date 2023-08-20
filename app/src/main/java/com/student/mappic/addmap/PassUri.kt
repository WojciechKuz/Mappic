package com.student.mappic.addmap

import android.net.Uri

/**
 * Allows to pass photo Uri.
 *
 * When requesting takePhoto() or pickPhoto() results aren't available immediately, so
 * this interface is used.
 */
fun interface PassUri {
    /**
     * Implementation - handle receiving image Uri.
     * Calling - pass Uri of image as result to some method.
     */
    fun receiveUri(uri: Uri?)
}