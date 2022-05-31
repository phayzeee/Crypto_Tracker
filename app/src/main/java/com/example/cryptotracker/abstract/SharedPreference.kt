package com.example.cryptotracker.abstract

import android.content.Context
import android.content.SharedPreferences

class SharedPreference(context: Context) {
    private var pref: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null
    private var context: Context? = null
    private val PRIVATE_MODE = 0

    private val BTC = "btc_price"

    init {
        this.context = context
        pref = context.getSharedPreferences("kotlinsharedpreference", PRIVATE_MODE)
        editor = pref?.edit()
    }

    fun setBTCPrice(btcPrice: Double?) {
        editor!!.putString(BTC, btcPrice!!.toString())
        editor!!.commit()
    }

    fun getBTCPrice(): String? {
        return pref?.getString(BTC, "")
    }
}