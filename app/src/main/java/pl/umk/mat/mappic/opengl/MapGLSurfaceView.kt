package pl.umk.mat.mappic.opengl

import android.content.Context
import android.graphics.PixelFormat
import android.graphics.PointF
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.util.Log
import pl.umk.mat.mappic.clist

/**
 * This class is used for rendering in AddMap step 2, 3 and is used in ViewMap.
 * View for point marked by user, rendered by [MapGLRenderer] using OpenGL.
 * Used in [Step2Fragment] and [Step3Fragment].
 */
class MapGLSurfaceView(context: Context, attrs: AttributeSet): GLSurfaceView(context, attrs) {

    val renderer: MapGLRenderer // displayPoint, displayUser, clearDisplay

    init {
        setEGLContextClientVersion(2) // OpenGL ES 2.0 context

        // this creates transparent background
        setZOrderOnTop(true)
        setEGLConfigChooser(8, 8, 8, 8, 16, 0)
        holder?.setFormat(PixelFormat.RGBA_8888)

        // set our fellow renderer
        renderer = MapGLRenderer()
        setRenderer(renderer)
        Log.d(clist.MapGLSurfaceView, ">>> Renderer is set.")

        // prevents the GLSurfaceView frame from being redrawn until you call requestRender()
        renderMode = RENDERMODE_WHEN_DIRTY // call requestRender() to refresh view

        requestRender() // documentation: "can be used from any thread"
    }

    /**
     * Render point marker in given position
     */
    fun pointMarker(p: PointF) {
        Log.d(clist.MapGLSurfaceView, ">>> pointMarker [${p.x}, ${p.y}]")
        renderer.displayPoint(p)
        requestRender()
    }
    /**
     * Render user marker in given position
     */
    fun userMarker(p: PointF, rotation: Float) {
        Log.d(clist.MapGLSurfaceView, ">>> userMarker [${p.x}, ${p.y}]")
        renderer.displayUser(p, rotation)
        requestRender()
    }
    /** Change size of user marker */
    fun userSize(multiplyBy: Int, divideBy: Int) {
        renderer.userSize(multiplyBy, divideBy)
        requestRender()
    }
    fun clear() {
        renderer.clearDisplay()
        requestRender()
    }

    // renderMode can be set from outside (i think, if not true, create method)
}