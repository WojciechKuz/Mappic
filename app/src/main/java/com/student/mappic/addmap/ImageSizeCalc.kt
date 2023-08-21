package com.student.mappic.addmap

import android.util.Size
import android.graphics.Point

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

    // has to be calculated
    var imgInView: Size? = null

    /**
     * Calculates actual size of image in ImageView (It Excludes black background of ImageView).
     */
    fun fit(): Size {
        val scaleW = view.width / original.width
        val scaleH = view.height / original.height
        val smallestScale = if(scaleW < scaleH) scaleW else scaleH
        val inView = Size(
            original.width * smallestScale,
            original.height * smallestScale
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
        val emptySp = Size(
            (view.width - imgInView.width) / 2,
            (view.height - imgInView.height) / 2
        )
        val pointOnImgInImgview = Point(
            pointInView.x - emptySp.width, // x
            pointInView.y - emptySp.height // y
        )
        return pointOnImgInView
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
        if(pointInImgInView.x < 0 || pointInImgInView.x > imgInView.width - 1)
            return false
        if(pointInImgInView.y < 0 || pointInImgInView.y > imgInView.height - 1)
            return false
        return true
    }
}
