package com.arpan.itunessearch.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.arpan.itunessearch.data.Song

@Dao
public interface SongDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(songList : List<Song>?)

    @Query("SELECT * FROM song_tbl")
    fun getAllSongs()  : LiveData<List<Song>>?

    @Query("DELETE FROM song_tbl")
    fun deleteAll()
}