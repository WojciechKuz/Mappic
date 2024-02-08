package pl.umk.mat.mappic.viewmap

import android.content.ContentUris
import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.util.Size
import android.view.View
import androidx.exifinterface.media.ExifInterface
import java.io.File

/** If log debug messages */
private const val iflog = false

open class SizeGetter(val context: Context, val TAG: String = "SizeGetter") {

    /** Gets Exif Interface for obtaining information about image from image Uri */
    fun getExifData(uri: Uri): ExifInterface {
        val istream = context.contentResolver.openInputStream(uri)
            ?: throw Exception("Input stream 'istream' is null!") // do it when istream is null
        val exif = ExifInterface(istream)
        if(exif.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, -1) == -1)
            Log.e(TAG, ">>> Width tag in image exif data not found. cannot display it correctly.")
        if(exif.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, -1) == -1)
            Log.e(TAG, ">>> Height tag in image exif data not found. cannot display it correctly.")
        istream.close()
        return exif
    }

    /** get size of original image */
    fun origImgSizeGet(imageUri: Uri): Size? {
        val exif = getExifData(imageUri)
        val width: Int = exif.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, -1)
        val height: Int = exif.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, -1)

        // Was checking if == -1, to filter out incorrectly read values, but I included case if == 0 (when image has 0, 0 exif values)
        if(width <= 0 || height <= 0) {
            if(iflog) Log.d(TAG, ">>> Can't get size of original image based on Exif data. Using BitmapFactory.Options")
            return getBitmapSize(imageUri)
        }
        return if(!isImgVerticalExif(imageUri)) Size(width, height) else Size(height, width)
    }

    private fun getBitmapSize(imageUri: Uri): Size? {
        val bitMapOptions = BitmapFactory.Options()
        bitMapOptions.inJustDecodeBounds = true // prevent loading whole image to memory, just check size
        val path = imageUri.path
        if(path != null) {
            if (path.startsWith("content://") || path.startsWith("/picker/")) {
                val inputStream = context.contentResolver.openInputStream(imageUri)
                ContentUris.parseId(imageUri)
                BitmapFactory.decodeStream(inputStream)
                if(iflog) Log.i(TAG, ">>> Stream decoded successfully, ${imageUri}")
            } else {
                BitmapFactory.decodeFile(File(path).absolutePath, bitMapOptions)
                if(iflog) Log.i(TAG, ">>> File decoded successfully")
            }
        } else {
            Log.e(TAG, ">>> Can't get size of original image using BitmapFactory.Options. Returning null, try to use view size.")
            return null
        }
        val size = Size(bitMapOptions.outWidth, bitMapOptions.outHeight)
        if(iflog) Log.i(TAG, ">>> Got image size Size(${size.width}, ${size.height})")
        return if(size.width > 0 && size.height > 0)
            size
        else {
            Log.e(TAG, ">>> Can't get size of original image using BitmapFactory.Options. Returning null, try to use view size.")
            null
        }
    }

    /**
     * Decodes from given Uri size of image and orientation. Returns true when image is vertical.
     */
    fun isImgVerticalExif(uri: Uri): Boolean {
        val exif = getExifData(uri)

        val width: Int = exif.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, -1)
        val height: Int = exif.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, -1)
        val orientation: Int = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1)

        if(iflog) Log.d(TAG, ">>> Size of image: h: ${height}, w: ${width}, o: ${orientation}")
        val vertOrient = intArrayOf(90, 270)
        vertOrient.forEach {
            if(it == checkOrientation(orientation)) {
                if(iflog) Log.d(TAG, ">>> Rotation - image is vertical.")
                return true
            }
        }
        if(checkOrientation(orientation) == -1) {
            Log.w(TAG, ">>> Rotation tag in image exif data not found. cannot display it correctly.")

        }
        else
            if(iflog) Log.d(TAG, ">>> Rotation - image is horizontal.")
        return false
    }

    companion object {
        /** Create size object for view size */
        fun viewSizeGet(imgView: View): Size {
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