package pl.umk.mat.mappic.addmap.shared_step2and3

import android.annotation.SuppressLint
import android.graphics.Point
import android.graphics.PointF
import android.location.Location
import android.net.Uri
import android.text.Editable
import android.util.Log
import android.view.MotionEvent
import android.widget.TextView
import androidx.annotation.FloatRange
import pl.umk.mat.mappic.db.MPoint
import pl.umk.mat.mappic.R
import pl.umk.mat.mappic.addmap.AddMapActivity
import pl.umk.mat.mappic.addmap.NewMapViewModel
import pl.umk.mat.mappic.addmap.location.LocationProvider
import pl.umk.mat.mappic.common.ErrTypes
import pl.umk.mat.mappic.common.ImageSizeCalc
import pl.umk.mat.mappic.common.PositionCalc

/** If log debug messages */
private const val iflog = true

// for JavaDoc to properly show links to [Step2Fragment] documentation whole name with packages must be specified. same for step3
/**
 * Fragments [Step2Fragment] and [Step3Fragment] are nearly identical.
 * It serves methods used by both.
 *
 * This class was created to prevent code duplication.
 * Some other helper methods are in [Step2and3]
 */
class AddmapSteps2and3Utility(val addMap: AddMapActivity, val TAG: String) {

    val step2and3 = Step2and3(addMap, TAG)
    private val locationProvider = LocationProvider(addMap, addMap.getPermissionManager())
    /** NOTE: these are 'in View' coordinates'.
     *  It has to be recalculated to 'originalPoint' coordinates to be saved to DB.
     *  Use ImgSizeCalc to calculate right coordinates. */
    private var viewCoords: PointF? = null
    private var gpsNS: Double? = null
    private var gpsEW: Double? = null
    private lateinit var imgCalc: ImageSizeCalc

    init {
        locationProvider.activityOnCreate() // here ???
        // this utility is initialized in fragment's onViewCreated and for sure is after activity's onCreate(), so it should be ok
    }

    /**
     * Triggered by onTouch in MyView, handle onClick:
     * mark MPoint in given position.
     */
    fun myViewClicked(event: MotionEvent?) {
        if(event != null) {
            // event.x and event.y are inView coordinates
            if(iflog) Log.d(TAG, ">>> Kliknięto w " + "x: " + event.x + "; y: " + event.y)
            step2and3.getOpenGLView().pointMarker(
                ImageSizeCalc.toOpenGLPoint(step2and3.viewSizeGet(), PointF(event.x, event.y))
            )

            // NOTE: these are coords relative to ImgView, read viewCoords documentation!
            viewCoords = PointF(event.x, event.y)
        }
        //else Log.w(clist.Step2Fragment, ">>> Nie otrzymano informacji o pozycji kliknięcia!")
    }

    /** If ViewModel already contains data fill text fields and mark point */
    fun fillFields(viewModel: NewMapViewModel, step: Int) {
        val p: MPoint?
        if(step == 2 && viewModel.isInitialized(1)) {
            p = viewModel.p1
        } else if(step == 3 && viewModel.isInitialized(2)) {
            p = viewModel.p2
        } else return
        fillGPSCoordinates(p.ygps, p.xgps)

        // Now, mark point on img
        step2and3.viewSizeWhenReady {
            val viewSize = it
            imgCalc = ImageSizeCalc(step2and3.origImgSizeGet(viewModel.mapImg)?: viewSize, viewSize)
            val ivp = imgCalc.toViewPoint(Point(p.xpx, p.ypx)) // original to inView coords
            viewCoords = ivp    // ivp is short for inViewPoint

            // openGL
            val glcoor = ImageSizeCalc.toOpenGLPoint(
                step2and3.viewSizeGet(),
                PointF(ivp.x, ivp.y)
            )
            Log.d(TAG, ">>> OpenGL coordinates: x: ${glcoor.x}, y: ${glcoor.y}")
            step2and3.getBindingOpenGLView().pointMarker(glcoor) // MARK POINT
        }
    }

    fun checkImgSizeData(viewModel: NewMapViewModel) {
        if(step2and3.origImgSizeGet(viewModel.mapImg) == null) {
            displayErrMsg(ErrTypes.NO_SIZE_DATA)
        }
    }

    /**
     * Fill text fields with GPS coordinates automatically
     */
    fun getCoordinates() {
        locationProvider.getUserLocation { this.fillGPSCoordinates(it) }
    }
    /**
     * Fills GPS coordinate text fields in app's UI. used in functional interface.
     */
    private fun fillGPSCoordinates(loc: Location) {
        // learn about Location class

        // save it with south as north negative value, and west as east negative value, but for user display E/W N/S
        loc.latitude // between -90.0 and 90.0 inclusive NS
        loc.longitude // between -180.0 and 180.0 inclusive EW
        fillGPSCoordinates(loc.latitude, loc.longitude)
    }

    /**
     * Fills GPS coordinate text fields in app's UI.
     * @param latitude NS
     * @param longitude EW
     */
    @SuppressLint("SetTextI18n")
    private fun fillGPSCoordinates(
        @FloatRange(from = -90.0, to = 90.0) latitude: Double, // NS
        @FloatRange(from = -180.0, to = 180.0) longitude: Double // EW
    ) {
        //val editableNS = step2and3.latitudeNS()
        //val editableEW = step2and3.longitudeEW() // They were using findViewById() and step3 was getting EditText of step2
        val editableNS = step2and3.bindingLatitudeNS()
        val editableEW = step2and3.bindingLongitudeEW()

        if (editableNS != null) {
            if(latitude >= 0)
                editableNS.setText("${latitude} N")
            else
                editableNS.setText("${-latitude} S")
        } else
            Log.e(TAG, ">>> editableNS is null!")
        if (editableEW != null) {
            if(longitude >= 0)
                editableEW.setText("${longitude} E")
            else
                editableEW.setText("${-longitude} W")
        } else
            Log.e(TAG, ">>> editableEW is null!")
    }

