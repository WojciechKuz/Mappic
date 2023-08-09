package com.student.mappic.addmap

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.student.mappic.DB.Point

class NewMapViewModel: ViewModel() {
    lateinit var mapImg: Uri
    lateinit var p1: Point
    lateinit var p2: Point
    lateinit var name: String
}