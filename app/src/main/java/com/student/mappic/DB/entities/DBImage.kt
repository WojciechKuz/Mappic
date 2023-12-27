package com.student.mappic.DB.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(
    entity = DBMap::class,
    childColumns = ["mapid_fk"],
    parentColumns = ["mapid"]
)])
data class DBImage(
    @PrimaryKey
    val imgid: Long,
    val mapid_fk: Long?,
    val uri: String?
)
