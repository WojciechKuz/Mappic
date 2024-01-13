package com.student.mappic.DB.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DBMap(
    @PrimaryKey
    val mapid: Long? = null,
    val map_name: String?
)