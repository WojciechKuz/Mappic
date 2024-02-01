package pl.umk.mat.mappic.addmap


import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import pl.umk.mat.mappic.R
import pl.umk.mat.mappic.addmap.features.permissions.PermissionManager
import pl.umk.mat.mappic.clist
import pl.umk.mat.mappic.databinding.ActivityAddMapBinding


class AddMapActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityAddMapBinding
    //lateinit var permManager: PermissionManager // handles permissions
    val permManager = PermissionManager(this) //LifeCycleOwners must register before they are created/resumed
    private var created = false

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        // if contains extra it's edit map mode
        val whichmap = intent.getLongExtra("whichmap", -1)
        val startWith = intent.getIntExtra("startWith", -1)
        val viewModel: NewMapViewModel by viewModels()
        if(whichmap != -1L) {
            viewModel.getEditMap(this, whichmap)
        }
        if(startWith == 0) {
            viewModel.startWith0 = true
        }

        binding = ActivityAddMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_2)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration) // toolbar on top with element name
        created = true
    }

    fun getPermissionManager(): PermissionManager {
        if(!created)
            Log.w(clist.AddMapActivity, ">>> Activity not created yet !!!")
        return permManager
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_2)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}