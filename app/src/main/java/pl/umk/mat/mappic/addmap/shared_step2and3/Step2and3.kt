package pl.umk.mat.mappic.addmap.shared_step2and3

import android.util.Size
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import pl.umk.mat.mappic.R
import pl.umk.mat.mappic.addmap.AddMapActivity
import pl.umk.mat.mappic.common.myview.MyView
import pl.umk.mat.mappic.opengl.MapGLSurfaceView
import pl.umk.mat.mappic.viewmap.SizeGetter

/**
 * Methods in this class shorten getting UI elements in code. Works for [Step2Fragment] and [Step3Fragment]
 * Used in [AddmapSteps2and3Utility]
 *
 * THIS ARE INHERITED:
 * - getExifData()
 * - origImgSizeGet()
 * - isImgVerticalExif()
 * - checkOrientation()
 */
class Step2and3(val addMap: AddMapActivity, TAG: String): SizeGetter(addMap, TAG) {

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

    // THIS ARE INHERITED:
    // getExifData()
    // origImgSizeGet()
    // isImgVerticalExif()
    // checkOrientation()

    /**
     * Decodes ExifInterface orientation codes into human-readable degrees which tell how much is image rotated clockwise.
     */
    private fun checkOrientation(orientation: Int): Int {
        // this was protected fun in SizeGetter, but
        //  "Using protected members which are not @JvmStatic in the superclass companion is unsupported yet"
        return SizeGetter.checkOrientation(orientation)
    }

    fun viewSizeGet(): Size {
        return Size(getImageView().width, getImageView().height)
    }
}