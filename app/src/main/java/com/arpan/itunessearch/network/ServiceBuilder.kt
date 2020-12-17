package com.arpan.itunessearch.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceBuilder {
    private const val URL = "https://itunes.apple.com"

    // create logger
    private val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    // create Okhttp Client
    private val okHttp = OkHttpClient.Builder().addInterceptor(logger)

    // create retrofit builder
    private val builder = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttp.build())

    // create retrofit instance
    private val retrofit = builder.build()

    fun <T> buildService(serviceType : Class<T>) : T {
        return retrofit.create(serviceType)
    }
}