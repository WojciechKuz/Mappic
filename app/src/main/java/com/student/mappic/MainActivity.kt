package com.student.mappic

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.student.mappic.addmap.AddMapActivity
import com.student.mappic.databinding.ActivityMainBinding
import com.student.mappic.clist

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        // View ID names should be Activity-wide unique.
        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            SnackShow(this, R.id.fab, "Clicked +. No act.")
            Log.d(clist.MainActivity, ">>> Clicked add map. Action open AddMapActivity.")
            // TODO open AddeMapActivity
            openAddMapActivity()
        }
    }
    private fun openAddMapActivity() {
        /*
        HERE is something weird.
        Kotlin: Activity::class.java
        Java:   Activity.class
         */
        startActivity(Intent(this, AddMapActivity::class.java))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                SnackShow(this, R.id.fragment_list, "Clicked on settings. No act.")
                Log.d(clist.MainActivity, ">>> Clicked settings. no action available yet.")
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /*
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragment_list)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }*/
}