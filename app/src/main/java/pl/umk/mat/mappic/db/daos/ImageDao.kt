package pl.umk.mat.mappic.db.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import pl.umk.mat.mappic.db.entities.DBImage

@Dao
interface ImageDao {

    @Query("SELECT * FROM DBImage WHERE mapid_fk = :mid")
    fun getImagesForMap(mid: Long): List<DBImage>
    /** Get certain image by it's id */
    @Query("SELECT * FROM DBImage WHERE imgid = :iid")
    fun getImage(iid: Long): List<DBImage>
    @Query("SELECT MAX(imgid) FROM DBImage")
    fun getMaxId(): Long
    @Insert
    fun insertAll(vararg images: DBImage)
    @Update
    fun updateImages(vararg images: DBImage)
    @Insert
    fun insertAll(images: List<DBImage>)
    @Delete
    fun delete(image: DBImage)
    @Query("DELETE FROM DBImage WHERE imgid == :delId")
    fun deleteById(delId: Long)
    /** Remember to delete associated points first! */
    @Query("DELETE FROM DBImage WHERE mapid_fk == :mid")
    fun deleteImageForMapid(mid: Long)
}