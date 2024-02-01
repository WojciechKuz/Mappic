package pl.umk.mat.mappic

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import com.google.android.material.floatingactionbutton.FloatingActionButton
import pl.umk.mat.mappic.addmap.AddMapActivity
import pl.umk.mat.mappic.databinding.ActivityMainBinding

/** Aka MapList Activity */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        // View ID names should be Activity-wide unique.
        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            Log.d(clist.MainActivity, ">>> Opening AddMapActivity.")
            openAddMapActivity()
        }
    }
    private fun openAddMapActivity() {
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
                SnackShow(
                    this,
                    R.id.fragment_list,
                    "No action available."
                )
                Log.d(clist.MainActivity, ">>> Clicked settings. no action available yet.")
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}