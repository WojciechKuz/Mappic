package pl.umk.mat.mappic.common

import android.util.Size
import android.graphics.Point
import android.graphics.PointF
import android.util.Log
import pl.umk.mat.mappic.clist
import kotlin.math.round

/**
 * This class solves this problem:
 * If we have original image, ex. 1200x1600 (3:4),
 * and we display it scaled down proportionally to fit in ImageView,
 * ex. 800x600 (yes, 4:3), how much space will image take in ImageView?
 * getScaledSize() method calculates just that.
 *
 * PS. Answer for example is 450x600 (3:4).
 */
class ImageSizeCalc(val original: Size, val view: Size) {

    // calculated in init
    val scaled: Size

    init {
        val scaleW = view.width.toDouble() / original.width.toDouble()
        val scaleH = view.height.toDouble() / original.height.toDouble()
        val smallestScale = if(scaleW < scaleH) scaleW else scaleH
        val inView = Size(
            (original.width * smallestScale).toInt(),
            (original.height * smallestScale).toInt()
        )
        scaled = inView
        Log.d(clist.ImageSizeCalc, ">>> Original size: ${original}, View size: ${view}, Scaled image size: ${scaled}.")
    }


    /**
     * Method for checking if point is in Image displayed in ImageView.
     * @returns true - if it fits within Image
     * @returns false - if isn't in Image
     */
    private fun isScaledPointInBounds(pointInScaled: PointF): Boolean {
        // position values can be from 0 to width - 1 or from 0 to height - 1
        if(pointInScaled.x < 0 || pointInScaled.x >= scaled.width)
            return false
        if(pointInScaled.y < 0 || pointInScaled.y >= scaled.height)
            return false
        return true
    }

    /** get size of one empty space around image. */
    private fun oneEmpty(): Size {
        // Uses view Size and imgScaled Size
        return Size(
            (view.width - scaled.width) / 2,
            (view.height - scaled.height) / 2
        )
    }
    /**
     * Method for checking if point is in Image displayed in ImageView.
     * @returns true - if it fits within Image
     * @returns false - if isn't in Image, point marked on black empty area.
     */
    fun isPointInBounds(inViewPoint: PointF): Boolean {
        val emptySp = oneEmpty()
        return isScaledPointInBounds( PointF( // creating inScaledPoint
            inViewPoint.x - emptySp.width,
            inViewPoint.y - emptySp.height
        ))
    }

    /**
     * Convert Point inView position to
     * coordinates of original image.
     * This are the coordinates that will be saved.
     */
    fun toOriginalPoint(inViewPoint: PointF): Point {
        val empty = oneEmpty()
        return Point(
            round((inViewPoint.x - empty.width) * original.width / scaled.width).toInt(),
            round((inViewPoint.y - empty.height) * original.height / scaled.height).toInt()
        )
    }

    /**
     * Reverse to toOriginalPoint().
     * Calculate inView coordinates from original coordinates.
     */
    fun toViewPoint(originalPoint: Point): PointF {
        val empty = oneEmpty()
        return PointF(
            originalPoint.x * 1f * scaled.width / original.width + empty.width,
            originalPoint.y * 1f * scaled.height / original.height + empty.height
        )
    }


    /**
     * @returns screen proportion as width/height
     */
    fun imgProportions(): Double {
        return (scaled.width * 1.0) / (scaled.height * 1.0)
    }
    companion object {
        const val PROP16TO9: Double = 16.0/9.0
        const val PROP4TO3: Double  =  4.0/3.0
        const val PROP9TO16: Double = 9.0/16.0
        const val PROP3TO4: Double  =  3.0/4.0

        /**
         * Translate coordinates from inView coordinates to OpenGL coordinates.
         *  This is what it calculates:
         *  - smll := min(width, height)
         *  - gl_x :=  ((2*px_x - width) / smll)
         *  - gl_y := -((2*px_y - height)/ smll)  // !!! minus!
         * @return OpenGL coordinates, gl_x, gl_y values in about [[-1, 1]] range depending on screen proportions
         */
        fun toOpenGLPoint(viewSize: Size, inViewPoint: PointF): PointF {
            /*
             OpenGL Coordinate limits for view proportions:
             For 1:1 view: x [-1, 1]; y [-1, 1];
             For 2:1 view: x [-1.5, 1.5]; y [-1, 1];
             For 1:2 view: x [-1, 1]; y [-1.5, 1.5];
             */
            val smallerSize = if(viewSize.width < viewSize.height) viewSize.width else viewSize.height
            return PointF(
                (2f*inViewPoint.x - 1f*viewSize.width) / smallerSize,
                -((2f*inViewPoint.y - 1f*viewSize.height) / smallerSize) // minus must be last operation
            )
        }

    }
}
