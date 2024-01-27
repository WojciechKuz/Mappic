package pl.umk.mat.mappic.viewmap

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import pl.umk.mat.mappic.db.DBAccess
import pl.umk.mat.mappic.db.entities.DBImage
import pl.umk.mat.mappic.db.entities.DBPoint
import pl.umk.mat.mappic.common.PassStuff
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewMapViewModel: ViewModel() {

    private inner class DBAcc: DBAccess


    fun getMapName(
        context: Context, mapid: Long, receiveName: PassStuff<String>
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val name = DBAcc().getMapName(context, mapid)
            if(name != null)
                receiveName.pass(name)
            else
                Log.e("ViewMapViewModel", ">>> map name returned from DB is null")
        }
    }
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
    /** Deletes map and associated images and points. */
    fun deleteMap(context: Context, mapid: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            DBAcc().deleteMap(context, mapid)
        }
    }
    // TODO override / implement methods from DBAccess
    //  addPoint()
    //  deletePoint()
    //  editPoint()

    // maybe points should contain names?
}