package com.student.mappic.DB.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.student.mappic.DB.entities.DBPoint

@Dao
interface PointDao {
    /** get all points on image */
    @Query("SELECT * FROM DBPoint WHERE imgid_fk = :iid")
    fun getPointsOnImage(iid: Long): List<DBPoint>

    /** Get all points on map */
    @Query("SELECT * FROM DBPoint WHERE imgid_fk in (SELECT imgid FROM DBImage WHERE mapid_fk = :mid)")
    fun getPointsOnMap(mid: Long): List<DBPoint>

    // FIXME if this doesn't work rollback to method above ^
    // ksp WARNING during build
    /** Get ids of all points on map */
    @Query("SELECT pid FROM DBPoint WHERE imgid_fk in (SELECT imgid FROM DBImage WHERE mapid_fk = :mid)")
    fun getPointIdsOnMap(mid: Long): List<DBPoint> // NOT USED

    @Query("SELECT * FROM DBPoint WHERE imgid_fk == :imageid AND reference == 1") // reference == 1 means == true, because it's integer
    fun getReferencePointsForImage(imageid: Long): List<DBPoint>

    @Query("SELECT MAX(pid) FROM DBPoint")
    fun getMaxId(): Long
    /** Get certain point by it's id */
    @Query("SELECT * FROM DBPoint WHERE pid = :pid")
    fun getPoint(pid: Long): List<DBPoint>

    // inserts
    @Insert
    fun insertAll(vararg points: DBPoint)
    @Update
    fun updatePoints(vararg points: DBPoint)
    @Insert
    fun insertAll(points: List<DBPoint>)

    // deletes
    @Delete
    fun delete(point: DBPoint)
    @Query("DELETE FROM DBPoint WHERE pid in (:delIds)")
    fun deleteMultiple(delIds: List<Long>)
    @Query("DELETE FROM DBPoint WHERE imgid_fk in (SELECT imgid FROM DBImage WHERE mapid_fk = :mid)")
    fun deletePointsForMapid(mid: Long)

}