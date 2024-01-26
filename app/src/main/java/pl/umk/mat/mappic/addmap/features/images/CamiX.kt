package pl.umk.mat.mappic.addmap.features.images

import android.content.ContentValues
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import pl.umk.mat.mappic.clist
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.Executors

/**
 * Class that handles camera to separate camera code from UI related code in activity.
 * You have to handle permissions in your activity (or fragment) though.
 * @param act activity class
 * @param previewId id of CameraX PreviewView where camera preview will be shown
 * @param appImgFolder name of folder in Pictures directory that photos will be saved in.
 */
class CamiX(var act: AppCompatActivity, private val previewId: Int, private val appImgFolder: String) {
    private var imageCapture: ImageCapture? = null
    private var cameraExecutor = Executors.newSingleThreadExecutor()

    /**
     * Init preview & allow imgCapture.
     * You have to implicitly start preview with this method
     * after getting permissions. ! Remember to call closeCam()
     * in onDestroy() in your activity.
     */
    fun startCamera() {
        Log.d(TAG, ">>> startCamera()")
        val cameraProviderFuture = ProcessCameraProvider.getInstance(act)

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    val camView = act.findViewById<PreviewView>(previewId)// FINDVIEWBYID instead of viewBinding
                    it.setSurfaceProvider(camView.surfaceProvider) // viewBinding.camPreview
                }

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera (preview, imgCapture)
                cameraProvider.bindToLifecycle(
                    act, cameraSelector, preview, imageCapture)


            } catch(exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(act))

        // this is code for image capture
        imageCapture = ImageCapture.Builder().build()
    }

    /**
     * Name of this method may not suggest it, but
     * this method takes photo.
     * It saves images to MediaStore and passes Uri to interface provided as arg.
     */
    fun takePhoto(receiver: PassUri) {
        Log.d(TAG, ">>> takePhoto()")
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        // Create time stamped name and MediaStore entry.
        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.GERMANY)
            .format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/${appImgFolder}")
            }
        }

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(act.contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues)
            .build()

        // Set up image capture listener, which is triggered after photo has
        // been taken
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(act),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    val msg = "Photo capture failed: ${exc.message}"
                    Toast.makeText(act.baseContext, msg, Toast.LENGTH_SHORT).show()
                    Log.e(TAG, msg, exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults){
                    Log.d(TAG, ">>> DBImage saved successfully.")
                    receiver.receiveUri(output.savedUri) // sends Uri to specified interface
                }
            }
        )
    }

    /**
     * Use it in onDestroy() in your activity.
     */
    fun closeCamera() {
        cameraExecutor.shutdown()
    }

    companion object {
        private const val TAG = clist.CamiX
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
    }
}