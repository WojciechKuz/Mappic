package pl.umk.mat.mappic.opengl

import android.graphics.PointF
import android.opengl.GLES20
import android.opengl.GLSurfaceView.Renderer
import android.util.Log
import android.util.Size
import pl.umk.mat.mappic.clist
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * Renderer for AddMApGLSurfaceView.
 */
class MapGLRenderer: Renderer {

    // shapes need to be lateinit, cause OpenGL ES stuff needs to be initialized first
    // objects to be drawn properly need to be members
    private lateinit var pinTri: Triangle
    private lateinit var pinCircl: Array<Triangle>
    private lateinit var usrArrow: Array<Triangle>
    private var drawnObjects: ArrayList<Triangle> = ArrayList()

    override fun onSurfaceCreated(unused: GL10?, config: EGLConfig?) {
        // call shapes constructors here, but they can be drawn or not somewhere else.
        pinTri = Triangle()
        pinTri.setColor(0.9375f, 0.3125f, 0.4375f, 0.8125f).setVertices(
            floatArrayOf(
                0f, 0f, 0f,
                -0.09375f, 0.1875f, 0f,
                0.09375f, 0.1875f, 0f
            ) // 0.09375 == 3/32; 0.1875 == 3/16 - using such numbers is computer friendly :)
        )

        pinCircl = CircleMaker(0.09375f).createCircleCoords(48)
        pinCircl.forEach {
            it.setColor(0.9375f, 0.3125f, 0.4375f, 0.8125f)
        }

        usrArrow = Array(2) { Triangle() }
        usrArrow[0].setVertices(
            floatArrayOf(
                0f, 0.125f, 0f,
                0.09375f, -0.0625f, 0f,
                0f, -0.03125f, 0f
            )
        ).setColor(0.25f, 0.25f, 0.75f, 0.75f) // B: 13/16 = 0.8125f;
        usrArrow[1].setVertices(
            floatArrayOf(
                0f, 0.125f, 0f,
                -0.09375f, -0.0625f, 0f,
                0f, -0.03125f, 0f
            )
        ).setColor(0.25f, 0.25f, 0.875f, 0.75f) // B: 14/16
    }

    /**
     * Called when screen is resized.
     */
    override fun onSurfaceChanged(unused: GL10?, width: Int, height: Int) {
        Log.d(clist.MapGLRenderer, ">>> Surface change - w: ${width}, h: ${height}.")
        GLES20.glViewport(0, 0, width, height)

        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)

        val prop = proportion(Size(width, height)) // you can compare it with statics from ImgSizeCalc like PROP16TO9
        // scale width by prop
        // react to size changes of surface

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
        //Log.d(clist.MapGLRenderer, ">>> There is ${drawnObjects.size} triangles on drawing list.")
        drawnObjects.forEach { it.draw() }
    }

    private fun proportion(img: Size): Double {
        return (img.width * 1.0) / (img.height * 1.0)
    }

    // My drawing methods
    /**
     * Marks where user has clicked with point.
     * Changes objects listed on [drawnObjects] so when requestRender() -> onDrawFrame() is called,
     * it contains modified and newly created Triangles.
     * @param p point, where marker should be displayed, in OpenGL.
     */
    fun displayPoint(p: PointF) {
        clearDisplay()

        // tri, 1
        pinTri.setPosition(ObjPosition(0f, p.x, p.y))
        drawnObjects.add(pinTri)

        // circl, 48
        pinCircl.forEach {
            it.setPosition(ObjPosition(0f, p.x, p.y + 0.1875f))
            drawnObjects.add(it)
        }

    }

    /**
     * Marks where user is located with arrow-gps-like marker.
     * Adds objects to drawing list
     * @param p point, where marker should be displayed, in OpenGL.
     * @param rotation rotation of user-marker in degrees
     */
    fun displayUser(p: PointF, rotation: Float) {
        clearDisplay()

        usrArrow.forEach {
            it.setPosition(ObjPosition(rotation, p.x, p.y))
            drawnObjects.add(it)
        }
    }

    /**
     * Clears display, removes objects from drawing list
     */
    fun clearDisplay() {
        drawnObjects.clear()
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f) // clear background. render is requested in SurfaceView
    }
}