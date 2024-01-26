package pl.umk.mat.mappic

import android.app.Activity
import com.google.android.material.snackbar.Snackbar

/**
 * Utility to quickly create snackbars
 */
class SnackShow(actv: Activity, val id: Int, val text: String) {
    init {
        Snackbar.make(actv.findViewById(id), text, Snackbar.LENGTH_LONG).show()
    }
}