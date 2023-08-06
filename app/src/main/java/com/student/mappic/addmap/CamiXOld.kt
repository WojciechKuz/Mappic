package com.student.mappic.addmap

import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.common.util.concurrent.ListenableFuture
import com.student.mappic.R
import com.student.mappic.clist

/**
 * CamiXOld is a class for accessing the camera.
 * It's only usage in app is in Step1Fragment,
 * where it is used to take picture of a map by the user.
 *
 * It has weird name cuz it's cool.
 */
class CamiXOld(private var addMap: AddMapActivity){
    private var cameraProviderFuture : ListenableFuture<ProcessCameraProvider> =
        ProcessCameraProvider.getInstance(addMap)

    /**
     * Initialize classes associated with preview of the camera.
     * I was unable to do this in Step1Fragment.kt because of lack of context
     * (context (getContext()) returns 'Context?'. Could be null!)
     */
    init {
        Log.d(clist.CamiXOld, ">>> init CamiXOld... permissions & stuff.")
        if (allPermissionsGranted()) {
            Log.d(clist.CamiXOld, ">>> permissions granted!")
            setupPreview()
        } else {
            Log.d(clist.CamiXOld, ">>> requesting permissions...")
            requestPermissions()
        }
        Log.d(clist.CamiXOld, ">>> what next?")
    }
    private fun setupPreview() {
        cameraProviderFuture.addListener( {
            val cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider)
        },
            ContextCompat.getMainExecutor(addMap)
        )
    }

    /**
     * Provide preview
     */
    private fun bindPreview(cameraProvider : ProcessCameraProvider) {
        var preview : Preview = Preview.Builder().build()

        var cameraSelector : CameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()
        var camView = addMap.findViewById<PreviewView>(R.id.camView) // WARNING: R.id.camView can be inaccessible after build!
        // how to solve if it fails ^: In AddMapActivity public add member fun,which returns reference to camView
        preview.setSurfaceProvider(camView.surfaceProvider)

        var camera = cameraProvider.bindToLifecycle(addMap as LifecycleOwner, cameraSelector, preview)
    }

    //fun requestPerm() {
        //
    //}

    /**
     * Take a pic
     */
    public fun takePhoto() {
        //TODO
    }

    /*
     * For me: init keyword is code executed in "primary constructor" (the default one)*.
     * Classes can have "secondary constructors" (other ones).
     *
     * * - wasn't meaning term "default constructor".
     */
    private val activityResultLauncher =
        addMap.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                permissions ->
            // Handle Permission granted/rejected
            var permissionGranted = true
            permissions.entries.forEach {
                if (it.key in REQUIRED_PERMISSIONS && it.value == false)
                    permissionGranted = false
            }
            if (!permissionGranted) {
                Toast.makeText(addMap.baseContext,
                    "Permission request denied",
                    Toast.LENGTH_SHORT).show()
            } else {
                //startCamera()
            }
        }

    // Permission handling classes
    private fun requestPermissions() {
        activityResultLauncher.launch(REQUIRED_PERMISSIONS)
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(addMap.baseContext, it) == PackageManager.PERMISSION_GRANTED
    }



    // android.Manifest includes .permission
    companion object {
        private const val TAG = "Mappic"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private val REQUIRED_PERMISSIONS =
            mutableListOf (
                android.Manifest.permission.CAMERA
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }
}