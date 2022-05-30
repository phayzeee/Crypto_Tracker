package com.example.cryptotracker.apiresponse

data class Data(
    val cryptoCurrencyList: ArrayList<CryptoCurrency>,
    val totalCount: String
)