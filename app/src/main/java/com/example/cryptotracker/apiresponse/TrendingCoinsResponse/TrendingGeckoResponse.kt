package com.example.cryptotracker.apiresponse.TrendingCoinsResponse

data class TrendingGeckoResponse(
    val coins: List<Coin>,
    val exchanges: List<Any>
)