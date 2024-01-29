package pl.umk.mat.mappic.addmap.shared_step2and3

import android.util.Size
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import pl.umk.mat.mappic.R
import pl.umk.mat.mappic.addmap.AddMapActivity
import pl.umk.mat.mappic.common.myview.MyView
import pl.umk.mat.mappic.databinding.FragmentStep2Binding
import pl.umk.mat.mappic.databinding.FragmentStep3Binding
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
    private val whichFragment = if(TAG.contains("2")) 2
    else if(TAG.contains("3")) 3
    else throw Exception("Tag does not contain accepted step number of 2 or 3")

    var binding2: FragmentStep2Binding? = null
    var binding3: FragmentStep3Binding? = null

    // UI getting methods shortened
    fun latitudeNS(): EditText? {
        return addMap.findViewById<EditText>(R.id.gpsLatitude)
    }
    fun longitudeEW(): EditText? {
        return addMap.findViewById<EditText>(R.id.gpsLongitude)
    }
    fun bindingLatitudeNS(): EditText? {
        return when(whichFragment) {
            2 -> binding2?.gpsLatitude
            3 -> binding3?.gpsLatitude
            else -> null
        }
    }
    fun bindingLongitudeEW(): EditText? {
        return when(whichFragment) {
            2 -> binding2?.gpsLongitude
            3 -> binding3?.gpsLongitude
            else -> null
        }
    }
    fun getBindingOpenGLView(): MapGLSurfaceView {
        return when(whichFragment) {
            2 -> binding2?.openGLView!!
            3 -> binding3?.openGLView!!
            else -> throw Exception("Can't get MapGLSurfaceView")
        }
    }

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