package com.student.mappic.DB

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.student.mappic.DB.daos.ImageDao
import com.student.mappic.DB.daos.MapDao
import com.student.mappic.DB.daos.PointDao
import com.student.mappic.DB.entities.DBImage
import com.student.mappic.DB.entities.DBMap
import com.student.mappic.DB.entities.DBPoint
import com.student.mappic.clist

val DBNAME = "mappic-database"

@Database(entities = [DBMap::class, DBImage::class, DBPoint::class], version = 1)
abstract class MapDatabase: RoomDatabase() {
    abstract fun mapDao(): MapDao
    abstract fun imageDao(): ImageDao
    abstract fun pointDao(): PointDao

    companion object {

        @Volatile // This means that read and write operations are atomic and visible to other threads.
        private var database: MapDatabase? = null

        private var inits = 0

        fun getDB(context: Context): MapDatabase {
            // Why synchronized? it blocks thread... Why not asynchronous, like suspend fun?
            /*return database ?: synchronized(this) { // ?: is elvis operator - if( ==null) do something
                database ?: initDatabase(context).also { database = it }
            }*/
            return database ?: initDatabase(context) // ?: is elvis operator - if( ==null) do something
        }

        private fun initDatabase(context: Context): MapDatabase {
            Log.i(clist.MapDatabase, ">>> initializing Database")
            inits++
            if(inits != 1)
                Log.w(clist.MapDatabase, ">>> Warning! Database initialized ${inits} times in app lifetime.")
            return Room.databaseBuilder(
                context,
                MapDatabase::class.java, DBNAME
            ).build()
        }
    }
}