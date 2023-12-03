package com.example.cryptotracker.activity


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptotracker.R
import com.example.cryptotracker.abstract.SharedPreference
import com.example.cryptotracker.abstract.SwipeToDelete
import com.example.cryptotracker.abstract.SwipeToRight
import com.example.cryptotracker.adapter.CurrencyRVAdapter
import com.example.cryptotracker.adapter.TrendingRVAdapter
import com.example.cryptotracker.apiresponse.TopCoinsResponse.CryptoApiResponse
import com.example.cryptotracker.apiresponse.TopCoinsResponse.CryptoCurrency
import com.example.cryptotracker.apiresponse.TrendingCoinsResponse.Coin
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

        initListener()
     //   swipeToRight()
        // swipeToRightTrending()

    }

    private fun initListener() {

        currencyRVAdapter.setOnItemClickListener(object : CurrencyRVAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {

                val intent = Intent(this@MainActivity, ChartActivity::class.java)
                intent.putExtra("sym_bol", currencyRVModelArrayList[position].symbol)
                intent.putExtra("coin_name", currencyRVModelArrayList[position].name)
                intent.putExtra(
                    "percentChange",
                    currencyRVModelArrayList[position].quotes[0].percentChange1h
                )
                startActivity(intent)

            }
        })

        topCoins.setOnClickListener {
            no_content.visibility = View.GONE
            idRVCurrencies.visibility = View.VISIBLE
            topCoins.backgroundTintList =
                ContextCompat.getColorStateList(this, R.color.black_shade_2)
            tlCoins.backgroundTintList = ContextCompat.getColorStateList(this, R.color.grey)
            //mvCoins.backgroundTintList = ContextCompat.getColorStateList(this, R.color.grey)
            getCoins()
        }

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            when {
                topCoins.isPressed -> {
                    getCoins()
                }

                tlCoins.isPressed -> {
                    getTrendingCoins()
                }
            }
        }

        tlCoins.setOnClickListener {
            no_content.visibility = View.GONE
            idRVCurrencies.visibility = View.VISIBLE
            tlCoins.backgroundTintList =
                ContextCompat.getColorStateList(this, R.color.black_shade_2)
            topCoins.backgroundTintList = ContextCompat.getColorStateList(this, R.color.grey)
            //  mvCoins.backgroundTintList = ContextCompat.getColorStateList(this, R.color.grey)
            getTrendingCoins()
        }
    }

    private fun getCoins() {

        showProgressBar()
        val request = RetrofitInstance.buildService(apiService::class.java)
        val call = request.getCurrency()

        call.enqueue(object : Callback<CryptoApiResponse> {
            override fun onResponse(
                call: Call<CryptoApiResponse>,
                response: Response<CryptoApiResponse>
            ) {
                try {
                    if (response.body() != null) {
                        if (response.code() == 200) {
                            val data = response.body()
                            setupUI(data!!.data.cryptoCurrencyList)
                            sharedPreference.setBTCPrice(data.data.cryptoCurrencyList[0].quotes[0].price)
                            hideProgressBar()
                        } else {
                            hideProgressBar()
                        }
                    } else {
                        Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {

                    e.message?.let {
                        Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<CryptoApiResponse>, t: Throwable) {

                t.message?.let { Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show() }
            }
        })

    }

    private fun getTrendingCoins() {
        showProgressBar()
        val request = RetrofitInstance.buildService2(apiService::class.java)

        val call = request.getTrendingCoins()

        call.enqueue(object : Callback<TrendingGeckoResponse> {
            override fun onResponse(
                call: Call<TrendingGeckoResponse>,
                response: Response<TrendingGeckoResponse>
            ) {
                try {
                    if (response.body() != null) {
                        if (response.code() == 200) {
                            val data = response.body()
                            setupTrendingUI(data!!.coins)
                            hideProgressBar()
                        } else {
                            hideProgressBar()
                        }
                    } else {
                        Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {

                    e.message?.let {
                        Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<TrendingGeckoResponse>, t: Throwable) {

                t.message?.let { Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show() }
            }
        })

    }

    fun setupUI(cryptoCurrency: List<CryptoCurrency>) {
        currencyRVModelArrayList.clear()
        currencyRVModelArrayList.addAll(cryptoCurrency)
        currencyRVAdapter.setCurrencyList(currencyRVModelArrayList)
        idRVCurrencies.adapter = currencyRVAdapter
     //   swipeToDelete()

    }

    fun setupTrendingUI(item: List<Coin>) {
        currencyRVModelArrayList.clear()
        trendingRVModelArrayList.clear()
        trendingRVModelArrayList.addAll(item)
        trendingRVAdapter.setTrendingList(trendingRVModelArrayList)
        idRVCurrencies.adapter = trendingRVAdapter
 //       swipeToDeleteTrending()
    }

//    private fun swipeToDelete() {
//        val swipeToDelete = object : SwipeToDelete() {
//            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                currencyRVAdapter.deleteItem(viewHolder.adapterPosition)
//            }
//        }
//        val itemTouchHelper = ItemTouchHelper(swipeToDelete)
//        itemTouchHelper.attachToRecyclerView(idRVCurrencies)
//    }

//    private fun swipeToDeleteTrending() {
//        val adapter = idRVCurrencies.adapter
//
//        if (adapter != null && adapter.itemCount > 0) {
//            val swipeToDelete = object : SwipeToDelete() {
//                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                    trendingRVAdapter.deleteItem(viewHolder.adapterPosition)
//                }
//            }
//            val itemTouchHelper = ItemTouchHelper(swipeToDelete)
//            itemTouchHelper.attachToRecyclerView(idRVCurrencies)
//        }
//    }

//    private fun swipeToRight() {
//        val adapter = idRVCurrencies.adapter
//
//        if (adapter != null && adapter.itemCount > 0) {
//            val swipetoright = object : SwipeToRight() {
//                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//
//                    currencyRVAdapter.showDialog(
//                        viewHolder.adapterPosition,
//                        this@MainActivity,
//                        currencyRVModelArrayList
//                    )
//
//
//                }
//            }
//            val itemTouchHelper = ItemTouchHelper(swipetoright)
//            itemTouchHelper.attachToRecyclerView(idRVCurrencies)
//        }
//    }


    fun showProgressBar() {
        spinkitlayout.visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        spinkitlayout.visibility = View.GONE
    }


}