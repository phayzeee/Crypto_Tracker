package com.example.cryptotracker.activity


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptotracker.*
import com.example.cryptotracker.abstract.SharedPreference
import com.example.cryptotracker.abstract.SwipeToDelete
import com.example.cryptotracker.abstract.SwipeToRight
import com.example.cryptotracker.adapter.CurrencyRVAdapter
import com.example.cryptotracker.adapter.TrendingRVAdapter
import com.example.cryptotracker.apiresponse.TopCoinsResponse.CryptoApiResponse
import com.example.cryptotracker.apiresponse.TopCoinsResponse.CryptoCurrency
import com.example.cryptotracker.apiresponse.TrendingCoinsResponse.Coin
import com.example.cryptotracker.apiresponse.TrendingCoinsResponse.Item
import com.example.cryptotracker.apiresponse.TrendingCoinsResponse.TrendingGeckoResponse
import com.example.cryptotracker.network.RetrofitInstance
import com.example.cryptotracker.network.apiService
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    var currencyRVModelArrayList = ArrayList<CryptoCurrency>()
    var trendingRVModelArrayList = ArrayList<Coin>()
    lateinit var currencyRVAdapter: CurrencyRVAdapter
    lateinit var trendingRVAdapter: TrendingRVAdapter
    private lateinit var sharedPreference: SharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        sharedPreference = SharedPreference(this)

        currencyRVAdapter = CurrencyRVAdapter()
        trendingRVAdapter = TrendingRVAdapter(this)

        getCoins()


        currencyRVAdapter.setOnItemClickListener(object : CurrencyRVAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {

                    val intent = Intent(this@MainActivity, ChartActivity::class.java)
                    intent.putExtra("sym_bol", currencyRVModelArrayList[position].symbol)
                    intent.putExtra("coin_name", currencyRVModelArrayList[position].name)
                    intent.putExtra("percentChange", currencyRVModelArrayList[position].quotes[0].percentChange1h)
                    startActivity(intent)

            }
        })

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            getCoins()
        }

        tlCoins.setOnClickListener {
            getTrendingCoins()
        }


        swipeToRight()

    }

    private fun getCoins() {

        val request = RetrofitInstance.buildService(apiService::class.java)

        val call = request.getCurrency()

        call.enqueue(object : Callback<CryptoApiResponse> {
            override fun onResponse(call: Call<CryptoApiResponse>, response: Response<CryptoApiResponse>) {
                try {
                    if (response.body() != null) {
                        if (response.code() == 200) {
                            val data= response.body()
                            setupUI(data!!.data.cryptoCurrencyList)
                            sharedPreference.setBTCPrice(data.data.cryptoCurrencyList[0].quotes[0].price)
                        } else {
                        }
                    } else {
                        Toast.makeText(this@MainActivity,"Error",Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {

                    e.message?.let {Toast.makeText(this@MainActivity,it,Toast.LENGTH_SHORT).show() }
                }
            }

            override fun onFailure(call: Call<CryptoApiResponse>, t: Throwable) {

                t.message?.let { Toast.makeText(this@MainActivity,it,Toast.LENGTH_SHORT).show() }
            }
        })

    }

    private fun getTrendingCoins() {

        val request = RetrofitInstance.buildService2(apiService::class.java)

        val call = request.getTrendingCoins()

        call.enqueue(object : Callback<TrendingGeckoResponse> {
            override fun onResponse(call: Call<TrendingGeckoResponse>, response: Response<TrendingGeckoResponse>) {
                try {
                    if (response.body() != null) {
                        if (response.code() == 200) {
                            val data= response.body()
                            setupTrendingUI(data!!.coins)

                        } else {
                        }
                    } else {
                        Toast.makeText(this@MainActivity,"Error",Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {

                    e.message?.let {Toast.makeText(this@MainActivity,it,Toast.LENGTH_SHORT).show() }
                }
            }

            override fun onFailure(call: Call<TrendingGeckoResponse>, t: Throwable) {

                t.message?.let { Toast.makeText(this@MainActivity,it,Toast.LENGTH_SHORT).show() }
            }
        })

    }

    fun setupUI(cryptoCurrency: List<CryptoCurrency>){
        currencyRVModelArrayList.clear()
        currencyRVModelArrayList.addAll(cryptoCurrency)
        currencyRVAdapter.setCurrencyList(currencyRVModelArrayList)
        idRVCurrencies.adapter = currencyRVAdapter
        swipeToDelete()

    }

    fun setupTrendingUI(item: List<Coin>){
        currencyRVModelArrayList.clear()
        trendingRVModelArrayList.clear()
        trendingRVModelArrayList.addAll(item)
        trendingRVAdapter.setTrendingList(trendingRVModelArrayList)
        idRVCurrencies.adapter = trendingRVAdapter
    }

    private fun swipeToDelete() {
        val swipeToDelete = object : SwipeToDelete(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                currencyRVAdapter.deleteItem(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDelete)
        itemTouchHelper.attachToRecyclerView(idRVCurrencies)
    }

    private fun swipeToRight(){
        val swipetoright = object : SwipeToRight(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                currencyRVAdapter.showDialog(viewHolder.adapterPosition, this@MainActivity, currencyRVModelArrayList )

            }
        }
        val itemTouchHelper = ItemTouchHelper(swipetoright)
        itemTouchHelper.attachToRecyclerView(idRVCurrencies)

    }



}