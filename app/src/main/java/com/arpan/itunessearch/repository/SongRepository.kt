package com.arpan.itunessearch.repository

import android.app.Application
import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.arpan.itunessearch.room.SongDao
import com.arpan.itunessearch.data.AllResults
import com.arpan.itunessearch.data.Song
import com.arpan.itunessearch.room.SongDatabase
import com.arpan.itunessearch.network.ApiService
import com.arpan.itunessearch.network.ServiceBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

class SongRepository(private var application: Application) : CoroutineScope {
    private var instance : SongRepository? = null
    private var songList : List<Song>? = ArrayList()
    private var data : MutableLiveData<ArrayList<Song>> = MutableLiveData()
    private val database = SongDatabase.getInstance(application)

    public fun getInstance() : SongRepository? {
        if(instance == null) {
            instance = SongRepository(application)
        }
        return instance
    }

    public fun getSongList() : LiveData<List<Song>>? {
        return database.songDao.getAllSongs()
    }

    public fun insert(list : List<Song>?) {
        UpdateAsyncTask(database, list).execute()
    }

    public fun getData(term: String?) {

        // create the class
        val serviceClass = ServiceBuilder.buildService(ApiService::class.java)

        val requestCall = serviceClass.getDataList(term)

        requestCall.enqueue(object : Callback<AllResults> {
            override fun onResponse(call: Call<AllResults>, response: Response<AllResults>) {
                if (response.isSuccessful) {
                    songList = response.body()?.results
                    Log.d("response", songList?.size.toString())
                    insert(songList)
//                    data.value = songList
                } else {
                    Log.d("response", "not successful")
                }
            }

            override fun onFailure(call: Call<AllResults>, t: Throwable) {
                Log.d("failure", t.toString())
            }

        })
    }

    class InsertAsyncTask(songDatabase: SongDatabase) : AsyncTask<List<Song>?, Void?, Void?>() {

        private var songDao : SongDao = songDatabase.songDao

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            Log.d("async", songDao.getAllSongs()?.value.toString())
        }

        override fun doInBackground(vararg params: List<Song>?): Void? {
            songDao.insert(params[0])
            Log.d("async2", params[0]?.size.toString())
            return null
        }

    }

    class UpdateAsyncTask(private val songDatabase: SongDatabase, private val songList: List<Song>?) : AsyncTask<Void?, Void?, Void?>() {
        private var songDao : SongDao = songDatabase.songDao
        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            InsertAsyncTask(songDatabase).execute(songList)
        }
        override fun doInBackground(vararg params: Void?): Void? {
            songDao.deleteAll()
            return null
        }

    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main
}