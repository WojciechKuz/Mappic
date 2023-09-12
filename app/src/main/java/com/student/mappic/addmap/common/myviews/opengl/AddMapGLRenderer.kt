package com.student.mappic.addmap.common.myviews.opengl

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

    // shapes need to be lateinit, cause OpenGL ES stuff needs to be initialized first
    private lateinit var tri: Triangle
    private lateinit var tri2: Triangle
    private lateinit var circl: Array<Triangle>
    private var drawnObjects: ArrayList<Triangle> = ArrayList()

    override fun onSurfaceCreated(unused: GL10?, config: EGLConfig?) {
        // call shapes constructors here, but they can be drawn or not somewhere else.
        Log.d(clist.AddMapGLRenderer, ">>> tri init")
        tri = Triangle()
        Log.d(clist.AddMapGLRenderer, ">>> tri2 init")
        tri2 = Triangle()

        circl = CircleMaker(0.09375f).createCircleCoords(48)
        circl.forEach {
            //it.setColor(0.3f, 0.24f, 0.83f, 0.7f) // blue
            it.setColor(0.9f, 0.3f, 0.4f, 0.8f)
            drawnObjects.add(it)
        }
        alwaysDisplayed()
    }

    override fun onSurfaceChanged(unused: GL10?, width: Int, height: Int) {
        Log.d(clist.AddMapGLRenderer, ">>> Surface change - w: ${width}, h: ${height}.")
        GLES20.glViewport(0, 0, width, height)

        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)

        val prop = proportion(Size(width, height)) // you can compare it with statics from ImgSizeCalc like PROP16TO9
        // scale width by prop
        // react to size changes of surface

        //drawnObjects.add(tri)

        val ratio: Float = width.toFloat() / height.toFloat()
        MVPCreator.calculateProjectionMx(ratio)
    }

    /**
     * Draws all triangles from the list [drawnObjects]
     */
    override fun onDrawFrame(unused: GL10?) {
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        // draw all listed triangles
        Log.d(clist.AddMapGLRenderer, ">>> There is ${drawnObjects.size} triangles on drawing list.")
        drawnObjects.forEach { it.draw() }

        // Draw shape
        //tri.draw(scratch)
        //tri2.draw(scratch)
        //circl.forEach { it.draw(scratch) } // check if this matrix is correct
        //cir.draw(unused)
    }

    private fun proportion(img: Size): Double {
        return (img.width * 1.0) / (img.height * 1.0)
    }

    /**
     * Add elements to the sceen that should be in every frame. Call after cleaning display.
     */
    private fun alwaysDisplayed() {
        val mainTriangle = ObjPosition(90f,0.90f,-0.90f)
        tri2.setVertices(floatArrayOf(
            0.0f, -0.0f, 0.0f,    // top
            -0.07f, 0.1f, 0.0f,    // bottom left
            0.07f, 0.1f, 0.0f      // bottom right
        )).setColor(0.63671875f, 0.73f, 0.22265625f, 0.6f).setPosition(mainTriangle)
        drawnObjects.add(tri2)
        //circl = CircleMaker(0.3f, PointF(0.5f, 0.2f)).circleCoords(12)
    }

    // My drawing methods
    /**
     * Marks where user has clicked with point.
     * Changes objects listed on [drawnObjects] so when requestRender() -> onDrawFrame() is called,
     * it contains modified and newly created Triangles.
     */
    fun displayPoint(p: PointF) {
        drawnObjects.clear()
        alwaysDisplayed()
        tri.setPosition(ObjPosition(0f, p.x, p.y)).setColor(0.9f, 0.3f, 0.4f, 0.8f).setVertices(
            floatArrayOf(
                0f, 0f, 0f,
                -0.09375f, 0.1875f, 0f,
                0.09375f, 0.1875f, 0f
            ) // 0.09375 == 3/32; 0.1875 == 3/16 - using such numbers is computer friendly :)
        )
        drawnObjects.add(tri)
        circl.forEach { it.setPosition(ObjPosition(0f, p.x, p.y + 0.1875f)) }
        val tri3 = Triangle().setVertices(
            floatArrayOf(
                0f, 0f, 0f,
                -0.1f, 0f, 0f,
                0f, -0.1f, 0f
            )
        ).setColor(0.9f, 0.9f, 0.1f, 0.8f).setPosition(ObjPosition(40f, -0.6f, -0.6f))
        drawnObjects.add(tri3)

    }
    fun displayUser(p: PointF, rotation: Float) {
        // TODO display user
    }
    fun clearDisplay() {
        drawnObjects.clear()
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f) // clear background. render is requested in SurfaceView
    }

    /*companion object {
        val IDENTITY = floatArrayOf(
            1f,0f,0f,0f,
            0f,1f,0f,0f,
            0f,0f,1f,0f,
            0f,0f,0f,1f
        )
    }*/

}