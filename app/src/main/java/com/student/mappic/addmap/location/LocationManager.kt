package com.student.mappic.addmap.location

import android.Manifest
import android.content.IntentSender
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.Priority
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.Task
import com.student.mappic.addmap.features.permissions.PermissionManager

/**
 * Provides location as single or continuous updates.
 * Before requesting any location updates you need to call activityOnCreate()
 * which initializes some class's internal fields.
 * Then you can call:
 * getUserLocation() for single location updates
 * or startLocationUpdates() for continous location delivery until
 * stopLocationUpdates() is called.
 */
class LocationManager(private val actv: AppCompatActivity) {

    // adjust times to provide location only once
    private val locationTime: Long = 1500 // interval, in milliseconds
    private val locRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, locationTime)
        //.setMaxUpdates(1) // single location request
        .setMinUpdateIntervalMillis(1000)
        .build()

    /**
     * Single location update
     * @param passLoc method that should be called to return location data
     */
    fun getUserLocation(passLoc: PassLocation) {
        // single time location delivery
        getLocOnce(passLoc)
    }

    /**
     * Start continuous location updates
     * @param passLoc method that should be called to return location data
     */
    fun startLocationUpdates(passLoc: PassLocation) {
        // TODO start
    }
    fun stopLocationUpdates() {
        // TODO stop
    }

    private fun checkLocSettings() {
        val settRequest = LocationSettingsRequest.Builder()
            .addLocationRequest(locRequest)
        val client: SettingsClient = LocationServices.getSettingsClient(actv)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(settRequest.build())
        task.addOnSuccessListener { locationSettingsResponse ->
            // ok, can request location
            // TODO get location
        }
        task.addOnFailureListener { exception ->
            if(exception is ResolvableApiException) {
                // https://developer.android.com/develop/sensors-and-location/location/change-location-settings
                try {
                    // TODO shouw user dialog to enable location setting
                }
                catch (exc2: IntentSender.SendIntentException) {
                    // ignore error
                }
            }
        }
    }

    /*private lateinit var locationCallback: LocationCallback
    fun locCallbInit() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locRes: LocationResult) {
                super.onLocationResult(locRes)
            }
        }
    }
    fun locReq() {
        //
        //if(requestingLocationUpdates) startLocationUpdates()
    }
    fun startLocationUpdates() {
        //val fusedLocationClient.
        fusedLocationClient.requestLocationUpdates(locRequest, locationCallback, Looper.getMainLooper())
    }
    */

    // OD NOWA
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    /**
     * ! Call it before requesting any location updates!
     */
    fun activityOnCreate() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(actv)
    }

    private fun getLocOnce(passLoc: PassLocation) {
        // check permissions first
        // Kotlin / Android Studio forces this and I cannot use my wrapper "PermissionManager" for checking permissions
        if (ActivityCompat.checkSelfPermission(
                actv,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                actv,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // If there's no permissions, ask user and retry
            PermissionManager(actv).grantGpsPerm { getLocOnce(passLoc) }
            return
        }

        // get location
        val locationTask = fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
        locationTask.addOnSuccessListener { location ->
            // do stuff with this data
            passLoc.pass(location)
        }
    }

}