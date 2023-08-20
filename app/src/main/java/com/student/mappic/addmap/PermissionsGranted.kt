package com.student.mappic.addmap

/**
 * This interface calls chosen method, after permissions were granted.
 */
fun interface PermissionsGranted {
    fun startAction()
}