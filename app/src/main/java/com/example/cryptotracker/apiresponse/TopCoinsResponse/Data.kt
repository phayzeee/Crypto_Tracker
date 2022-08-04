package com.example.cryptotracker.apiresponse.TopCoinsResponse

import com.example.cryptotracker.apiresponse.TopCoinsResponse.CryptoCurrency

data class Data(
    val cryptoCurrencyList: ArrayList<CryptoCurrency>,
    val totalCount: String
)