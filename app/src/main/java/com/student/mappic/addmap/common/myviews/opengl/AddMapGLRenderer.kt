package com.student.mappic.addmap.common.myviews.opengl

import android.graphics.Point
import android.opengl.GLES20
import android.opengl.GLSurfaceView.Renderer
import android.opengl.Matrix
import android.util.Log
import android.util.Size
import com.student.mappic.clist
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * Renderer for AddMApGLSurfaceView.
 */
class AddMapGLRenderer: Renderer {
    private lateinit var tri :Triangle

    override fun onSurfaceCreated(unused: GL10?, config: EGLConfig?) {
        // construct shapes, this needs to be lateinit, cause OpenGL ES stuff needs to be initialized first
        tri = Triangle()
    }

    private val vPMatrix = FloatArray(16)
    private val projectionMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)

    override fun onSurfaceChanged(unused: GL10?, width: Int, height: Int) {
        Log.d(clist.AddMapGLRenderer, ">>> Surface change - w: ${width}, h: ${height}.")
        GLES20.glViewport(0, 0, width, height)

        val prop = proportion(Size(width, height)) // you can compare it with statics from ImgSizeCalc like PROP16TO9
        // scale width by prop
        // react to size changes of surface

        val ratio: Float = width.toFloat() / height.toFloat()

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 7f)
    }

    override fun onDrawFrame(unused: GL10?) {
        // Set the camera position (View matrix)
        Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, 3f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)

        // Calculate the projection and view transformation
        Matrix.multiplyMM(vPMatrix, 0, projectionMatrix, 0, viewMatrix, 0)

        // Draw shape
        tri.draw(vPMatrix)

        // TODO ---- DRAW YOUR OWN SHAPE ----

    }

    private fun proportion(img: Size): Double {
        return (img.width * 1.0) / (img.height * 1.0)
    }

    // My drawing methods
    fun displayPoint(p: Point) {
        // TODO display point
    }
    fun displayUser(p: Point, rotation: Double) {
        // TODO display user
    }
    fun clearDisplay() {
        // TODO clear
    }

}