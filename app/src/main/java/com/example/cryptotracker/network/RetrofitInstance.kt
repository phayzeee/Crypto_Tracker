package com.example.cryptotracker.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitInstance {
    val BASE_URL = "https://api.coinmarketcap.com/"
    val base_url2 = "https://api.coingecko.com/"

    private lateinit var retrofit : Retrofit
    private lateinit var retrofit2 : Retrofit


    fun <T> buildService(service: Class<T>): T {

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()


        return retrofit.create(service)

    }

    fun <T> buildService2(service: Class<T>): T {

        retrofit2 = Retrofit.Builder()
            .baseUrl(base_url2)
            .addConverterFactory(GsonConverterFactory.create())
            .build()


        return retrofit2.create(service)

    }



}
