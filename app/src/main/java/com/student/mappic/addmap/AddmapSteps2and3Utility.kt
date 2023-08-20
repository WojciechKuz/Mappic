package com.student.mappic.addmap

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import androidx.exifinterface.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.TextView
import com.student.mappic.R
import androidx.annotation.RequiresApi
import com.student.mappic.clist

/**
 * Fragments [Step2Fragment] and [Step3Fragment] are nearly identical.
 * It serves methods used by both.
 *
 * This class was created to prevent code duplication.
 */
class AddmapSteps2and3Utility(val addMap: AddMapActivity, val TAG: String) {
    /**
     * Decodes from given Uri size of image and orientation. Returns true when image is vertical.
     */
    fun isImgVerticalExif(uri: Uri): Boolean {
        val istream = addMap.contentResolver.openInputStream(uri)
            ?: throw Exception("Input stream 'istream' is null!") // do it when istream is null
        Log.d(TAG, ">>> Decoding size of image")
        val exif = ExifInterface(istream)
        val height: Int = exif.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, -1)
        val width: Int = exif.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, -1)
        val orientation: Int = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1)
        istream.close()
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
                Log.d(TAG, ">>> Image rotated by ${orient.value} deg clockwise.")
                return orient.value
            }
        }
        return -1
    }

    // TODO member fun checking if typed coordinates are correct (and if point is marked correctly)

    /**
     * Displays error message in given TextView and of given Error type.
     */
    fun displayErrMsg(textView: TextView, err: ErrTypes) {
        val array: Array<String> = addMap.resources.getStringArray(R.array.errTypesMessages)
        textView.text = array[err.code]
        textView.visibility = TextView.VISIBLE
    }
}