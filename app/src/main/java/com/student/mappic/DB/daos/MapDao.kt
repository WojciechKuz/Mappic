package com.student.mappic.DB.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.student.mappic.DB.entities.DBMap

@Dao
interface MapDao {
    @Query("SELECT * FROM DBMap")
    fun MapList(): List<DBMap>
    /** Get certain map by it's id */
    @Query("SELECT * FROM DBMap WHERE mapid = :mid")
    fun getMap(mid: Long): List<DBMap>
    @Insert
    fun insertAll(vararg maps: DBMap)
    @Insert
    fun insertAll(maps: List<DBMap>)
    @Delete
    fun delete(map: DBMap)
}