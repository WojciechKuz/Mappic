package com.student.mappic.addmap

import android.widget.EditText
import android.widget.TextView
import com.student.mappic.R

class Step2and3(val addMap: AddMapActivity) {

    // redundant
    fun errMessage(): TextView {
        return addMap.findViewById<TextView>(R.id.errorText)
    }
    fun latitudeNS(ns: EditText?): EditText? {
        return addMap.findViewById<EditText>(R.id.gpsLatitude)
    }
    fun longitudeEW(ew: EditText?): EditText? {
        return addMap.findViewById<EditText>(R.id.gpsLongitude)
    }
    fun getMarkerPosition() {
        //
    }

}