    /**
     * Decodes from given Uri size of image and orientation. Returns true when image is vertical.
     * Used in Step2 and Step3.
     */
    fun isImgVerticalExif(uri: Uri): Boolean {
        return step2and3.isImgVerticalExif(uri)
    }

    // Return or assignment can be lifted out
    /**
     * Gets GPS values from UI, and checks if they are correct.
     * Sets gpsNS and gpsEW members even if values are incorrect!!!
     * @return true if values are correct,
     *      false otherwise and sets error message in UI to let user know, what's wrong.
     */
    private fun getGpsValues(): Boolean {
        val errorValue = -128509.125
        /** returns letter symbolising direction - N, E, W, S. For '20.000 N' gets N. */
        fun getDirection(txtEd: Editable): String {
            val L = txtEd.length
            return txtEd.trim().subSequence(L - 1, L).toString()
        }
        /** returns direction value. For '20.000 N' gets 20.000 as floating point number. */
        fun getValue(txtEd: Editable): Double {
            return try {
                txtEd.subSequence(0, txtEd.length - 2).toString().trim().toDouble()
            } catch (e: NumberFormatException) {
                errorValue
            }
        }

        // read text values from UI
        val editableNS = step2and3.latitudeNS()!!.editableText
        val editableEW = step2and3.longitudeEW()!!.editableText

        // if empty check
        if(editableNS.isEmpty() || editableEW.isEmpty()) {
            displayErrMsg(ErrTypes.NOT_FILLED_GPS)
            return false
        }

        // get numeric value for NS coordinates
        if (getDirection(editableNS) == "N") {
            gpsNS = getValue(editableNS)
        } else if (getDirection(editableNS) == "S") {
            gpsNS = -getValue(editableNS)
        } else {
            displayErrMsg(ErrTypes.INCORRECT_GPS)
            return false
        }

        // get numeric value for EW coordinates
        if (getDirection(editableEW) == "E") {
            gpsEW = getValue(editableEW)
        } else if (getDirection(editableEW) == "W") {
            gpsEW = -getValue(editableEW)
        } else {
            displayErrMsg(ErrTypes.INCORRECT_GPS)
            return false
        }

        // check for error in letters
        if(gpsNS == errorValue || gpsEW == errorValue) {
            displayErrMsg(ErrTypes.INCORRECT_GPS)
            return false
        }

        // coordinate value check
        if (gpsNS!! > 90.0 || gpsNS!! < -90.0) {
            displayErrMsg(ErrTypes.INCORRECT_GPS)
            return false
        }
        if (gpsEW!! > 180.0 || gpsEW!! < -180.0) {
            displayErrMsg(ErrTypes.INCORRECT_GPS)
            return false
        }

        // if no check returned false, then coordinates are well formed
        return true
    }

    /**
     * save GPS coordinates and marker position on the image of a map.
     * If it returns true save succeeded and we can navigate to next step.
     * If it returns false, there are some errors and they are displayed to user
     */
    fun saveUserInput(viewModel: NewMapViewModel, step: Int): Boolean {

        if (!getGpsValues()) // also verifies user input: NOT_FILLED_GPS, INCORRECT_GPS
            return false

        // init helper class for coordinate recalculation
        imgCalc = ImageSizeCalc(step2and3.origImgSizeGet(viewModel.mapImg)?: step2and3.viewSizeGet(), step2and3.viewSizeGet())

        // Not checking gpsNS and gpsEW here because it's already been checked.
        if(viewCoords != null) {
            if (!imgCalc.isPointInBounds(viewCoords!!)) {
                displayErrMsg(ErrTypes.POINT_OUT_OF_BOUNDS)
                return false
            }

            // save gpsNS, gpsEW, pxCoords to viewModel
            val origPoint = imgCalc.toOriginalPoint(viewCoords!!) // recalculates to original image coordinates
            val p = MPoint(
                origPoint.x, origPoint.y,
                gpsEW!!, gpsNS!!, reference = true
            )

            if (step == 2) {
                viewModel.p1 = p
                return true
            }
            if (step == 3) {
                // If difference is greater than 10m.
                if(PositionCalc.geoPosToDist(viewModel.p1, p) > 10.0) {
                    viewModel.p2 = p
                    return true
                }
                displayErrMsg(ErrTypes.SAME_POINT)
                return false
            }
        } else {
            displayErrMsg(ErrTypes.POINT_NOT_MARKED)
            return false
        }
        // if code reaches here, this means some kind of error, that is not handled
        displayErrMsg(ErrTypes.UNKNOWN)
        return false
    }

    /**
     * Displays error message in errorText TextView and of given Error type.
     */
    fun displayErrMsg(err: ErrTypes) {
        val array: Array<String> = addMap.resources.getStringArray(R.array.errTypesMessages)
        val textView = step2and3.errMessage()
        textView.text = array[err.code]
        textView.visibility = TextView.VISIBLE
    }
}