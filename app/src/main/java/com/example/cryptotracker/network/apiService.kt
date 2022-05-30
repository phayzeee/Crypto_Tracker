package com.example.cryptotracker.network

import com.example.cryptotracker.apiresponse.CryptoApiResponse
import retrofit2.Call
import retrofit2.http.GET

interface apiService {

    @GET("data-api/v3/cryptocurrency/listing?start=1&limit=100")
    fun getCurrency(): Call<CryptoApiResponse>
}