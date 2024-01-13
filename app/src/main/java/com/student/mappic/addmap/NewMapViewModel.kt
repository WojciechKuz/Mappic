package com.student.mappic.addmap

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.student.mappic.DB.DBAccess
import com.student.mappic.DB.MPoint
import com.student.mappic.DB.MapDatabase
import com.student.mappic.DB.entities.DBMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

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

    /** Adds data from ViewModel to database. */
    fun addNewMap(context: Context) {
        // Przekaże zadanie do puli wątków odpowiedzialnych za operacje wej/wyj
        // This will move task to shared pool of threads responsible for I/O operations
        viewModelScope.launch(Dispatchers.IO) {
            // SHOULD I MAKE THIS FUNCTION AND DAO METHODS SUSPENDED???
            DBAccess().addNewMap(context, this@NewMapViewModel)
        }
    }

    /** check if point p1 or p2 is initialized */
    fun isInitialized(pointNo: Int): Boolean {
        return if(pointNo == 1) this::p1.isInitialized else if(pointNo == 2) this::p2.isInitialized else throw Exception("Wrong number of point! Only accepted are 1 and 2.")
    }
}