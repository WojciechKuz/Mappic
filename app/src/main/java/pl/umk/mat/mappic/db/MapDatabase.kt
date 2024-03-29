package pl.umk.mat.mappic.db

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import pl.umk.mat.mappic.db.daos.ImageDao
import pl.umk.mat.mappic.db.daos.MapDao
import pl.umk.mat.mappic.db.daos.PointDao
import pl.umk.mat.mappic.db.entities.DBImage
import pl.umk.mat.mappic.db.entities.DBMap
import pl.umk.mat.mappic.db.entities.DBPoint
import pl.umk.mat.mappic.clist

const val DBNAME = "mappic-database"

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
            return database ?: initDatabase(context) // ?: is elvis operator - if( ==null) do something
        }

        private fun initDatabase(context: Context): MapDatabase {
            Log.i(clist.MapDatabase, ">>> initializing Database")
            inits++
            if(inits != 1) {
                Log.w(clist.MapDatabase, ">>> Warning! Database initialized ${inits} times in app lifetime.")
            }
            database = Room.databaseBuilder(
                context,
                MapDatabase::class.java, DBNAME
            ).build()
            return database!!
        }
    }
}