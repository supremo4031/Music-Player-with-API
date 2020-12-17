package com.arpan.itunessearch.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arpan.itunessearch.data.Song
import com.arpan.itunessearch.repository.SongRepository

class MainViewModel(application: Application): AndroidViewModel(application) {

    private var mSongList : LiveData<List<Song>>? = null

    private var mSongRepository : SongRepository? = SongRepository(application).getInstance()

    init  {
        mSongList = mSongRepository?.getSongList()
    }

    public fun getSongList() : LiveData<List<Song>>? {
        return mSongList
    }

    public fun getData(query : String?) {
        mSongRepository?.getData(query)
    }
}