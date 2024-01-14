package com.student.mappic.viewmap

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.ImageButton
import com.student.mappic.MainActivity
import com.student.mappic.MapOptionsPopup.Companion.openPopupMenu
import com.student.mappic.R

/**
 * This activity shows user map and displays user's location on it
 */
class ViewMapActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_map)

        findViewById<ImageButton>(R.id.backArrow).setOnClickListener { backToMapList() }
        findViewById<ImageButton>(R.id.mapOptions).setOnClickListener { openPopupMenu(it) }
    }
    private fun backToMapList() {
        val gotoList = Intent(this, MainActivity::class.java)
        startActivity(gotoList)
    }

    /*
    /**
     * Not sure if this gets the size of image or ImageView. --> If not sure check out methods in step2and3 and in addmap package
     */
    private fun getImgSize() {
        var img = findViewById<ImageView>(R.id.mapView)
        var width: Int = img.measuredWidth
        var height: Int = img.measuredHeight
    }*/

    /**
     * This gets size of image (as List(width, height)) for later calculations
     */
    private fun getDrawableSize(): List<Int> {
        val opt = BitmapFactory.Options()
        opt.inTargetDensity = DisplayMetrics.DENSITY_DEFAULT
        val bmp = BitmapFactory.decodeResource(resources, R.id.mapView, opt)
        return listOf(bmp.width, bmp.height)
    }

    // TODO show map & user position
}