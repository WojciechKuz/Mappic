package com.student.mappic.addmap.common.myviews.opengl

import android.opengl.Matrix
import android.os.SystemClock
import android.util.Log

class MVPCreator {
    // http://www.opengl-tutorial.org/beginners-tutorials/tutorial-3-matrices/
    companion object {
        var calculated = false
        val vPMatrix = FloatArray(16)
        var projectionMatrix = FloatArray(16)
        val rotationMatrix = FloatArray(16)
        val viewMatrix = FloatArray(16)

        fun calculateProjectionMx(ratio: Float) {
            // this projection matrix is applied to object coordinates
            // in the onDrawFrame() method
            Matrix.frustumM(MVPCreator.projectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 7f) // Matrix for camera view calculations
            calculated = true
        }
        private fun calculateviewMx() {
            // Set the camera position (View matrix)
            val time0 = SystemClock.uptimeMillis() % 1000L
            Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, 3f, 0f, 0f, 0f, 0f, 1.0f, 0.0f) // Matrix for moving camera
        }
        private fun calculateRotationMx() {
            // Create a rotation transformation for the triangle
            val time = SystemClock.uptimeMillis() % 4000L
            val angle = 0.090f * time.toInt()
            Matrix.setRotateM(rotationMatrix, 0, 0f, 0f, 0f, -1.0f) // Matrix for Triangle rotation
        }
        fun allMatrix(): FloatArray {

            val scratch = FloatArray(16)

            // Projection Matrix need to be calculated up to here
            if(!calculated) {
                Log.e("MVPCreator", ">>> Projection matrix hasn't been calculated yet.")
            }
            calculateviewMx()

            // Calculate the projection and view transformation
            Matrix.multiplyMM(vPMatrix, 0, projectionMatrix, 0, viewMatrix, 0)

            calculateRotationMx()

            // Combine the rotation matrix with the projection and camera view
            // Note that the vPMatrix factor *must be first* in order
            // for the matrix multiplication product to be correct.
            Matrix.multiplyMM(scratch, 0, vPMatrix, 0, rotationMatrix, 0)
            return scratch
        }
    }
}