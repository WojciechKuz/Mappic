package pl.umk.mat.mappic.viewmap

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.ImageButton
import androidx.activity.viewModels
import pl.umk.mat.mappic.MainActivity
import pl.umk.mat.mappic.MapOptionsPopup
import pl.umk.mat.mappic.R

/**
 * This activity shows user map and displays user's location on it
 */
class ViewMapActivity : AppCompatActivity() {

    val viewModel: ViewMapViewModel by viewModels()

    val mapid: Long = intent.getLongExtra("whichmap", -1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_map)

        val popup = MapOptionsPopup(this, mapid)
        popup.setDeleteFun({ con, id -> viewModel.deleteMap(con, id) })
        viewModel.getMapName(this, mapid) { popup.setMapName(it) } // DB access may take a while, it's fine

        findViewById<ImageButton>(R.id.backArrow).setOnClickListener { backToMapList() }
        findViewById<ImageButton>(R.id.mapOptions).setOnClickListener { popup.openPopupMenu(it) }
    }
    private fun backToMapList() {
        val gotoList = Intent(this, pl.umk.mat.mappic.MainActivity::class.java)
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