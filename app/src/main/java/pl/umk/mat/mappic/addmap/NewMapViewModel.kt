package pl.umk.mat.mappic.addmap

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import pl.umk.mat.mappic.db.DBAccess
import pl.umk.mat.mappic.db.MPoint
import pl.umk.mat.mappic.db.entities.DBMap
import pl.umk.mat.mappic.addmap.common.PassStuff
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * ViewModel for passing data between fragments in addmap package.
 */
class NewMapViewModel: ViewModel() {
    lateinit var mapImg: Uri
    lateinit var p1: MPoint
    lateinit var p2: MPoint
    lateinit var name: String
    /** For editing mode */
    var mapid: Long? = null

    /**
     * When editing map (addmap with mapid set)
     * Step0 navigates immediately navigates to step1ok, but it should occur only once.
     * This allows to navigate back to step0.
     */
    var step0visited = false

    private inner class DBAcc: DBAccess

    /** Adds data from this ViewModel to database as a new map. */
    fun addNewMap(context: Context) {
        // Przekaże zadanie do puli wątków odpowiedzialnych za operacje wej/wyj
        // This will move task to shared pool of threads responsible for I/O operations
        viewModelScope.launch(Dispatchers.IO) {
            // SHOULD I MAKE THIS FUNCTION AND DAO METHODS SUSPENDED???
            DBAcc().addNewMap(context, this@NewMapViewModel)
        }
    }
    /** Get data of map to be edited. Puts data into ViewModel */
    fun getEditMap(context: Context, mapid: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            // WARNING! if viewmodel isn't passed as reference, then it wouldn't work!
            DBAcc().getEditMap(context, mapid, this@NewMapViewModel)
        }
    }
    /** Change map in database. Record in database will be overwritten by data from ViewModel */
    fun editMap(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            DBAcc().editMap(context, this@NewMapViewModel)
        }
    }
    /** Get list of maps */
    fun getMapList(context: Context, receiveMaps: PassStuff<List<DBMap>>) {
        viewModelScope.launch(Dispatchers.IO) {
            val maps = DBAcc().getMapList(context)
            receiveMaps.pass(maps)
        }
    }

    /** check if point p1 or p2 is initialized */
    fun isInitialized(pointNo: Int): Boolean {
        return when (pointNo) {
            1 -> this::p1.isInitialized
            2 -> this::p2.isInitialized
            else -> throw Exception("Wrong number of point! Only accepted are 1 and 2.")
        }
    }
}