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
            // Why synchronized? it blocks thread... Why not asynchronous, like suspend fun?
            /*return database ?: synchronized(this) { // ?: is elvis operator - if( ==null) do something
                database ?: initDatabase(context).also { database = it }
            }*/
            return database ?: initDatabase(context) // ?: is elvis operator - if( ==null) do something
        }

        private fun initDatabase(context: Context): MapDatabase {
            Log.i(clist.MapDatabase, ">>> initializing Database")
            inits++
            if(inits != 1) {
                // This is logged quite frequently
                // TODO inspect why it creates more than one instance
                // FIXME make sure app creates one database reference and keeps it through it's life
                Log.w(clist.MapDatabase, ">>> Warning! Database initialized ${inits} times in app lifetime.")
            }
            return Room.databaseBuilder(
                context,
                MapDatabase::class.java, DBNAME
            ).build()
        }
    }
}