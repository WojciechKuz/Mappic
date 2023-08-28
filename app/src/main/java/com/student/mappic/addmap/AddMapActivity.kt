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
import android.view.MotionEvent
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.student.mappic.clist


class AddMapActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityAddMapBinding
    lateinit var permManager: PermissionManager // handles permissions

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityAddMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        permManager = PermissionManager(this)
        val navController = findNavController(R.id.nav_host_fragment_2) // ref na miejsce na fragment   ??? Po czasie widzę ten komentarz i nie wiem co znaczy...
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration) // toolbar u góry z nazwą elementu
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_2)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    /*
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        // here my code
        if (event != null) {
            Log.d(clist.AddMapActivity, ">>> Addmap wykrył klik w " + "x: " + event.x + "; y: " + event.y)
        }
        else {
            Log.e(clist.AddMapActivity, ">>> Eeee, Addmap MotionEvent jest null")
        }
        return super.onTouchEvent(event)
    }
    */
}