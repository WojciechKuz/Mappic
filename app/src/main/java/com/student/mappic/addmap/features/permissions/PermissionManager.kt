package com.student.mappic.addmap.features.permissions

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.student.mappic.addmap.AddMapActivity
import com.student.mappic.clist

/**
 * Manager for handling permissions. My app uses camera, gps and write_storage.
 */
class PermissionManager(private val addMap: AddMapActivity) {

    // *Granted
    private lateinit var allGranted: PermissionsGranted
    private lateinit var camGranted: PermissionsGranted
    private lateinit var gpsGranted: PermissionsGranted

    // Set interface and request permissions methods:
    /**
     * Initializes camera when all needed permissions are granted.
     */
    fun grantCamPerm(camGranted: PermissionsGranted) { // Called from outside
        this.camGranted = camGranted
        if (camPermissionsGranted()) {
            camGranted.startAction() // When already has permissions: --> action()
        } else {
            requestCamPermissions() // Request permissions: --> Launcher.launch(PERM) --> if granted { granted() { action() } }
        }
    }
    /**
     * Reads user location when all needed permissions are granted.
     */
    fun grantGpsPerm(gpsGranted: PermissionsGranted) {
        this.gpsGranted = gpsGranted
        if (gpsPermissionsGranted()) {
            gpsGranted.startAction()
        } else {
            requestGpsPermissions()
        }
    }
    /**
     * Starts some action when all needed permissions are granted.
     */
    fun grantAllPerm(allGranted: PermissionsGranted) {
        this.allGranted = allGranted
        if (allPermissionsGranted()) {
            allGranted.startAction()
        } else {
            requestAllPermissions()
        }
    }

    // *permissionsGranted
    private fun allPermissionsGranted() = ALL_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            addMap.baseContext, it) == PackageManager.PERMISSION_GRANTED
    }
    private fun camPermissionsGranted() = CAM_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            addMap.baseContext, it) == PackageManager.PERMISSION_GRANTED
    }
    private fun gpsPermissionsGranted() = GPS_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            addMap.baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    // request*Permissions
    private fun requestAllPermissions() {
        Log.d(TAG, ">>> requestAllPermissions()")
        activityResultLauncher.launch(ALL_PERMISSIONS)
    }
    private fun requestCamPermissions() {
        Log.d(TAG, ">>> requestCamPermissions()")
        camLauncher.launch(CAM_PERMISSIONS)
    }
    private fun requestGpsPermissions() {
        Log.d(TAG, ">>> requestGpsPermissions()")
        gpsLauncher.launch(GPS_PERMISSIONS)
    }

    // resultLaunchers for granting permissions

    // make it priv val, instead of interface it should call method here that would then call interface 👍
    private var activityResultLauncher =
        addMap.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions())
        { permissions ->
            // Handle Permission granted/rejected
            Log.d(TAG, ">>> activityResultLauncher - init")
            var permissionGranted = true
            permissions.entries.forEach {
                if (it.key in ALL_PERMISSIONS && it.value == false)
                    permissionGranted = false
            }
            if (!permissionGranted) {
                Toast.makeText(addMap.baseContext,"Permission request denied", Toast.LENGTH_SHORT).show()
            } else {
                startAny()
            }
        }
    private var camLauncher =
        addMap.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions())
        { permissions ->
            // Handle Permission granted/rejected
            Log.d(TAG, ">>> camLauncher - init")
            var permissionGranted = true
            permissions.entries.forEach {
                if (it.key in CAM_PERMISSIONS && it.value == false)
                    permissionGranted = false
            }
            if (!permissionGranted) {
                Toast.makeText(addMap.baseContext,"Permission request denied", Toast.LENGTH_SHORT).show()
            } else {
                startCam()
            }
        }
    private var gpsLauncher =
        addMap.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions())
        { permissions ->
            // Handle Permission granted/rejected
            Log.d(TAG, ">>> gpsLauncher - init")
            var permissionGranted = true
            permissions.entries.forEach {
                if (it.key in GPS_PERMISSIONS && it.value == false)
                    permissionGranted = false
            }
            if (!permissionGranted) {
                Toast.makeText(addMap.baseContext,"Permission request denied", Toast.LENGTH_SHORT).show()
            } else {
                startGps()
            }
        }

    // start*
    /**
     * Start chosen method (after permissions were granted), that required all the permissions.
     */
    private fun startAny() {
        Log.d(clist.AddMapActivity, ">>> any.startAction() called after granting permissions")
        allGranted.startAction()
    }
    /**
     * Starts Camera (via PermissionsGranted interface that is set in [Step1Fragment] ).
     */
    private fun startCam() {
        Log.d(clist.AddMapActivity, ">>> cam.startAction() called after granting permissions")
        camGranted.startAction()
    }
    /**
     * Reads user location (via PermissionsGranted interface that is set in [Step2Fragment] or [Step3Fragment] ).
     */
    private fun startGps() {
        Log.d(clist.AddMapActivity, ">>> gps.startAction() called after granting permissions")
        gpsGranted.startAction()
    }

    companion object {
        private const val TAG = clist.PermissionManager

        // *_PERMISSIONS
        val ALL_PERMISSIONS =
            mutableListOf (
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
        val CAM_PERMISSIONS =
            mutableListOf (
                Manifest.permission.CAMERA
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
        private val GPS_PERMISSIONS =
            mutableListOf (
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ).toTypedArray()
    }
}