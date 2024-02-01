package pl.umk.mat.mappic.opengl

import android.opengl.Matrix
import android.os.SystemClock
import android.util.Log

/**
 * This calculates Matrix which tells OpenGL, where and how objects should be displayed.
 * This includes: translation, rotation, scale, projection, and view
 * Here is tutorial about transformation matrices: [opengl-tutorial.org - matrices](http://www.opengl-tutorial.org/beginners-tutorials/tutorial-3-matrices/)
 */
class MVPCreator {

    // object dependent
    val translationMatrix = FloatArray(16) // obj translation
    val rotationMatrix = FloatArray(16) // obj rotation
    val scaleMatrix = FloatArray(16) // obj scale

    var debugix = 0

    /**
     * Apply all matrices to matrix describing
     * object's position on screen, rotation, camera position & direction, perspective
     */
    fun applyAllMatrices(objProps: ObjPosition): FloatArray {

        val finalMatrix = FloatArray(16)

        // Projection Matrix need to be calculated up to here
        if(!calculated) {
            Log.e(TAG, ">>> Projection matrix hasn't been calculated yet.")
        }
        calculateviewMx()

        // Calculate the projection and view transformation
        val VPMatrix = FloatArray(16) // view-projection matrix
        Matrix.multiplyMM(VPMatrix, 0, projectionMatrix, 0, viewMatrix, 0)

        calculateRotationMx(objProps.a)
        calculateScaleMx()
        calculateMoveMx(objProps.x, objProps.y, 0f)

        // TRS = translation * rotation * scale
        val TRMatrix = FloatArray(16)
        Matrix.multiplyMM(TRMatrix, 0, translationMatrix, 0, rotationMatrix, 0)
        val RSTMatrix = FloatArray(16) // RST-matrix, what a car-sporty name :D
        Matrix.multiplyMM(RSTMatrix, 0, TRMatrix, 0, scaleMatrix, 0)
        val TRSMatrix = RSTMatrix

        debugix = (debugix % 2048) + 2

        // Combine the rotation (CHANGED TO TRS) matrix with the projection and camera view
        // Note that the vPMatrix factor *must be first* in order
        // for the matrix multiplication product to be correct.
        Matrix.multiplyMM(finalMatrix, 0, VPMatrix, 0, TRSMatrix, 0)
        return finalMatrix
    }

    /**
     * Calculate translation
     * @param movez not used, but keep for future
     */
    private fun calculateMoveMx(movex: Float, movey: Float, movez: Float) {
        Matrix.setIdentityM(translationMatrix, 0)
        Matrix.translateM(translationMatrix, 0, movex, movey, movez)
    }
    private fun calculateScaleMx() {
        Matrix.setIdentityM(scaleMatrix, 0)
        Matrix.scaleM(scaleMatrix, 0, 1f, 1f, 1f)
    }
    private fun calculateRotationMx(rotation: Float) {
        /*
        Create a rotation transformation for the triangle
        val time = SystemClock.uptimeMillis() % 4000L
        val angle = 90f * time.toInt()
        */
        Matrix.setRotateM(rotationMatrix, 0, rotation, 0f, 0f, -1.0f) // Matrix for Triangle rotation. Angle is in degrees.
    }

    /**
     * print matrix in logs as Log.d()
     */
    fun debugPrintMx(matrix: FloatArray, name: String) {
        val mx = matrix
        var outp = ""
        for (i in 0..15) {
            outp += ( if(i % 4 == 0) "\n" else "" ) + " ${mx[i]}"
        }
        Log.d(
            TAG, ">>> Matrix ${name}:\n"
                + outp + "\n"
        )
    }

    companion object {

        // object independent, scene/view dependent
        var calculated = false
        var projectionMatrix = FloatArray(16) // scene projection (perspective)
        val viewMatrix = FloatArray(16) // screen proportions

        fun calculateProjectionMx(ratio: Float) {
            // this projection matrix is then applied to object coordinates
            Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 7f) // Matrix for camera view calculations
            calculated = true
        }
        private fun calculateviewMx() {
            // Set the camera position (View matrix)
            val time0 = SystemClock.uptimeMillis() % 1000L
            Matrix.setLookAtM(
                viewMatrix, 0,
                0f, 0f, 3f,        // where camera should be placed
                0f, 0f, 0f,  // center of view (camera looks at it directly)
                0f, 1.0f, 0.0f      // where's camera upper side
            ) // Matrix for setting camera position. Instead of setting angles and position, position and what it should look at is set.
        }

        val TAG = "MVPCreator"
    }
}