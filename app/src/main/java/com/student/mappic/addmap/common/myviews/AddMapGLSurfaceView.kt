package com.student.mappic.addmap.common.myviews

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.util.Log
import com.student.mappic.clist

/**
 * View for point marked by user, rendered by [AddMapGLRenderer] using OpenGL.
 * Used in [Step2Fragment] and [Step3Fragment].
 */
class AddMapGLSurfaceView(context: Context): GLSurfaceView(context) {

    val renderer: AddMapGLRenderer // displayPoint, displayUser, clearDisplay

    init {
        Log.d(clist.MyView, ">>> default init, 1 arg")
        setEGLContextClientVersion(2) // OpenGL ES 2.0 context
        renderer = AddMapGLRenderer()
        setRenderer(renderer)

        // prevents the GLSurfaceView frame from being redrawn until you call requestRender()
        renderMode = RENDERMODE_WHEN_DIRTY // call requestRender() to refresh view
    }
    constructor(context: Context, attrs: AttributeSet): this(context) {
        Log.d(clist.MyView, ">>> init, 2 args")
    }
}