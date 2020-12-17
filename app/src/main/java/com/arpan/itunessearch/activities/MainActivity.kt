package com.arpan.itunessearch.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arpan.itunessearch.R
import com.arpan.itunessearch.adapters.SongAdapter
import com.arpan.itunessearch.data.Song
import com.arpan.itunessearch.service.MusicService
import com.arpan.itunessearch.viewmodel.MainViewModel
import com.google.android.material.appbar.MaterialToolbar

class MainActivity : AppCompatActivity(), SongAdapter.OnSongClickListener {

    private lateinit var toolbar : MaterialToolbar

    private lateinit var recyclerView : RecyclerView
    private lateinit var songAdapter : SongAdapter
    private lateinit var mMainViewModel: MainViewModel

    private var songList : List<Song>? = null

    private var URL : String? = null
    private var musicService : MusicService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        mMainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)


        mMainViewModel.getSongList()?.observe(this, { t ->
            songList = t
            songAdapter.updateSongList(t)
        })

        setAllViews();

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main_menu, menu)

        val menuItem : MenuItem? = menu?.findItem(R.id.app_bar_search)
        val searchView : SearchView = menuItem?.actionView as SearchView
        searchView.queryHint = "Artist or Song"

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                if (query != null) {
                    mMainViewModel.getData(query)
                }

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        return true
    }

    private fun setAllViews() {

        initToolbar()
        initRecylerView()

    }


    private fun initToolbar() {
        toolbar = findViewById(R.id.main_toolbar)
        setSupportActionBar(toolbar)
    }

    private fun initRecylerView() {
        recyclerView = findViewById(R.id.recyclerViewId)
        recyclerView.hasFixedSize()
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)

        songAdapter = SongAdapter(applicationContext, this)
        recyclerView.adapter = songAdapter
    }

    override fun OnSongClick(position: Int) {

        Log.d("TAG", songList?.get(position)?.previewUrl.toString())
        val url = songList?.get(position)?.previewUrl
        val intent: Intent = Intent(applicationContext, MusicService::class.java)
        intent.putExtra("URL", url)

        startService(intent)
    }

}