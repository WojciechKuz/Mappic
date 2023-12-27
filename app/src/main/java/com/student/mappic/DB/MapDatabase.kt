package com.student.mappic.DB

import androidx.room.Database
import androidx.room.RoomDatabase
import com.student.mappic.DB.daos.ImageDao
import com.student.mappic.DB.daos.MapDao
import com.student.mappic.DB.daos.PointDao
import com.student.mappic.DB.entities.DBImage
import com.student.mappic.DB.entities.DBMap
import com.student.mappic.DB.entities.DBPoint

@Database(entities = [DBMap::class, DBImage::class, DBPoint::class], version = 1)
abstract class MapDatabase: RoomDatabase() {
    abstract fun mapDao(): MapDao
    abstract fun imageDao(): ImageDao
    abstract fun pointDao(): PointDao
}