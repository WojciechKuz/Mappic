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
import android.util.Size
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.UiThread
import androidx.core.graphics.toPointF
import com.google.android.material.snackbar.Snackbar
import pl.umk.mat.mappic.MainActivity
import pl.umk.mat.mappic.MapOptionsPopup
import pl.umk.mat.mappic.R
import pl.umk.mat.mappic.addmap.AddMapActivity
import pl.umk.mat.mappic.addmap.features.permissions.PermissionManager
import pl.umk.mat.mappic.addmap.location.LocationProvider
import pl.umk.mat.mappic.clist
import pl.umk.mat.mappic.common.ImageSizeCalc
import pl.umk.mat.mappic.common.PositionCalc
import pl.umk.mat.mappic.db.MPoint
import pl.umk.mat.mappic.db.entities.DBPoint
import pl.umk.mat.mappic.opengl.MapGLSurfaceView
import java.util.stream.Collectors
import android.content.res.Configuration

/** If log debug messages */
private const val iflog = true

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
    //private var iscInitialized: Boolean = false
    //private var pcInitialized: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_map)

        // set map_name field height to height of backArrow button
        val nameView = findViewById<TextView>(R.id.map_name)
        nameView.layoutParams.height = findViewById<ImageButton>(R.id.backArrow).height

        // read extra - mapid
        viewModel.mapid = intent.getLongExtra("whichmap", -1)
        locationProvider = LocationProvider(this, getPermissionManager())
        glView = findViewById<MapGLSurfaceView>(R.id.mapView)

        // set popup menu for 3 dots button, get map name for it
        val popup = MapOptionsPopup(this, viewModel.mapid!!)
        popup.setDeleteFun({ con, id -> viewModel.deleteMap(con, id) })
        viewModel.getMapName(this, viewModel.mapid!!) {
            popup.setMapName(it)
            nameView.text = it
        } // DB access may take a while, it's fine

        // get uri and set image
        viewModel.getMapImages(this, viewModel.mapid!!) {
            viewModel.mapImg = Uri.parse(it[0].uri)
            // set image here or in runOnUiThread ???
            runOnUiThread {
                findViewById<ImageView>(R.id.mapBackground).setImageURI(viewModel.mapImg)
                setImageCalc()
            }
            if(iflog) Log.d(clist.ViewMapActivity, ">>> Got mapImg Uri")
        }

        // get reference points
        viewModel.getMapReferencePoints(this, viewModel.mapid!!) {
            val mapPoints = it.stream().map{ dbPoint -> MPoint.toMPoint(dbPoint) }.collect(Collectors.toList())
            if(mapPoints.size >= 2) {
                //Log.d(clist.ViewMapActivity, ">>> Got points from DB:\n${mapPoints.subList(0, 2)}") // OK CORRECT
                posCalc = PositionCalc(mapPoints.subList(0, 2)) // SUS
                if(iflog) Log.d(clist.ViewMapActivity, ">>> Got points")
            }
            else {
                // error!
            }
        }


        findViewById<ImageButton>(R.id.backArrow).setOnClickListener { backToMapList() }
        findViewById<ImageButton>(R.id.mapOptions).setOnClickListener { popup.openPopupMenu(it) }
        //findViewById<MapGLSurfaceView>(R.id.mapView).setOnClickListener { fakeUserPosition() }
        locationProvider.activityOnCreate()
        created = true
    }

    private fun setImageCalc() {
        val imgView = findViewById<ImageView>(R.id.mapBackground)
        val origImgSize = SizeGetter(this).origImgSizeGet(viewModel.mapImg) // UI Thread ok?
        imgView.post {
            val viewSize = SizeGetter.viewSizeGet(imgView)
            imgCalc = ImageSizeCalc(
                origImgSize?: viewSize,
                viewSize
            )
        }
    }

    override fun onStart() {
        super.onStart()
        if(!this::imgCalc.isInitialized) {
            if(iflog) Log.d(clist.ViewMapActivity, ">>> onStart, initializing imageSizeCalc()")
            setImageCalc()
        }
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

    private var changedSize = false

    fun receiveLocation(loc: Location) {

        // When rotating screen there's moment when some fields are not initialized,
        //  but location update comes. Therefore, this check.
        if(!this::imgCalc.isInitialized)
            return

        val origPoint = posCalc.basic_whereUser(PointF(
            loc.longitude.toFloat(), // between -180.0 and 180.0 inclusive EW x
            loc.latitude.toFloat() // between -90.0 and 90.0 inclusive NS y
        ))
        if(iflog) Log.d(clist.ViewMapActivity, ">>> User is in ${loc.latitude.toFloat()} N, ${loc.longitude.toFloat()} E")
        val viewPoint = imgCalc.toViewPoint(origPoint) // point inView

        if(iflog) Log.d(clist.ViewMapActivity, ">>> Original point: ${origPoint}")
        if(iflog) Log.d(clist.ViewMapActivity, ">>> In View point: ${origPoint}")

        if(true) {
            var angle = 0.0
            if(viewModel.lastPoint != null) {
                // use lastPoint to point in opposite direction (forward)
                angle = PositionCalc.toDeg(PositionCalc.pointAt(viewPoint, viewModel.lastPoint!!)) + 180.0
            }
            val viewSize = SizeGetter.viewSizeGet(findViewById(R.id.mapBackground))
            val gluser = ImageSizeCalc.toOpenGLPoint(viewSize, viewPoint)

            Log.d(clist.ViewMapActivity, ">>> OpenGL user point: ${gluser.x}, ${gluser.y}, $angle")
            glView.userMarker(
                gluser, angle.toFloat()
            )
            if(!changedSize) {
                when (resources.configuration.orientation) {
                    Configuration.ORIENTATION_LANDSCAPE -> glView.userSize(3, 4)
                    Configuration.ORIENTATION_PORTRAIT -> glView.userSize(3, 8)
                    Configuration.ORIENTATION_SQUARE -> glView.userSize(1, 2)
                    Configuration.ORIENTATION_UNDEFINED -> glView.userSize(1, 2)
                }
                changedSize = true
            }
        }
        viewModel.lastPoint = PointF(viewPoint.x, viewPoint.y) // copy
    }

    private var glfake = PointF(0f, 0f)

    fun fakeUserPosition() {
        glfake.x -= 0.05f
        //glfake.y -= 0.05f
        Log.d(clist.ViewMapActivity, ">>> Fake OpenGL user point: ${glfake.x}, ${glfake.y}")
        glView.userMarker(
            glfake, 0.0f
        )
    }

    private fun backToMapList() {
        val gotoList = Intent(this, MainActivity::class.java)
        startActivity(gotoList)
    }

    private fun getPermissionManager(): PermissionManager {
        if(!created)
            Log.w(clist.ViewMapActivity, ">>> Can't get Permission manager. Activity not created yet !!!")
        return permManager
    }

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
/*
Methods of executing on ui thread:

# no 1.
runOnUiThread {}

# no 2.
@UiThread
fun doUiThing() {
    // do it here
}
doUiThing()

# no 3*.
Looper.getMainLooper().run {} // * - Usually works. MainThread != UiThread (Most times equal, sometimes not.)

 */