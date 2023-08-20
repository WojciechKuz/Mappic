package com.student.mappic.addmap

import android.net.Uri
import android.util.Log
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.student.mappic.clist

/**
 * Utility class for requesting system photo-picker.
 */
class PickPhoto(val addMap: AddMapActivity) {
    /**
     * Pick photo through system photo-picker.
     *
     * @param whenNotSelected - handle receiving uri of photo selected by user
     * @param whenNotSelected - passes null uri, don't use it's value. Use this interface only to trigger actions.
     */
    fun pickPhoto(whenSelected: PassUri, whenNotSelected: PassUri) {
        val pickMedia = addMap.registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->

            // Callback is invoked after the user selects a media item or closes the photo picker.
            if (uri != null) {
                Log.d(clist.PickPhoto, ">>> Selected URI: ${uri}.")
                whenSelected.receiveUri(uri) // Hmmm... Compiler thinks uri could be null here...
            } else {
                Log.d(clist.PickPhoto, ">>> No media has been selected.")
                whenNotSelected.receiveUri(uri)
            }
        }

        // Launch the system photo picker and let the user choose images.
        pickMedia.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }
}