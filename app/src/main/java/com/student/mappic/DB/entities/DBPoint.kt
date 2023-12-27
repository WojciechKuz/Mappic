package com.student.mappic.DB.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

// (DBImage.class, [DBImage.imgid], )
// @ForeignKey(DBImage, [DBImage.imgid], [DBPoint.uid_fk]

@Entity(foreignKeys = [ForeignKey(
    entity = DBImage::class,
    childColumns = ["imgid_fk"],
    parentColumns = ["imgid"]
)])
data class DBPoint (
    @PrimaryKey
    val pid: Long,
    /** Id of image on which this point is defined. Foreign key. */
    val imgid_fk: Long?,
    val xpx: Long?,
    val ypx: Long?,
    /** ygps, North-South */
    val nsgps: Double?,
    /** xgps, East-West */
    val ewgps: Double?
)

/*
    According to this site https://techdroid6.rssing.com/chan-56565215/article9.html
    In Entity class ? behind field type prevents field in SQLite from being declared as NOT NULL
*/
