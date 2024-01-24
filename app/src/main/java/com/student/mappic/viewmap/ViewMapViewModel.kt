package com.student.mappic.viewmap

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.student.mappic.DB.DBAccess
import com.student.mappic.DB.entities.DBImage
import com.student.mappic.DB.entities.DBPoint
import com.student.mappic.addmap.common.PassStuff
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewMapViewModel: ViewModel() {

    private inner class DBAcc: DBAccess

    /** Get images */
    fun getMapImages(
        context: Context, mapid: Long, receiveImages: PassStuff<List<DBImage>>
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val images = DBAcc().getMapImages(context, mapid)
            receiveImages.pass(images)
        }
    }
    /** Get all points on given map */
    fun getMapPoints(
        context: Context, mapid: Long, receivePoints: PassStuff<List<DBPoint>>
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val points = DBAcc().getMapPoints(context, mapid)
            receivePoints.pass(points)
        }
    }
    // TODO override / implement methods from DBAccess
    //  addPoint()
    //  deletePoint()
    //  editPoint()

    // maybe points should contain names?
}