package com.student.mappic.addmap


import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.student.mappic.R
import com.student.mappic.addmap.features.permissions.PermissionManager
import com.student.mappic.clist
import com.student.mappic.databinding.ActivityAddMapBinding
//import com.student.mappic.addmap.features.permissions.PermissionManager


class AddMapActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityAddMapBinding
    //lateinit var permManager: PermissionManager // handles permissions
    val permManager = PermissionManager(this) //LifeCycleOwners must register before they are created/resumed
    private var created = false

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityAddMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        //permManager = PermissionManager(this)
        val navController = findNavController(R.id.nav_host_fragment_2) // ref na miejsce na fragment   ??? Po czasie widzę ten komentarz i nie wiem co znaczy...
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration) // toolbar u góry z nazwą elementu
        created = true
    }

    fun getPermissionManager(): PermissionManager {
        if(!created)
            Log.w(clist.AddMapActivity, ">>> Activity not created yet !!!")
        //return PermissionManager(this)
        return permManager
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_2)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}