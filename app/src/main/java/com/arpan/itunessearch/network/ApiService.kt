package com.arpan.itunessearch.network

import com.arpan.itunessearch.data.AllResults
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("search/")
    fun getDataList(@Query("term") term : String?): Call<AllResults>
}