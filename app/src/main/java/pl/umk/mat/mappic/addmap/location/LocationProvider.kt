package pl.umk.mat.mappic.addmap.location

import android.Manifest
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.Priority
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.Task
import pl.umk.mat.mappic.addmap.features.permissions.PermissionManager
import pl.umk.mat.mappic.clist

/**
 * Provides location as single or continuous location updates.
 * Before requesting any location updates you need to call activityOnCreate()
 * which initializes some class's internal fields.
 * Then you can call:
 * getUserLocation() for single location updates
 * or startLocationUpdates() for continuous location delivery until
 * stopLocationUpdates() is called.
 * These methods do not return Location, instead you have to pass
 * interface (function/method that receives Location)
 * as an argument to them.
 * @param actv activity that has member 'permManager'
 * @param permManager PermissionManager that is member of activity
 */
class LocationProvider(private val actv: AppCompatActivity, private val permManager: PermissionManager) {

    private val locationTime: Long = 1500 // interval, in milliseconds
    private val locRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, locationTime)
        .setMinUpdateIntervalMillis(1000)
        .build()
    private lateinit var locationCallback: LocationCallback
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var locationTaskRunning = false

    // prevents infinite permission ask popups
    private var whoAsked1 = 0
    private var whoAsked2 = 0

    /**
     * ! Call it before requesting any location updates!
     */
    fun activityOnCreate() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(actv)
    }

    /**
     * Single location update
     * @param passLoc method that should be called to return location data
     */
    fun getUserLocation(passLoc: PassLocation) {
        // single time location delivery
        getLocOnce(passLoc)
    }

    /**
     * Used in getUserLocation
     */
    private fun getLocOnce(passLoc: PassLocation) {
        // check permissions first
        // Kotlin / Android Studio forces this and I cannot use my wrapper "PermissionManager" for checking permissions >:(
        if (ActivityCompat.checkSelfPermission(
                actv,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                actv,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // If there's no permissions, ask user and retry
            whoAsked1++
            if(whoAsked1 < 2)
                permManager.grantGpsPerm { getLocOnce(passLoc) }
            else
                whoAsked1 = 0
            return
        }

        // get location
        val locationTask1 = fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
        locationTask1.addOnSuccessListener { location ->
            // do stuff with this data
            passLoc.pass(location)
        }
    }

    /**
     * Start continuous location updates
     * @param passLoc method that should be called to return location data
     */
    fun startLocationUpdates(passLoc: PassLocation) {

        // define location callback
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationRes: LocationResult) {
                // Pass last retrieved location to interface
                Log.d(clist.LocationProvider, ">>> getting last location")
                val lastLoc = locationRes.lastLocation
                lastLoc ?: return
                passLoc.pass(lastLoc)
            }
        }

        // check permissions
        if (ActivityCompat.checkSelfPermission(
                actv,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                actv,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // If there's no permissions, ask user and retry
            whoAsked2++
            if (whoAsked2 < 2)
                permManager.grantGpsPerm { startLocationUpdates(passLoc) }
            else
                whoAsked2 = 0
            return
        }

        // request location updates
        fusedLocationProviderClient.requestLocationUpdates(locRequest, locationCallback, Looper.getMainLooper())
        locationTaskRunning = true
    }

    /**
     * Stop continuous location updates.
     * Call it in activity onPause() or when it's not needed.
     */
    fun stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        locationTaskRunning = false
    }

    /**
     * UNUSED YET
     * Should be used to check if location setting is enabled
     */
    private fun checkLocSettings() {
        val settRequest = LocationSettingsRequest.Builder()
            .addLocationRequest(locRequest)
        val client: SettingsClient = LocationServices.getSettingsClient(actv)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(settRequest.build())
        task.addOnSuccessListener { locationSettingsResponse ->
            // ok, can request location
            // TODO settings ok, now do something
        }
        task.addOnFailureListener { exception ->
            if(exception is ResolvableApiException) {
                // https://developer.android.com/develop/sensors-and-location/location/change-location-settings
                try {
                    // TODO shouw user dialog to enable location setting
                }
                catch (exc2: IntentSender.SendIntentException) {
                    // ignore error
                    Log.w(clist.LocationProvider, ">>> Caught SendIntentException!")
                }
            }
        }
    }

}