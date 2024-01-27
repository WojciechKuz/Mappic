package pl.umk.mat.mappic.db.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import pl.umk.mat.mappic.db.entities.DBPoint

@Dao
interface PointDao {
    /** get all points on image */
    @Query("SELECT * FROM DBPoint WHERE imgid_fk = :iid")
    fun getPointsOnImage(iid: Long): List<DBPoint>

    /** Get all points on map */
    @Query("SELECT * FROM DBPoint WHERE imgid_fk in (SELECT imgid FROM DBImage WHERE mapid_fk = :mid)")
    fun getPointsOnMap(mid: Long): List<DBPoint>

    // This does the same as above, but it's worse.
    // ksp WARNING during build
    /** Get ids of all points on map */
    @Query("SELECT pid FROM DBPoint WHERE imgid_fk in (SELECT imgid FROM DBImage WHERE mapid_fk = :mid)")
    fun getPointIdsOnMap(mid: Long): List<DBPoint> // NOT USED

    @Query("SELECT * FROM DBPoint WHERE imgid_fk == :imageid AND reference == 1") // reference == 1 means == true, because it's integer
    fun getReferencePointsForImage(imageid: Long): List<DBPoint>

    @Query("SELECT * FROM DBPoint WHERE imgid_fk in (SELECT imgid FROM DBImage WHERE mapid_fk = :mid) AND reference == 1")
    fun getReferencePointsForMap(mid: Long): List<DBPoint>

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