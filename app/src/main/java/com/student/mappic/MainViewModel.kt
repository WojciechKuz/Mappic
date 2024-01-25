package com.student.mappic

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.student.mappic.db.DBAccess
import com.student.mappic.db.entities.DBMap
import com.student.mappic.addmap.common.PassStuff
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    lateinit var mapList: List<DBMap>

    private inner class DBAcc: DBAccess

    /** Get list of maps. mapid, map_name */
    fun getMapList(context: Context, receiveMaps: PassStuff<List<DBMap>>) {
        viewModelScope.launch(Dispatchers.IO) {
            val maplist = DBAcc().getMapList(context)
            receiveMaps.pass(maplist)
        }
    }
    /** Deletes map and associated images and points. */
    fun deleteMap(context: Context, mapid: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            DBAcc().deleteMap(context, mapid)
        }
    }

    /** check if list is initialized */
    fun isListInitialized(): Boolean {
        return this::mapList.isInitialized
    }
}