package com.example.cryptotracker.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptotracker.R
import com.example.cryptotracker.abstract.SharedPreference
import com.example.cryptotracker.apiresponse.TrendingCoinsResponse.Coin
import com.example.cryptotracker.apiresponse.TrendingCoinsResponse.Item
import kotlinx.android.synthetic.main.currency_rv_item.view.*
import java.text.DecimalFormat

class TrendingRVAdapter(context: Context): RecyclerView.Adapter<TrendingRVAdapter.ViewHolder>() {

    private lateinit var currencyRvModalArrayList: ArrayList<Coin>
    private var sharedPreference : SharedPreference = SharedPreference(context)


    fun setTrendingList( currencyRvModalArrayList: ArrayList<Coin>){
        this.currencyRvModalArrayList = currencyRvModalArrayList
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.currency_rv_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currencyRvModalArrayList[position].item, sharedPreference)
    }

    override fun getItemCount(): Int {
        return currencyRvModalArrayList.size
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var currencyNameTv: TextView = view.idTVCurrencyName
        var symbolTv: TextView = view.idTVSymbol
        var rateTv: TextView = view.idTVCurrencyRate
        var df2: DecimalFormat = DecimalFormat("#.######")


        fun bind(data: Item, sharedPreference: SharedPreference){
            currencyNameTv.text = data.name
            symbolTv.text = data.symbol
            rateTv.text = "$ " + df2.format(data.price_btc * sharedPreference.getBTCPrice()!!.toDouble())
        }
    }


}