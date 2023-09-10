package com.student.mappic.addmap.common

import android.graphics.PointF
import androidx.exifinterface.media.ExifInterface
import android.net.Uri
import android.util.Log
import android.util.Size
import android.view.MotionEvent
import android.widget.TextView
import com.student.mappic.R
import com.student.mappic.addmap.AddMapActivity
import com.student.mappic.clist

// for JavaDoc to properly show links to [Step2Fragment] documentation whole name with packages must be specified. same for step3
/**
 * Fragments [Step2Fragment] and [Step3Fragment] are nearly identical.
 * It serves methods used by both.
 *
 * This class was created to prevent code duplication.
 */
class AddmapSteps2and3Utility(val addMap: AddMapActivity, val TAG: String) {

    val step2and3 = Step2and3(addMap)
    /**
     * Triggered by onTouch in MyView, handle onClick:
     * mark Point in given position.
     */
    fun myViewClicked(event: MotionEvent?) {
        if(event != null) {
            Log.d(clist.Step2Fragment, ">>> Kliknięto w " + "x: " + event.x + "; y: " + event.y)
            step2and3.getOpenGLView().pointMarker(toOpenGLCoordinates(PointF(event.x, event.y)))
        }
        else Log.w(clist.Step2Fragment, ">>> Nie otrzymano informacji o pozycji kliknięcia!")
    }
    private fun toOpenGLCoordinates(pixelCoordinates: PointF): PointF {
        val viewSize = Size(step2and3.getImageView().width, step2and3.getImageView().height)
        // TODO check if in image bounds
        //val exif = utility.getExifData(viewModel.mapImg)
        //val imageSize = Size(exif.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, -1), exif.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, -1))
        return PointF(pixelCoordinates.x/viewSize.height, pixelCoordinates.y/viewSize.height)
    }

    /**
     * Decodes from given Uri size of image and orientation. Returns true when image is vertical.
     */
    fun isImgVerticalExif(uri: Uri): Boolean {
        val exif = getExifData(uri)

        val height: Int = exif.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, -1)
        val width: Int = exif.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, -1)
        val orientation: Int = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1)

        Log.d(TAG, ">>> Size of image: h: ${height}, w: ${width}, o: ${orientation}") // o 6=vert, 1,3=horiz
        val vertOrient = intArrayOf(90, 270)
        vertOrient.forEach {
            if(it == checkOrientation(orientation)) {
                Log.d(TAG, ">>> Rotation - image is vertical.")
                return true
            }
        }
        if(checkOrientation(orientation) == -1)
            Log.e(TAG, ">>> Rotation tag in image exif data not found. cannot display it correctly.")
        else
            Log.d(TAG, ">>> Rotation - image is horizontal.")
        return false
    }
    fun getExifData(uri: Uri): ExifInterface {
        val istream = addMap.contentResolver.openInputStream(uri)
            ?: throw Exception("Input stream 'istream' is null!") // do it when istream is null
        Log.d(TAG, ">>> Decoding size of image")
        val exif = ExifInterface(istream)
        if(exif.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, -1) == -1)
            Log.e(TAG, ">>> Width tag in image exif data not found. cannot display it correctly.")
        if(exif.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, -1) == -1)
            Log.e(TAG, ">>> Height tag in image exif data not found. cannot display it correctly.")
        istream.close()
        return exif
    }

    /**
     * Decodes ExifInterface orientation codes into human-readable degrees which tell how much is image rotated clockwise.
     */
    private fun checkOrientation(orientation: Int): Int {
        val orientationList = mapOf(
            ExifInterface.ORIENTATION_NORMAL to 0,
            ExifInterface.ORIENTATION_ROTATE_90 to 90,
            ExifInterface.ORIENTATION_ROTATE_180 to 180,
            ExifInterface.ORIENTATION_ROTATE_270 to 270
        )
        // FIXME noticed Log.d("Image rotated...") executes 3 times
        for(orient: Map.Entry<Int, Int> in orientationList) {
            if(orientation == orient.key) {
                Log.d(TAG, ">>> Image rotated by ${orient.value} deg clockwise.")
                return orient.value
            }
        }
        return -1
    }

    /**
     * Fill text fields with GPS coordinates automatically
     */
    fun fillGpsCoordinates() {
        if(getPermissions()) {
            // TODO fill text fields with GPS coordinates
        }
    }
    private fun getPermissions(): Boolean {
        var retVal = false
        addMap.permManager.grantGpsPerm {
            Log.d(TAG, ">>> Permissions Granted!")
            retVal = true
        }
        return retVal
    }

    /**
     * Should verify, what user typed (coordinates and marked point).
     * @return tells if should navigate to next fragment in addmap
     */
    fun verifyUserInput(): Boolean {
        // TODO checking if typed coordinates are correct (and if point is marked correctly).
        //  Probably requires separate verifying class - use Step2and3 and create more complex things there.
        //  Additional info - in ViewModel GPS 2xFloat and PXposition 2xInt are stored together as ../DB/Point

        return true // FIXME Temporary
    }

    /**
     * save GPS coordinates and marker position on the image of a map.
     */
    fun saveUserInput() {
        // TODO save GPS Coordinates
    }

    /**
     * Displays error message in errorText TextView and of given Error type.
     * TODO WILL BECOME PRIVATE!
     */
    fun displayErrMsg(err: ErrTypes) {
        val array: Array<String> = addMap.resources.getStringArray(R.array.errTypesMessages)
        val textView = step2and3.errMessage()
        textView.text = array[err.code]
        textView.visibility = TextView.VISIBLE
    }
}