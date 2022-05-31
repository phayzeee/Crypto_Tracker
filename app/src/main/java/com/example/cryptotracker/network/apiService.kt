package com.example.cryptotracker.network

import com.example.cryptotracker.apiresponse.TopCoinsResponse.CryptoApiResponse
import com.example.cryptotracker.apiresponse.TrendingCoinsResponse.TrendingGeckoResponse
import retrofit2.Call
import retrofit2.http.GET

interface apiService {

    @GET("data-api/v3/cryptocurrency/listing?start=1&limit=100")
    fun getCurrency(): Call<CryptoApiResponse>

    @GET("api/v3/search/trending")
    fun getTrendingCoins() : Call<TrendingGeckoResponse>
}