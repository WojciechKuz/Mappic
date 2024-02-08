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

    // sometimes order of execution flips - displayPoint() before onSurfaceCreated()
    private var displayWasFirst = false // if order flipped
    private var laterPointF: PointF = PointF()

    // color of user arrow, order: R, G, B, a. All in range [0, 1].
    private val color0 = floatArrayOf(
        0.5f, 0.5f, 0.75f, 0.75f
    )
    private val color1 = floatArrayOf(
        0.5f, 0.5f, 0.875f, 0.75f
    )

    override fun onSurfaceCreated(unused: GL10?, config: EGLConfig?) {
        // call shapes constructors here, but they can be drawn or not somewhere else.
        pinTri = Triangle()
        pinTri.setColor(0.9375f, 0.3125f, 0.4375f, 0.8125f).setVertices(
            floatArrayOf(
                0f, 0f, 0f,
                -0.09375f, 0.1875f, 0f,
                0.09375f, 0.1875f, 0f
            ) // using such numbers is computer friendly :)
        )
        Log.d(clist.MapGLRenderer, ">>> pinTri initialized")

        pinCircl = CircleMaker(0.09375f).createCircleCoords(48)
        pinCircl.forEach {
            it.setColor(0.9375f, 0.3125f, 0.4375f, 0.8125f)
        }
        Log.d(clist.MapGLRenderer, ">>> pinCircl initialized")

        usrArrow = Array(2) { Triangle() }
        usrArrow[0].setVertices(
            floatArrayOf(
                0f, 0.125f, 0f,
                0.09375f, -0.0625f, 0f,
                0f, -0.03125f, 0f
            )
        ).setColor(color0[0], color0[1], color0[2], color0[3]) // B: 13/16 = 0.8125f;
        usrArrow[1].setVertices(
            floatArrayOf(
                0f, 0.125f, 0f,
                -0.09375f, -0.0625f, 0f,
                0f, -0.03125f, 0f
            )
        ).setColor(color1[0], color1[1], color1[2], color1[3]) // B: 14/16

        if(displayWasFirst) {
            Log.w(clist.MapGLRenderer, ">>> calling displayPoint() again")
            displayPoint(laterPointF)
        }
    }

    /**
     * Called when screen is resized.
     */
    override fun onSurfaceChanged(unused: GL10?, width: Int, height: Int) {
        Log.d(clist.MapGLRenderer, ">>> SURFACE CHANGE Surface change - w: ${width}, h: ${height}.")
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
        Log.w(clist.MapGLRenderer, ">>> DRAW FRAME")
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        // draw all listed triangles
        //Log.d(clist.MapGLRenderer, ">>> There is ${drawnObjects.size} triangles on drawing list.")
        drawnObjects.forEach { it.draw() }
    }

    private fun proportion(img: Size): Double {
        return (img.width * 1.0) / (img.height * 1.0)
    }

    /**
     * Marks where user has clicked with point.
     * Changes objects listed on [drawnObjects] so when requestRender() -> onDrawFrame() is called,
     * it contains modified and newly created Triangles.
     * @param p point, where marker should be displayed, in OpenGL.
     */
    fun displayPoint(p: PointF) {
        Log.d(clist.MapGLRenderer, ">>> DISPLAY_POINT displayPoint()")
        clearDisplay()

        if(!this::pinTri.isInitialized || !this::pinCircl.isInitialized) {
            Log.w(clist.MapGLRenderer, ">>> pinTri, pinCircl weren't initialized before calling displayPoint()")
            displayWasFirst = true
            laterPointF = p
            return
        }

        // tri, 1
        pinTri.setPosition(ObjPosition(0f, p.x, p.y))
        drawnObjects.add(pinTri)

        // circl, 48
        pinCircl.forEach {
            it.setPosition(ObjPosition(0f, p.x, p.y + 0.1875f))
            drawnObjects.add(it)
        }
        Log.d(clist.MapGLRenderer, ">>> POINT ADDED TO DRAW ARRAY")
    }

    /** Change size of user marker */
    fun userSize(multiplyBy: Int, divideBy: Int) {
        if(!this::usrArrow.isInitialized) {
            Log.w(clist.MapGLRenderer, ">>> usrArrow wasn't initialized before calling userSize()")
            return
        }
        fun rec(f: Float): Float {
            return if (divideBy != 0) f * multiplyBy / divideBy else f * multiplyBy
        }
        usrArrow[0].setVertices(floatArrayOf(
            rec(0f), rec(0.125f), 0f,
            rec(0.09375f), rec(-0.0625f), 0f,
            rec(0f), rec(-0.03125f), 0f
        )).setColor(color0[0], color0[1], color0[2], color0[3])
        usrArrow[1].setVertices(floatArrayOf(
            rec(0f), rec(0.125f), 0f,
            rec(-0.09375f), rec(-0.0625f), 0f,
            rec(0f), rec(-0.03125f), 0f
        )).setColor(color1[0], color1[1], color1[2], color1[3])
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
        //Log.d(clist.MapGLRenderer, ">>> CLEAR")
        drawnObjects.clear()
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f) // clear background. render is requested in SurfaceView
    }
}