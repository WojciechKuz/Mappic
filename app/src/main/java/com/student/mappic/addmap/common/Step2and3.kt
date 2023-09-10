package com.student.mappic.addmap.common

import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.student.mappic.R
import com.student.mappic.addmap.AddMapActivity
import com.student.mappic.addmap.common.myviews.MyView
import com.student.mappic.addmap.common.myviews.opengl.AddMapGLSurfaceView

/**
 * Methods in this class shorten getting UI elements in code. Works for [Step2Fragment] and [Step3Fragment]
 */
class Step2and3(val addMap: AddMapActivity) {

    // redundant
    fun latitudeNS(ns: EditText?): EditText? {
        return addMap.findViewById<EditText>(R.id.gpsLatitude)
    }
    fun longitudeEW(ew: EditText?): EditText? {
        return addMap.findViewById<EditText>(R.id.gpsLongitude)
    }
    fun getMarkerPosition() {
        //
    }

    // FIXME compiler thinks those won't be null, but when used (or checked) it causes NullPointerException
    // This methods shorten getting UI elements in code. Works in Step2 and Step3
    fun errMessage(): TextView {
        return addMap.findViewById<TextView>(R.id.errorText)
    }
    fun getImageView(): ImageView {
        return addMap.findViewById<ImageView>(R.id.imgView)
    }
    fun getOpenGLView(): AddMapGLSurfaceView {
        return addMap.findViewById<AddMapGLSurfaceView>(R.id.openGLView)
    }
    fun getTouchDetector(): MyView {
        return addMap.findViewById<MyView>(R.id.touchDetector)
    }
}