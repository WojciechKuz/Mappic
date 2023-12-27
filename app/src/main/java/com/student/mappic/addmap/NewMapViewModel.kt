package com.student.mappic.addmap

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.student.mappic.DB.MPoint

/**
 * ViewModel for passing data between fragments in addmap package.
 */
class NewMapViewModel: ViewModel() {
    lateinit var mapImg: Uri
    lateinit var p1: MPoint
    lateinit var p2: MPoint
    lateinit var name: String
}