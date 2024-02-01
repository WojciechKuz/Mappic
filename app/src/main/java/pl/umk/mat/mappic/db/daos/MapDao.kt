package pl.umk.mat.mappic.db.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import pl.umk.mat.mappic.db.entities.DBMap

@Dao
interface MapDao {
    @Query("SELECT * FROM DBMap")
    fun mapList(): List<DBMap>
    /** Get certain map by it's id */
    @Query("SELECT * FROM DBMap WHERE mapid = :mid")
    fun getMap(mid: Long): List<DBMap>
    @Query("SELECT MAX(mapid) FROM DBMap")
    fun getMaxId(): Long
    @Insert
    fun insertAll(vararg maps: DBMap)
    @Update
    fun updateMaps(vararg maps: DBMap)
    @Insert
    fun insertAll(maps: List<DBMap>)
    /** Remember to delete associated points and images first! */
    @Delete
    fun delete(map: DBMap)
    @Query("DELETE FROM DBMap WHERE mapid == :delId")
    fun deleteById(delId: Long)
}