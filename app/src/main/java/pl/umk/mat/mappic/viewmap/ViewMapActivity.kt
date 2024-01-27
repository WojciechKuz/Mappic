package pl.umk.mat.mappic.viewmap

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Point
import android.graphics.PointF
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.annotation.UiThread
import androidx.core.graphics.toPointF
import pl.umk.mat.mappic.MainActivity
import pl.umk.mat.mappic.MapOptionsPopup
import pl.umk.mat.mappic.R
import pl.umk.mat.mappic.addmap.features.permissions.PermissionManager
import pl.umk.mat.mappic.addmap.location.LocationProvider
import pl.umk.mat.mappic.clist
import pl.umk.mat.mappic.common.ImageSizeCalc
import pl.umk.mat.mappic.common.PositionCalc
import pl.umk.mat.mappic.db.MPoint
import pl.umk.mat.mappic.db.entities.DBPoint
import pl.umk.mat.mappic.opengl.MapGLSurfaceView
import java.util.stream.Collectors

/**
 * This activity shows user map and displays user's location on it
 */
class ViewMapActivity : AppCompatActivity() {

    val viewModel: ViewMapViewModel by viewModels()

    private lateinit var locationProvider: LocationProvider
    private val permManager = PermissionManager(this)
    private var created = false
    private lateinit var glView: MapGLSurfaceView
    private lateinit var posCalc: PositionCalc
    private lateinit var imgCalc: ImageSizeCalc

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_map)

        viewModel.mapid = intent.getLongExtra("whichmap", -1)
        locationProvider = LocationProvider(this, getPermissionManager())
        glView = findViewById<MapGLSurfaceView>(R.id.mapView)

        // set popup menu for 3 dots button, het map name for it
        val popup = MapOptionsPopup(this, viewModel.mapid!!)
        popup.setDeleteFun({ con, id -> viewModel.deleteMap(con, id) })
        viewModel.getMapName(this, viewModel.mapid!!) {
            popup.setMapName(it)
            @UiThread
            fun setText() { // Must be done on UI thread Looper.getMainLooper().run
                findViewById<TextView>(R.id.map_name).text = it
            }
            setText()
        } // DB access may take a while, it's fine

        // get reference points
        viewModel.getMapReferencePoints(this, viewModel.mapid!!) {
            val mapPoints = it.stream().map{ dbPoint -> MPoint.toMPoint(dbPoint) }.collect(Collectors.toList())
            if(mapPoints.size >= 2) {
                posCalc = PositionCalc(mapPoints.subList(0, 2))
            }
            else {
                // error!
            }
        }

        // get uri and set image
        viewModel.getMapImages(this, viewModel.mapid!!) {
            viewModel.mapImg = Uri.parse(it[0].uri)
            findViewById<ImageView>(R.id.mapBackground).setImageURI(viewModel.mapImg)
            imgCalc = ImageSizeCalc(
                SizeGetter(this).origImgSizeGet(viewModel.mapImg),
                SizeGetter.viewSizeGet(findViewById(R.id.mapBackground))
            )
        }

        findViewById<ImageButton>(R.id.backArrow).setOnClickListener { backToMapList() }
        findViewById<ImageButton>(R.id.mapOptions).setOnClickListener { popup.openPopupMenu(it) }
        locationProvider.activityOnCreate()
        created = true
    }

    override fun onStart() {
        super.onStart()
        locationProvider.startLocationUpdates { receiveLocation(it) }
    }
    override fun onResume() {
        super.onResume()
    }
    override fun onPause() {
        super.onPause()
    }
    override fun onStop() {
        locationProvider.stopLocationUpdates()
        super.onStop()
    }

    fun receiveLocation(loc: Location) {
        val origPoint = posCalc.basic_whereUser(PointF(
            loc.latitude.toFloat(), // between -90.0 and 90.0 inclusive NS
            loc.longitude.toFloat() // between -180.0 and 180.0 inclusive EW
        ))
        val viewPoint = imgCalc.toViewPoint(origPoint) // point inView
        if(true) {
            var angle = 0.0
            if(viewModel.lastPoint != null) {
                // use lastPoint to point in opposite direction (forward
                angle = PositionCalc.toDeg(PositionCalc.pointAt(viewPoint, viewModel.lastPoint!!)) + 180.0
            }
            val viewSize = SizeGetter.viewSizeGet(findViewById(R.id.mapBackground))
            glView.userMarker(
                ImageSizeCalc.toOpenGLPoint(viewSize, viewPoint), angle.toFloat())
        }
        viewModel.lastPoint = viewPoint
    }

    private fun backToMapList() {
        val gotoList = Intent(this, MainActivity::class.java)
        startActivity(gotoList)
    }

    private fun getPermissionManager(): PermissionManager {
        if(!created)
            Log.w(clist.ViewMapActivity, ">>> Activity not created yet !!!")
        return permManager
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