package com.arpan.itunessearch.data

import java.io.Serializable

data class AllResults(
    val resultCount: String?,
    val results: List<Song>? = ArrayList<Song>()
)