package pl.umk.mat.mappic.addmap.features.images

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import pl.umk.mat.mappic.addmap.AddMapActivity
import pl.umk.mat.mappic.clist

/**
 * Utility class for requesting system photo-picker.
 * @param persistent if uri should be granted forever.
 * Normally uris are short lived, and access to resource is blocked when app closes.
 * If uri is going to be stored in database and used after app restarts, then set this to true.
 */
class PickPhoto(val addMap: AddMapActivity, val persistent: Boolean = false) {

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
     * @param whenSelected - handle receiving uri of photo selected by user
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
        if (persistent) {
            addMap.contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        whenSelected.receiveUri(uri)
    }
    private fun photoNotPicked(uri: Uri?) {
        Log.d(clist.PickPhoto, ">>> No media has been selected.")
        whenNotSelected.receiveUri(uri)
    }
    /*
    fun persistentUri(conRes: ContentResolver, uri: Uri) {
        conRes.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
        // ContentUris
        // val code = Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
        //conRes.persistedUriPermissions
    }
    */
}