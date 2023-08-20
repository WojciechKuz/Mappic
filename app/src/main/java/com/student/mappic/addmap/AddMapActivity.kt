package com.student.mappic.addmap


import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.student.mappic.R
import com.student.mappic.databinding.ActivityAddMapBinding
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.student.mappic.clist


class AddMapActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityAddMapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityAddMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_2) // ref na miejsce na fragment   ??? Po czasie widzę ten komentarz i nie wiem co znaczy...
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration) // toolbar u góry z nazwą elementu
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_2)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }



    // Under this comment is code, that grants permissions for Step1Fragment

    // make it priv val, instead of interface it should call method here that would then call interface 👍
    private var activityResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions())
        { permissions ->
            // Handle Permission granted/rejected
            Log.d(TAG, ">>> activityResultLauncher - init")
            var permissionGranted = true
            permissions.entries.forEach {
                if (it.key in REQUIRED_PERMISSIONS && it.value == false)
                    permissionGranted = false
            }
            if (!permissionGranted) {
                Toast.makeText(baseContext,
                    "Permission request denied",
                    Toast.LENGTH_SHORT).show()
            } else {
                startCam()
            }
        }

    private lateinit var camGranted: CamGranted

    /**
     * Initializes camera when all needed permissions are granted.
     */
    fun step1(camGranted: CamGranted) {
        this.camGranted = camGranted
        if (allPermissionsGranted()) {
            camGranted.startCamera()
        } else {
            requestPermissions()
        }
    }

    /**
     * Starts Camera (via CamGranted interface that is set in [Step1Fragment] ).
     */
    private fun startCam() {
        if(camGranted != null) {
            Log.d(clist.AddMapActivity, ">>> startCam() called")
        } else {
            Log.w(clist.AddMapActivity, ">>> startCam() called, and camGranted is null!")
        }
        camGranted.startCamera()
    }

    private fun requestPermissions() {
        Log.d(TAG, ">>> requestPermissions()")
        activityResultLauncher.launch(REQUIRED_PERMISSIONS)
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private const val TAG = clist.AddMapActivity
        private val REQUIRED_PERMISSIONS =
            mutableListOf (
                Manifest.permission.CAMERA/*,
                Manifest.permission.RECORD_AUDIO*/
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }
}