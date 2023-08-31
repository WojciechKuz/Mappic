package com.student.mappic.addmap.common.myviews

import android.graphics.Point
import android.opengl.GLSurfaceView.Renderer
import android.util.Log
import com.student.mappic.clist
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * Renderer for AddMApGLSurfaceView.
 */
class AddMapGLRenderer: Renderer {
    override fun onSurfaceCreated(unused: GL10?, config: EGLConfig?) {
        TODO("Not yet implemented")
    }

    override fun onSurfaceChanged(unused: GL10?, width: Int, height: Int) {
        Log.d(clist.AddMapGLRenderer, ">>> Surface change - w: ${width}, h: ${height}.")
        TODO("Not yet implemented")
    }

    override fun onDrawFrame(unused: GL10?) {
        TODO("Not yet implemented")
    }

    // My drawing methods
    fun displayPoint(p: Point) {
        // TODO display point
    }
    fun displayUser(p: Point, rotation: Double) {
        // TODO display user
    }
    fun clearDisplay() {
        // TODO
    }
}