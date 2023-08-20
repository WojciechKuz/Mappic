package com.student.mappic.addmap

import android.net.Uri
import android.util.Log
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts

class PickPhoto(val addMap: AddMapActivity) {
    /**
     * Pick photo through system photo-picker.
     *
     * @param whenNotSelected - handle receiving uri of photo selected by user
     * @param whenNotSelected - passes null uri, don't use it's value. Use this interface only to trigger actions.
     */
    fun pickPhoto(whenSelected: PassUri, whenNotSelected: PassUri) {
        val pickMedia = addMap.registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: ${uri}")
                whenSelected.receiveUri(uri)
            } else {
                Log.d("PhotoPicker", "No media selected")
                whenNotSelected.receiveUri(null)
            }
        }

        // Launch the photo picker and let the user choose only images.
        pickMedia.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )

    }
}