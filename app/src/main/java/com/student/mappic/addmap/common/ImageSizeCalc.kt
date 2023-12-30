package com.student.mappic.addmap.common

import android.util.Size
import android.graphics.Point
import android.graphics.PointF
import android.util.Log
import com.student.mappic.clist

/**
 * This class solves this problem:
 * If we have original image, ex. 1200x1600 (3:4),
 * and we display it scaled down proportionally to fit in ImageView,
 * ex. 800x600 (yes, 4:3), how much space will image take in ImageView?
 * fit() method calculates just that.
 *
 * PS. Answer for example is 450x600 (3:4).
 */
class ImageSizeCalc(val original: Size, val view: Size) {

    init {
        Log.d(clist.ImageSizeCalc, ">>> Original size: ${original}, View size: ${view}. Image inview size is yet to be known.")
    }

    // has to be calculated
    var imgInView: Size? = null

    /**
     * Calculates actual size of image in ImageView (It Excludes black background of ImageView).
     */
    fun fit(): Size {
        val scaleW = view.width.toDouble() / original.width.toDouble()
        val scaleH = view.height.toDouble() / original.height.toDouble()
        val smallestScale = if(scaleW < scaleH) scaleW else scaleH
        val inView = Size(
            (original.width * smallestScale).toInt(),
            (original.height * smallestScale).toInt()
        )
        imgInView = inView
        return inView
    }

    /**
     * Translates coordinates relative to ImageView to coordinates relative to
     * actual space occupied by the image
     * @returns point position relative to imgInView
     */
    fun pointInImg(pointInView: Point): Point {
        if(imgInView == null) {
            fit()
        }
        Log.d(clist.ImageSizeCalc, ">>> Point inview: ${pointInView}")
        val emptySp = Size(
            (view.width - imgInView!!.width) / 2, // !! = not null asserted. - I'm sure it's non-null here. It's the purpose of this operator.
            (view.height - imgInView!!.height) / 2 // About !!: https://discuss.kotlinlang.org/t/purpose-of-double-exclamation-operator-null-check/8735
        )
        Log.d(clist.ImageSizeCalc, ">>> Original size: ${original}, View size: ${view}. Image inview size: ")
        Log.d(clist.ImageSizeCalc, ">>> empty space (1 of 2): ${emptySp}")
        val pointOnImgInImgview = Point(
            pointInView.x - emptySp.width, // x
            pointInView.y - emptySp.height // y
        )
        Log.d(clist.ImageSizeCalc, ">>> Point on image inview: ${pointOnImgInImgview}")
        return pointOnImgInImgview
    }

    /**
     * Method for checking if point is in Image displayed in ImageView.
     * @returns true - if it fits within Image
     * @returns false - if isn't in Image
     */
    fun isPointInBounds(pointInImgInView: Point): Boolean {
        // position values can be from 0 to width - 1 or from 0 to height - 1
        if(imgInView == null) {
            fit()
        }
        if(pointInImgInView.x < 0 || pointInImgInView.x > imgInView!!.width)
            return false
        if(pointInImgInView.y < 0 || pointInImgInView.y > imgInView!!.height)
            return false
        return true
    }

    /**
     * @returns screen proportion as width/height
     */
    fun imgProportions(): Double {
        if(imgInView == null) {
            fit()
        }
        return (imgInView!!.width * 1.0) / (imgInView!!.height * 1.0)
    }
    companion object {
        val PROP16TO9: Double = 16.0/9.0
        val PROP4TO3: Double  =  4.0/3.0
        val PROP9TO16: Double = 9.0/16.0
        val PROP3TO4: Double  = 3.0/4.0

        /**
         * Translate from pixel coordinate system to OpenGL coordinate system
         * gl_x := ( ((2*px_x) - width) / height )
         * gl_y := -( ((2*px_y) / height) - 1 )     // !!! minus!
         * @param viewSize in pixels
         * @param pixelCoordinates pixel coordinates
         * @return OpenGL coordinates, values in about [-1, 1] range depending on screen proportions
         */
        fun toOpenGLCoordinates(viewSize: Size, pixelCoordinates: PointF): PointF {
            // TODO check if in image bounds
            //val exif = utility.getExifData(viewModel.mapImg)
            //val imageSize = Size(exif.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, -1), exif.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, -1))
            return PointF((2f*pixelCoordinates.x - 1f*viewSize.width)/ viewSize.height, -2f*pixelCoordinates.y/viewSize.height + 1f)
        }
    }
}
