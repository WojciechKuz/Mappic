package com.student.mappic.addmap

import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.common.util.concurrent.ListenableFuture
import com.student.mappic.R

/**
 * CamiX is a class for accessing the camera.
 * It's only usage in app is in Step1Fragment,
 * where it is used to take picture of a map by the user.
 *
 * It has weird name cuz it's cool.
 */
class CamiX(private var addMap: AddMapActivity){
    private var cameraProviderFuture : ListenableFuture<ProcessCameraProvider> =
        ProcessCameraProvider.getInstance(addMap)

    /**
     * Initialize classes associated with preview of the camera.
     * I was unable to do this in Step1Fragment.kt because of lack of context
     * (context (getContext()) returns 'Context?'. Could be null!)
     */
    init {
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

    /**
     * Take a pic
     */
    public fun takePhoto() {
        //
    }

    /*
     * For me: init keyword is code executed in "primary constructor" (the default one)*.
     * Classes can have "secondary constructors" (other ones).
     *
     * * - wasn't meaning term "default constructor".
     */
}