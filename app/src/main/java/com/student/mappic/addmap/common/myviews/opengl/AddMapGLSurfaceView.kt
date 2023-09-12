package com.student.mappic.addmap.common.myviews.opengl

import android.content.Context
import android.graphics.PixelFormat
import android.graphics.PointF
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.util.Log
import com.student.mappic.clist

/**
 * This class (will be renamed) is used for rendering in AddMap step 2, 3 and (TODO) used in ViewMap.
 * View for point marked by user, rendered by [AddMapGLRenderer] using OpenGL.
 * Used in [Step2Fragment] and [Step3Fragment].
 */
class AddMapGLSurfaceView(context: Context, attrs: AttributeSet): GLSurfaceView(context, attrs) {

    val renderer: AddMapGLRenderer // displayPoint, displayUser, clearDisplay

    init {
        Log.d(clist.MyView, ">>> default init, 2 arg")

        setEGLContextClientVersion(2) // OpenGL ES 2.0 context

        // this creates transparent background
        setZOrderOnTop(true)
        setEGLConfigChooser(8, 8, 8, 8, 16, 0)
        holder?.setFormat(PixelFormat.RGBA_8888)

        // set our fellow renderer
        renderer = AddMapGLRenderer()
        setRenderer(renderer)
        Log.d(clist.AddMapGLSurfaceView, ">>> Renderer is set.")

        // prevents the GLSurfaceView frame from being redrawn until you call requestRender()
        // FIXME RENDERMODE
        renderMode = RENDERMODE_WHEN_DIRTY // call requestRender() to refresh view

        requestRender() // "can be used from any thread"
    }

    /**
     * Render point marker in given position
     */
    fun pointMarker(p: PointF) {
        Log.d(clist.AddMapGLSurfaceView, ">>> Zaznacz punkt")

        renderer.displayPoint(p) // FIXME call to OpenGL ES API with no current context (logged once per thread)

        requestRender()
    }
    /**
     * Render user marker in given position
     */
    fun userMarker(p: PointF, rotation: Float) {
        renderer.displayUser(p, rotation)
        requestRender()
    }
    fun clear() {
        renderer.clearDisplay()
        requestRender()
    }

    // renderMode can be set from outside (i think, if not true, create method)
}