package com.student.mappic

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.student.mappic.DB.DBAccess
import com.student.mappic.DB.entities.DBMap
import com.student.mappic.addmap.common.PassStuff
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel: ViewModel(), DBAccess {
    lateinit var mapList: List<DBMap>
    /** Get list of maps. mapid, map_name */
    fun getMapList(context: Context, receiveMaps: PassStuff<List<DBMap>>) {
        viewModelScope.launch(Dispatchers.IO) {
            val maplist = super.getMapList(context)
            receiveMaps.pass(maplist)
        }
    }

    /** check if list is initialized */
    fun isListInitialized(): Boolean {
        return this::mapList.isInitialized
    }
}