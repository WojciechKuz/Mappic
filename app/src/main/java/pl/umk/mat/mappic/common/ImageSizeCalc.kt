package pl.umk.mat.mappic.common

import android.util.Size
import android.graphics.Point
import android.graphics.PointF
import android.util.Log
import pl.umk.mat.mappic.clist

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

    /* // CODE GRAVEYARD
    /**
     * Translates coordinates relative to ImageView to coordinates relative to
     * actual space occupied by the image
     * @returns point position relative to scaledImg
     */
    fun pointInScaled(pointInView: Point): Point {
        Log.d(clist.ImageSizeCalc, ">>> Point inview: ${pointInView}")
        val emptySp = Size(
            (view.width - scaled.width) / 2, // !! = not null asserted. - I'm sure it's non-null here. It's the purpose of this operator.
            (view.height - scaled.height) / 2 // About !!: https://discuss.kotlinlang.org/t/purpose-of-double-exclamation-operator-null-check/8735
        )
        Log.d(clist.ImageSizeCalc, ">>> Original size: ${original}, View size: ${view}. Image inview size: ")
        Log.d(clist.ImageSizeCalc, ">>> empty space (1 of 2): ${emptySp}")
        val pointOnScaledImg = Point(
            pointInView.x - emptySp.width, // x
            pointInView.y - emptySp.height // y
        )
        Log.d(clist.ImageSizeCalc, ">>> Point on image inview: ${pointOnScaledImg}")
        return pointOnScaledImg
    }

    /** This is REVERSE function to [pointInScaled()]. */
    fun pointInView(pointOnImgScaled: Point): Point {
        val emptySp = Size(
            (view.width - scaled.width) / 2, // !! = not null asserted. - I'm sure it's non-null here. It's the purpose of this operator.
            (view.height - scaled.height) / 2 // About !!: https://discuss.kotlinlang.org/t/purpose-of-double-exclamation-operator-null-check/8735
        )
        val pointInView = Point(
            pointOnImgScaled.x + emptySp.width, // x
            pointOnImgScaled.y + emptySp.height // y
        )
        return pointInView
    }*/

    /**
     * Method for checking if point is in Image displayed in ImageView.
     * @returns true - if it fits within Image
     * @returns false - if isn't in Image
     */
    private fun isScaledPointInBounds(pointInScaled: Point): Boolean {
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
    fun isPointInBounds(inViewPoint: Point): Boolean {
        val emptySp = oneEmpty()
        return isScaledPointInBounds( Point( // creating inScaledPoint
            inViewPoint.x - emptySp.width,
            inViewPoint.y - emptySp.height
        ))
    }

    /**
     * Convert Point inView position to
     * coordinates of original image.
     * This are the coordinates that will be saved.
     */
    fun toOriginalPoint(inViewPoint: Point): Point {
        val empty = oneEmpty()
        return Point(
            (inViewPoint.x - empty.width) * original.width / scaled.width,
            (inViewPoint.y - empty.height) * original.height / scaled.height
        )
    }

    /**
     * Reverse to toOriginalPoint().
     * Calculate inView coordinates from original coordinates.
     */
    fun toViewPoint(originalPoint: Point): Point {
        val empty = oneEmpty()
        return Point(
            originalPoint.x * scaled.width / original.width + empty.width,
            originalPoint.y * scaled.height / original.height + empty.height
        )
    }


    /**
     * @returns screen proportion as width/height
     */
    fun imgProportions(): Double {
        return (scaled.width * 1.0) / (scaled.height * 1.0)
    }
    companion object {
        val PROP16TO9: Double = 16.0/9.0
        val PROP4TO3: Double  =  4.0/3.0
        val PROP9TO16: Double = 9.0/16.0
        val PROP3TO4: Double  =  3.0/4.0

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
                (-2f*inViewPoint.y - 1f*viewSize.height) / smallerSize // minus can be last or first operation
            )
            /* // if coordinates were always in [-1, 1] range:
            return PointF(
                inViewPoint.x * 2f / viewSize.width - 1f,
                inViewPoint.y * -2f / viewSize.height - 1f
            )
            */
        }

        /* // REMOVED CODE
        /**
         * Translate from pixel coordinate system to OpenGL coordinate system
         * gl_x := ( ((2*px_x) - width) / height )
         * gl_y := -( ((2*px_y) / height) - 1 )     // !!! minus!
         * @param viewSize in pixels
         * @param inviewPoint pixel coordinates
         * @return OpenGL coordinates, values in about [-1, 1] range depending on screen proportions
         */
        //@Deprecated("This function does not support views with height greater than width, use toOpenGLPoint() instead.")
        fun toOpenGLCoordinates(viewSize: Size, inviewPoint: PointF): PointF {
            // futureTODO check if in image bounds
            //val exif = utility.getExifData(viewModel.mapImg)
            //val imageSize = Size(exif.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, -1), exif.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, -1))
            return PointF(
                (2f*inviewPoint.x - 1f*viewSize.width)/ viewSize.height,
                -2f*inviewPoint.y/viewSize.height + 1f
            )
        }
        */
    }
}
