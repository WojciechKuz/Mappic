package com.student.mappic.addmap.features.images

import android.net.Uri
import android.util.Log
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.student.mappic.addmap.AddMapActivity
import com.student.mappic.clist

/**
 * Utility class for requesting system photo-picker.
 */
class PickPhoto(addMap: AddMapActivity) {

    val pickMedia = addMap.registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        // Callback is invoked after the user selects a media item or closes the photo picker.
        if (uri != null) {
            photoPicked(uri)
        } else {
            photoNotPicked(uri)
        }
    }
    /**
     * Pick photo through system photo-picker.
     *
     * @param whenNotSelected - handle receiving uri of photo selected by user
     * @param whenNotSelected - passes null uri, don't use it's value. Use this interface only to trigger actions.
     */
    private lateinit var whenSelected: PassUri
    private lateinit var whenNotSelected: PassUri
    fun pickPhoto(whenSelected: PassUri, whenNotSelected: PassUri) {
        this.whenSelected = whenSelected
        this.whenNotSelected = whenNotSelected
        // Launch the system photo picker and let the user choose images.
        pickMedia.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }
    private fun photoPicked(uri: Uri) {
        Log.d(clist.PickPhoto, ">>> Selected URI: ${uri}.")
        whenSelected.receiveUri(uri)
    }
    private fun photoNotPicked(uri: Uri?) {
        Log.d(clist.PickPhoto, ">>> No media has been selected.")
        whenNotSelected.receiveUri(uri)
    }
}