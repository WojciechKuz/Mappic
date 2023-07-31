package com.student.mappic.addmap


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.student.mappic.R
import com.student.mappic.databinding.ActivityAddMapBinding
import android.content.pm.PackageManager
import android.util.Log
import com.student.mappic.clist


class AddMapActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityAddMapBinding
    private lateinit var camiX: CamiX

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

    // FIXME fix Step1Fragm. being null (probably too early created?) or MOVE IT to Step1Fragment
    public fun startPreview() {
        /*
         * If Step1Fragment is visible on the screen, we need to initialize classes
         * associated with preview of the camera
         */
        Log.d(clist.AddMapActivity, ">>> Starting preview...")
        val currFrag = getStep1Fragment()
        if(isStep1FragmentOnScreen(currFrag)) {
            Log.d(clist.AddMapActivity, ">>> Constructing CamiX...")
            camiX = CamiX(this) // bind view from camera to camView
            // keep CamiX reference to take photos
        }
        else {
            Log.wtf(clist.AddMapActivity, ">>> What??? Step1Fragment not on screen?")
            if(currFrag == null) {
                Log.e(clist.AddMapActivity, ">>> Oh no, currFrag is null!")
            }
            else {
                if(currFrag.isVisible)
                    Log.d(clist.AddMapActivity, ">>> At least it's visible...")
            }
        }
    }

    /**
     * Returns Step1Fragment if it exists (Step1Fragment?).
     * If you want to use Step1Fragment you have to check if it's not null.
     */
    private fun getStep1Fragment(): Step1Fragment? {
        return supportFragmentManager.findFragmentByTag(clist.Step1Fragment/*"Step1Fragment"*/) as Step1Fragment?
    }

    private fun isStep1FragmentOnScreen(currFrag: Step1Fragment?): Boolean {
        /*
         * Note: this is comment for me. I'm still learning Kotlin and this comment
         * wasn't meant to educate other programmers.
         *
         * Here is fun Kotlin thing: Under this comment you can see what's called 'Smart Cast'
         * Inside if first, we check if currFlag isn't null (also could be type check).
         * This allows Compiler to deduce, what type is used in next check (or operation),
         * so we don't need to explicitly cast to certain type in next check (or operation).
         *
         * Without smart cast:
         * (currFrag as Step1Fragment).isVisible
         *
         * With smart cast:
         * currFrag.isVisible
         */
        if(currFrag != null && currFrag.isVisible) {
            return true
        }
        return false
    }

    //

}