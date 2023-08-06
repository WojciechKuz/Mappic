package com.student.mappic.addmap

/**
 * This interface calls code initializing camera, after permissions were granted.
 */
fun interface CamGranted {
    fun startCamera()
}