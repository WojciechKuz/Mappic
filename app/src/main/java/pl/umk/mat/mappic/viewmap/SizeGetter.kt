package pl.umk.mat.mappic.viewmap

import android.content.Context
import android.net.Uri
import android.util.Log
import android.util.Size
import android.widget.ImageView
import androidx.exifinterface.media.ExifInterface

open class SizeGetter(val context: Context, val TAG: String = "SizeGetter") {

    /** Gets Exif Interface for obtaining information about image from image Uri */
    fun getExifData(uri: Uri): ExifInterface {
        val istream = context.contentResolver.openInputStream(uri)
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

    /** get size of original image */
    fun origImgSizeGet(imageUri: Uri): Size {
        val exif = getExifData(imageUri)
        val width: Int = exif.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, -1)
        val height: Int = exif.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, -1)
        return if(!isImgVerticalExif(imageUri)) Size(width, height) else Size(height, width)
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

    companion object {
        /** Create size object for view size */
        fun viewSizeGet(imgView: ImageView): Size {
            return Size(imgView.width, imgView.height)
        }

        /**
         * Decodes ExifInterface orientation codes into human-readable degrees which tell how much is image rotated clockwise.
         */
        fun checkOrientation(orientation: Int): Int {
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
    }
}