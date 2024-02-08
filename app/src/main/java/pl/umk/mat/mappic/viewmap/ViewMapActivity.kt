package pl.umk.mat.mappic.viewmap

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.PointF
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import pl.umk.mat.mappic.MainActivity
import pl.umk.mat.mappic.MapOptionsPopup
import pl.umk.mat.mappic.R
import pl.umk.mat.mappic.addmap.features.permissions.PermissionManager
import pl.umk.mat.mappic.addmap.location.LocationProvider
import pl.umk.mat.mappic.clist
import pl.umk.mat.mappic.common.ImageSizeCalc
import pl.umk.mat.mappic.common.PositionCalc
import pl.umk.mat.mappic.db.MPoint
import pl.umk.mat.mappic.opengl.MapGLSurfaceView
import java.util.stream.Collectors
import android.content.res.Configuration
import pl.umk.mat.mappic.common.Signal

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_map)

        // read extra - mapid
        viewModel.mapid = intent.getLongExtra("whichmap", -1)
        if (!checkMapid("onCreate()")) {
            Log.e(clist.ViewMapActivity, ">>> whichmap extra received incorrectly, didn't set mapid.")
        }
        locationProvider = LocationProvider(this, getPermissionManager())
        glView = findViewById<MapGLSurfaceView>(R.id.mapView)

        // set map_name field height to height of backArrow button
        val nameView = findViewById<TextView>(R.id.map_name)
        nameView.layoutParams.height = findViewById<ImageButton>(R.id.backArrow).height
        viewModel.getMapName(this, viewModel.mapid!!) { nameView.text = it }

        /* // moved to onStart()
        // set popup menu for 3 dots button, get map name for it
        //setMapPopup()

        // get uri and set image
        //getMapImg()

        // get reference points
        //getMapRefPoints()
        */

        findViewById<ImageButton>(R.id.backArrow).setOnClickListener { backToMapList() }
        locationProvider.activityOnCreate()
        created = true
    }

    /** Used in onStart(). Makes sure everything is initialized. */
    private fun newOnStart(initFinished: Signal? = null) {
        // all in one runBlocking, or in separate ones?

        checkMapid("newStart()")

        // set popup menu for 3 dots button, get map name for it
        setMapPopup()

        var mapImgSet = false
        var refPointsSet = false
        /** Check if finished init */
        fun isInitDone() {
            if(mapImgSet && refPointsSet)
                initFinished?.startAction()
        }

        // get uri and set image
        getMapImg() {
            //Log.d(clist.ViewMapActivity, ">>> mapImg is now initialized!")
            mapImgSet = true
            isInitDone()
        }

        // get reference points
        getMapRefPoints() {
            refPointsSet = true
            isInitDone()
        }
    }

    private fun setMapPopup() {
        val nameView = findViewById<TextView>(R.id.map_name)
        // set popup menu for 3 dots button, get map name for it
        val popup = MapOptionsPopup(this, viewModel.mapid!!)
        popup.setDeleteFun({ con, id -> viewModel.deleteMap(con, id) })
        viewModel.getMapName(this, viewModel.mapid!!) {
            popup.setMapName(it)
            nameView.text = it
        } // DB access may take a while, it's fine
        findViewById<ImageButton>(R.id.mapOptions).setOnClickListener { popup.openPopupMenu(it) }
    }

    private fun getMapImg(doNext: Signal? = null) {
        if(viewModel.mapid == null) {
            Log.e(clist.ViewMapActivity, ">>> mapid is null before using it in setMapImg!")
        }
        // get uri and set image
        viewModel.getMapImages(this, viewModel.mapid!!) {
            viewModel.mapImg = Uri.parse(it[0].uri)
            // set image here or in runOnUiThread ???
            runOnUiThread {
                findViewById<ImageView>(R.id.mapBackground).setImageURI(viewModel.mapImg)
                setImageCalc() {
                    doNext?.startAction() // do if not null
                }
            }
            if(iflog) Log.d(clist.ViewMapActivity, ">>> Got mapImg Uri")
        }
    }

    private fun getMapRefPoints(doNext: Signal? = null) {
        // get reference points
        viewModel.getMapReferencePoints(this, viewModel.mapid!!) {
            val mapPoints = it.stream().map{ dbPoint -> MPoint.toMPoint(dbPoint) }.collect(Collectors.toList())
            if(mapPoints.size >= 2) {
                //Log.d(clist.ViewMapActivity, ">>> Got points from DB:\n${mapPoints.subList(0, 2)}") // OK CORRECT
                posCalc = PositionCalc(mapPoints.subList(0, 2))
                if(iflog) Log.d(clist.ViewMapActivity, ">>> Got points")
            }
            else {
                // error!
            }
            doNext?.startAction()
        }
    }

    private fun setImageCalc(doNext: Signal? = null) {
        val imgView = findViewById<ImageView>(R.id.mapBackground)
        val origImgSize = SizeGetter(this).origImgSizeGet(viewModel.mapImg) // UI Thread ok?
        imgView.post {
            val viewSize = SizeGetter.viewSizeGet(imgView)
            imgCalc = ImageSizeCalc(
                origImgSize?: viewSize,
                viewSize
            )
            if (origImgSize == null) {
                Log.e(clist.ViewMapActivity, ">>> original size of image not found")
            }
            doNext?.startAction()
        }
    }

    //private var started = false
    override fun onStart() {
        //if(!started) super.onStart()
        super.onStart()
        /*
        lifecycle.coroutineScope.launch {
            newOnStartContent()
        }*/

        // feels like it doesn't change anything. Maybe 'suspend' should be added?
        //runBlocking { newOnStartContent() } // do not execute any further, until this is completed

        // instead of runBlocking, passed
        newOnStart() {
            // init finished
            locationProvider.startLocationUpdates { receiveLocation(it) }
            Log.d(clist.ViewMapActivity, ">>> everything is initialized")
        }

        /*
        // check if all fields are initialized
        checkMapid("onStart")
        // this can be because onCreate is not executed or values are not assigned there yet.
        if(!viewModel.mapImgInitialized()) {
            //if(iflog) Log.d(clist.ViewMapActivity, ">>> onStart will be done later")
            Log.e(clist.ViewMapActivity, ">>> mapImg is not initialized!")

            // This is not a good practise, getMapImg() should be executed
            //  synchronously/blocking? (wait for finishing) and then continue.

            //getMapImg { onStart() } // do onStart() when done
            //started = true
            return
        }
        if(!this::imgCalc.isInitialized) { // probably if body above does this already
            //if(iflog) Log.d(clist.ViewMapActivity, ">>> onStart, initializing imageSizeCalc()")
            Log.e(clist.ViewMapActivity, ">>> imgSizeCalc is not initialized!")
            //setImageCalc()
        }

        //locationProvider.startLocationUpdates { receiveLocation(it) }
        */
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
    var angle = 0.0

    /**
     * This is executed when locationProvider passes location.
     * Calculates user marker position and orientation based on user's real geographical location.
     * Then it draws marker on the screen.
     */
    fun receiveLocation(loc: Location) {

        // When rotating screen there's moment when some fields are not initialized,
        //  but location update comes. Therefore, this check.
        if(!this::imgCalc.isInitialized)
            return

        val origPoint = posCalc.whereUser(PointF(
            loc.longitude.toFloat(), // between -180.0 and 180.0 inclusive EW x
            loc.latitude.toFloat() // between -90.0 and 90.0 inclusive NS y
        ))
        if(iflog) Log.d(clist.ViewMapActivity, ">>> User is in ${loc.latitude.toFloat()} N, ${loc.longitude.toFloat()} E")
        val viewPoint = imgCalc.toViewPoint(origPoint) // point inView

        if(iflog) Log.d(clist.ViewMapActivity, ">>> Original point: ${origPoint}")
        if(iflog) Log.d(clist.ViewMapActivity, ">>> In View point: ${viewPoint}")

        if(viewModel.lastPoint != null) {
            // use lastPoint to point in opposite direction (forward)
            // direction will be changed only if difference between last and current point is greater than 10m.
            val coveredDistance = PositionCalc.geoPosToDist(viewPoint, viewModel.lastPoint!!)
            val angleChange = PositionCalc.toDeg(PositionCalc.pointAt(viewPoint, viewModel.lastPoint!!)) + 180.0
            angle = if(coveredDistance > 10.0) angleChange else angle

            // change lastPoint only if difference is greater than 10m - for increasing orientation stability
            viewModel.lastPoint = if(coveredDistance > 10.0)
                PointF(viewPoint.x, viewPoint.y) // copy
            else
                viewModel.lastPoint
        }
        val viewSize = SizeGetter.viewSizeGet(findViewById(R.id.mapBackground))
        val gluser = ImageSizeCalc.toOpenGLPoint(viewSize, viewPoint)

        Log.d(clist.ViewMapActivity, ">>> OpenGL user point: ${gluser.x}, ${gluser.y}, $angle")
        glView.userMarker(
            gluser, angle.toFloat()
        )
        if(!changedSize) {
            when (resources.configuration.orientation) {
                Configuration.ORIENTATION_LANDSCAPE -> glView.userSize(1, 1)
                Configuration.ORIENTATION_PORTRAIT -> glView.userSize(3, 8)
                Configuration.ORIENTATION_SQUARE -> glView.userSize(1, 2)
                Configuration.ORIENTATION_UNDEFINED -> glView.userSize(1, 2)
            }
            changedSize = true
        }
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

    /**
     * Checks if viewModel.mapid is set (not null).
     * Returns true if not null, returns false if null.
     */
    private fun checkMapid(funName: String): Boolean {
        if(viewModel.mapid != null) {
            if(iflog) Log.d(clist.ViewMapActivity, ">>> mapid is set in $funName")
            return true
        } else {
            Log.e(clist.ViewMapActivity, ">>> mapid is null in $funName\nNULL NULL NULL")
            return false
        }
    }
}
/*
"""
The MOST RECENT APPROACH would be to use extension properties in ViewModel and Activity/Fragment:

    In ViewModel we can use viewModelScope to launch a coroutine:

    viewModelScope.launch { ... }

It attached to the lifecycle of Activity/Fragment and cancels launched coroutines when they destroyed.

    Similar in Activity/Fragment we can use the following extension properties to launch a coroutine: lifecycleScope.launch {}, lifecycle.coroutineScope.launch {}, viewLifecycleOwner.lifecycleScope.launch {}(applicable in Fragments).
""" ~Sergio, StackOverflow https://stackoverflow.com/questions/53928668/suspend-function-callgetapi-should-be-called-only-from-a-coroutine-or-another

additional option is runBlocking {}
 */