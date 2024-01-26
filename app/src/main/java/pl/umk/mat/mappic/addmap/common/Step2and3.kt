package pl.umk.mat.mappic.addmap.common

import android.net.Uri
import android.util.Log
import android.util.Size
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.exifinterface.media.ExifInterface
import pl.umk.mat.mappic.R
import pl.umk.mat.mappic.addmap.AddMapActivity
import pl.umk.mat.mappic.addmap.NewMapViewModel
import pl.umk.mat.mappic.addmap.common.myviews.MyView
import pl.umk.mat.mappic.opengl.MapGLSurfaceView

/**
 * Methods in this class shorten getting UI elements in code. Works for [Step2Fragment] and [Step3Fragment]
 * Used in [AddmapSteps2and3Utility]
 */
class Step2and3(val addMap: AddMapActivity, val TAG: String) {

    // redundant
    fun latitudeNS(): EditText? {
        return addMap.findViewById<EditText>(R.id.gpsLatitude)
    }
    fun longitudeEW(): EditText? {
        return addMap.findViewById<EditText>(R.id.gpsLongitude)
    }
    /*fun getMarkerPosition() {
        //
    }*/

    // nFIXME compiler thinks those won't be null, but when used (or checked) it causes NullPointerException
    //  maybe add ? to returned value?
    // This methods shorten getting UI elements in code. Works in Step2 and Step3
    fun errMessage(): TextView {
        return addMap.findViewById<TextView>(R.id.errorText)
    }
    fun getImageView(): ImageView {
        return addMap.findViewById<ImageView>(R.id.imgView)
    }
    fun getOpenGLView(): MapGLSurfaceView {
        return addMap.findViewById<MapGLSurfaceView>(R.id.openGLView)
    }
    fun getTouchDetector(): MyView {
        return addMap.findViewById<MyView>(R.id.touchDetector)
    }

    /** Gets Exif Interface for obtaining information about image from image Uri */
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
        for(orient: Map.Entry<Int, Int> in orientationList) {
            if(orientation == orient.key) {
                //Log.d(TAG, ">>> Image rotated by ${orient.value} deg clockwise.")
                return orient.value
            }
        }
        return -1
    }

    fun origImgSizeGet(viewModel: NewMapViewModel): Size {
        val exif = getExifData(viewModel.mapImg)
        val width: Int = exif.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, -1)
        val height: Int = exif.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, -1)
        return if(!isImgVerticalExif(viewModel.mapImg)) Size(width, height) else Size(height, width)
    }

    fun viewSizeGet(): Size {
        return Size(getImageView().width, getImageView().height)
    }

    /**
     * Decodes from given Uri size of image and orientation. Returns true when image is vertical.
     */
    fun isImgVerticalExif(uri: Uri): Boolean {
        val exif = getExifData(uri)

        // This probably could have been simpler.

        val width: Int = exif.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, -1)
        val height: Int = exif.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, -1)
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
}