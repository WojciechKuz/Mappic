package com.student.mappic.DB.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.student.mappic.DB.entities.DBPoint

@Dao
interface PointDao {
    /** get all points on image */
    @Query("SELECT * FROM DBPoint WHERE imgid_fk = :iid")
    fun getPointsOnImage(iid: Long): List<DBPoint>
    /** Get all points on map */
    @Query("SELECT * FROM DBPoint WHERE imgid_fk in (SELECT imgid FROM DBImage WHERE mapid_fk = :mid)")
    fun getPointsOnMap(mid: Long): List<DBPoint>
    /** Get certain point by it's id */
    @Query("SELECT * FROM DBPoint WHERE pid = :pid")
    fun getPoint(pid: Long): List<DBPoint>

    @Insert
    fun insertAll(vararg points: DBPoint)
    @Insert
    fun insertAll(points: List<DBPoint>)
    @Delete
    fun delete(point: DBPoint)
}