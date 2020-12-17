package com.arpan.itunessearch.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.arpan.itunessearch.R
import com.arpan.itunessearch.data.Song
import com.squareup.picasso.Picasso

class SongAdapter(private var context: Context, private var onSongClickListener: OnSongClickListener) : RecyclerView.Adapter<SongAdapter.ViewHolder>() {

    private var songList: List<Song>? = null

    public fun updateSongList(songs : List<Song>?) {
        this.songList = songs
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.song_layout, parent, false)
        return ViewHolder(view, onSongClickListener);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val song = songList?.get(position)

        holder.songName.text = song?.trackName
        holder.artistName.text = song?.artistName

        if(song?.artworkUrl100 != null) {
            Picasso.get()
                    .load(song?.artworkUrl100)
                    .placeholder(android.R.drawable.ic_input_add)
                    .resize(60, 60)
                    .into(holder.imageView)
        }

    }

    override fun getItemCount(): Int {

        if(songList != null) {
            return songList!!.size
        }
        return 0;
    }

    public class ViewHolder(itemView: View, private var mOnSongClickListener: OnSongClickListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var imageView : ImageView = itemView.findViewById(R.id.imageId)
        var songName : TextView = itemView.findViewById(R.id.titleId)
        var artistName : TextView = itemView.findViewById(R.id.artistId)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            mOnSongClickListener.OnSongClick(adapterPosition)
        }

    }

    public interface OnSongClickListener {
        fun OnSongClick(position : Int) {}
    }


}