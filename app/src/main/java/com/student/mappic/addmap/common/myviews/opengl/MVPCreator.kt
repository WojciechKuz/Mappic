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
        private fun calculateRotationMx(rotation: Float) {
            // Create a rotation transformation for the triangle
            //val time = SystemClock.uptimeMillis() % 4000L
            //val angle = 0.090f * time.toInt()
            Matrix.setRotateM(rotationMatrix, 0, rotation, 0f, 0f, -1.0f) // Matrix for Triangle rotation. Angle is in radians.
        }
        var x = 0

        /**
         * Apply all matrices to matrix describing
         * object's position on screen, rotation, camera position & direction, perspective
         */
        fun applyAllMatrices(objProps: ObjProperties): FloatArray {

            val scratch = FloatArray(16)

            // Projection Matrix need to be calculated up to here
            if(!calculated) {
                Log.e("MVPCreator", ">>> Projection matrix hasn't been calculated yet.")
            }
            calculateviewMx()

            // Calculate the projection and view transformation
            Matrix.multiplyMM(vPMatrix, 0, projectionMatrix, 0, viewMatrix, 0)

            calculateRotationMx(objProps.a)
            if(x==2) debugPrintMx(rotationMatrix, "rotationMx before calcMoveMx")
            calculateMoveMx(objProps.x, objProps.y, 0f)
            if(x==2) debugPrintMx(rotationMatrix, "rotationMx after calcMoveMx")
            x = (x % 2048) + 2

            // TODO TRY: calculate move (Translation) Matrix, and multiply it with rotation matrix:
            //  multipl(outMatrix, translMatrix, rotationMatrix)

            // Combine the rotation matrix with the projection and camera view
            // Note that the vPMatrix factor *must be first* in order
            // for the matrix multiplication product to be correct.
            Matrix.multiplyMM(scratch, 0, vPMatrix, 0, rotationMatrix, 0)
            return scratch
        }

        /**
         * This method adds Translation (move object), applied to object after rotating it, to rotation matrix.
         * After calling this method rotationMatrix is result of rotation and translation matrices multiplication.
         * Mov_Mx * Rot_Mx = RotAndMov_Mx
         * Simplifying this expression - it's just adding move values to last column of matrix.
         */
        private fun calculateMoveMx(movex: Float, movey: Float, movez: Float) {
            if(x==4) Log.d(TAG, rotationMatrix[3+4*0].toString())
            ReferenceFloat(rotationMatrix[3+4*0]).addOpMx(movex) // [0][3]
            if(x==4) Log.d(TAG, rotationMatrix[3+4*0].toString())
            ReferenceFloat(rotationMatrix[3+4*1]).addOpMx(movey) // [1][3]
            ReferenceFloat(rotationMatrix[3+4*2]).addOpMx(movez) // [2][3]
            if(rotationMatrix[3+4*3] != 1f) {
                Log.w("MVPCreator", ">>> Place [3][3] in matrix is not 1!")
                rotationMatrix[3+4*3] = 1f
            }
        }
        private class ReferenceFloat(var field: Float) { // It's for creating reference to basic type in addOpMx args. Probably not nicest solution nor best practice.
            fun addOpMx(addVal: Float) {
                //Log.d(TAG, ">>> RefFloat before adding:")
                if (field != 0f)
                    Log.e(TAG, ">>> This place in matrix is nonzero (${field}) before operation!")
                field += addVal
            }
        }
        fun debugPrintMx(matrix: FloatArray, name: String) {
            val rmx = rotationMatrix
            var outp = ""
            for (i in 0..15) {
                outp += ( if(i % 4 == 0) "\n" else "" ) + " ${rmx[i]}"
            }
            Log.d("MVPCreator", ">>> Matrix ${name}:\n"
                + outp + "\n"
            )
        }
        val TAG = "MVPCreator"
    }
}