package com.arpan.itunessearch.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.arpan.itunessearch.data.Song

@Database(entities = [Song::class] , version = 1, exportSchema = false)
public abstract class SongDatabase : RoomDatabase() {

    abstract val songDao: SongDao

    companion object {

        @Volatile
        private var INSTANCE: SongDatabase? = null
        private const val DATABASE_NAME = "song_database"

        fun getInstance(context: Context): SongDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = databaseBuilder(
                            context.applicationContext,
                            SongDatabase::class.java,
                            DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}