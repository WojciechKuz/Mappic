package com.student.mappic.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.student.mappic.db.MPoint

// (DBImage.class, [DBImage.imgid], )
// @ForeignKey(DBImage, [DBImage.imgid], [DBPoint.uid_fk]

@Entity(foreignKeys = [ForeignKey(
    entity = DBImage::class,
    childColumns = ["imgid_fk"],
    parentColumns = ["imgid"]
)])
data class DBPoint (
    @PrimaryKey
    val pid: Long? = null,
    /** Id of image on which this point is defined. Foreign key. */
    val imgid_fk: Long?,
    val xpx: Long?,
    val ypx: Long?,
    /** ygps, North-South */
    val nsgps: Double?,
    /** xgps, East-West */
    val ewgps: Double?,
    /** if point is reference for locating user. 1 or 0. */
    val reference: Int?
) {
    companion object {
        fun toDBPoint(mPoint: MPoint, imgid: Long): DBPoint {
            return DBPoint(
                imgid_fk = imgid,
                xpx = mPoint.xpx.toLong(),
                ypx = mPoint.ypx.toLong(),
                nsgps = mPoint.ygps,
                ewgps = mPoint.xgps,
                reference = if(mPoint.reference) 1 else 0
            )
        }
        /** Creates new DBPoint
         * @param dbPoint provides ids (pid, imgid_fk),
         * @param mPoint provides the rest
         */
        fun updatePoint(dbPoint: DBPoint, mPoint: MPoint): DBPoint {
            return DBPoint(
                pid = dbPoint.pid,
                imgid_fk = dbPoint.imgid_fk,
                xpx = mPoint.xpx.toLong(),
                ypx = mPoint.ypx.toLong(),
                nsgps = mPoint.ygps,
                ewgps = mPoint.xgps,
                reference = if(mPoint.reference) 1 else 0
            )
        }
    }
}

/*
    According to this site https://techdroid6.rssing.com/chan-56565215/article9.html
    In Entity class ? behind field type prevents field in SQLite from being declared as NOT NULL
*/
