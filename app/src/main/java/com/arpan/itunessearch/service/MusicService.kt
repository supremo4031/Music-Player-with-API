package com.arpan.itunessearch.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log
import androidx.room.Room
import com.arpan.itunessearch.room.SongDatabase

class MusicService : Service() {

    companion object {

        @Volatile
        private var INSTANCE : MediaPlayer? = null
        private var URL : String? = null

        fun getInstance(): MediaPlayer {
            synchronized(this) {
                var instance = MusicService.INSTANCE

                if (instance == null) {
                    instance = MediaPlayer()

                    MusicService.INSTANCE = instance
                    INSTANCE?.setAudioStreamType(AudioManager.STREAM_MUSIC)

                    INSTANCE?.setOnCompletionListener { MediaPlayer.OnCompletionListener {
                        URL = null
                        Log.d("TAG", "ON COMPLETE CALLED")
                    }}
                }
                return instance
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val mediaPlayer = getInstance()

        Log.d("TAG", "SERVICE STARTED")

        val url : String = intent?.getStringExtra("URL") ?: return START_STICKY

        if(url == URL) {
            if(mediaPlayer.isPlaying) {
                mediaPlayer.pause()
            } else {
                if(mediaPlayer.duration == 0) {
                    mediaPlayer.reset()

                    mediaPlayer.setDataSource(url)
                    mediaPlayer.prepare()

                    mediaPlayer.start()
                }
                else {
                    mediaPlayer.start()
                }
            }
        } else {
            if(mediaPlayer.isPlaying) {
                mediaPlayer.stop()
            }

            mediaPlayer.reset()

            URL = url

            mediaPlayer.setDataSource(url)
            mediaPlayer.prepare()

            mediaPlayer.start()
        }



        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()

        URL = null

        INSTANCE?.stop()
        INSTANCE?.release()

        Log.d("TAG", "SERVICE DESTROYED")
    }
    
}