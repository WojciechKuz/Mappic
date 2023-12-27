package com.student.mappic.DB.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.student.mappic.DB.entities.DBImage

@Dao
interface ImageDao {
    /*@Query("SELECT imgid FROM DBImage WHERE mapid_fk = :mid")
    fun getImgIdsOnMap(mid: Int): List<Int>*/

    @Query("SELECT * FROM DBImage WHERE mapid_fk = :mid")
    fun getImagesForMap(mid: Long): List<DBImage>
    /** Get certain image by it's id */
    @Query("SELECT * FROM DBImage WHERE imgid = :iid")
    fun getImage(iid: Long): List<DBImage>

    @Insert
    fun insertAll(vararg images: DBImage)
    @Insert
    fun insertAll(images: List<DBImage>)
    @Delete
    fun delete(image: DBImage)
}