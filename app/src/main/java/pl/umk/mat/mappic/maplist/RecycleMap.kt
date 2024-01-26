package pl.umk.mat.mappic.maplist

import pl.umk.mat.mappic.db.entities.DBMap

/** Data of row for RecyclerView in MainView */
data class RecycleMap(
    val mapid: Long?,
    val name: String
    // FUTURE thumbnail?
) {
    fun toDBMap(): DBMap {
        return DBMap(mapid, name)
    }
    companion object {
        fun dbMaptoRecycleMap(dbMap: DBMap): RecycleMap {
            return RecycleMap(dbMap.mapid, dbMap.map_name!!)
        }
    }
}