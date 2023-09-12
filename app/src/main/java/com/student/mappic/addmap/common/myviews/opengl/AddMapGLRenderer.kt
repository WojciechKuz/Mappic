package com.student.mappic.addmap.common.myviews.opengl

import android.graphics.Point
import android.graphics.PointF
import android.opengl.GLES20
import android.opengl.GLSurfaceView.Renderer
import android.util.Log
import android.util.Size
import com.student.mappic.clist
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * Renderer for AddMApGLSurfaceView.
 */
class AddMapGLRenderer: Renderer {
    private lateinit var tri: Triangle
    private lateinit var tri2: Triangle
    private lateinit var circl: Array<Triangle>
    //private lateinit var cir: JCircle

    override fun onSurfaceCreated(unused: GL10?, config: EGLConfig?) {
        // construct shapes, this needs to be lateinit, cause OpenGL ES stuff needs to be initialized first

        // call shapes constructors here, but they can be drawn or not somewhere else.
        tri = Triangle()
        tri2 = Triangle().setVertices(floatArrayOf(
            0.0f, -0.622008459f, 0.0f,    // top
            -0.5f, 0.311004243f, 0.0f,    // bottom left
            0.5f, 0.311004243f, 0.0f      // bottom right
        )).setColor(0.63671875f, 0.73f, 0.22265625f, 0.6f)
        //circl = CircleMaker(0.3f, PointF(0.5f, 0.2f)).circleCoords(12)
    }

    override fun onSurfaceChanged(unused: GL10?, width: Int, height: Int) {
        Log.d(clist.AddMapGLRenderer, ">>> Surface change - w: ${width}, h: ${height}.")
        GLES20.glViewport(0, 0, width, height)

        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)

        val prop = proportion(Size(width, height)) // you can compare it with statics from ImgSizeCalc like PROP16TO9
        // scale width by prop
        // react to size changes of surface

        val ratio: Float = width.toFloat() / height.toFloat()
        MVPCreator.calculateProjectionMx(ratio)
    }

    //
    override fun onDrawFrame(unused: GL10?) {
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        val mainTriangle = ObjPosition(90f,0.2f,-0.1f)
        tri2.setPosition(mainTriangle).draw()

        // Draw shape
        //tri.draw(scratch)
        //tri2.draw(scratch)
        //circl.forEach { it.draw(scratch) } // check if this matrix is correct
        //cir.draw(unused)

        // TODO ---- DRAW YOUR OWN SHAPE ----
        // TODO ---- PARAMETRIZE DRAWING (ROTATION, POSITION, APPEARANCE(POINT/USER)) ----

    }

    private fun proportion(img: Size): Double {
        return (img.width * 1.0) / (img.height * 1.0)
    }

    // My drawing methods
    // FIXME
    fun displayPoint(p: PointF) {
        Log.d(clist.AddMapGLSurfaceView, ">>> Zaznacz punkt 2")
        tri.setPosition(ObjPosition(-45f, -0.4f, 0.5f)).setColor(0.9f, 0.3f, 0.4f, 0.8f).draw()
    }
    fun displayUser(p: PointF, rotation: Float) {
        // TODO display user
    }
    fun clearDisplay() {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f) // clear background. render is requested in SurfaceView
    }

    companion object {
        val IDENTITY = floatArrayOf(
            1f,0f,0f,0f,
            0f,1f,0f,0f,
            0f,0f,1f,0f,
            0f,0f,0f,1f
        )
    }

}