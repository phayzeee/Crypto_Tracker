package com.example.cryptotracker.activity


import android.content.Intent
import android.os.Bundle
import android.text.Layout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptotracker.*
import com.example.cryptotracker.abstract.SwipeToDelete
import com.example.cryptotracker.abstract.SwipeToRight
import com.example.cryptotracker.adapter.CurrencyRVAdapter
import com.example.cryptotracker.apiresponse.CryptoApiResponse
import com.example.cryptotracker.apiresponse.CryptoCurrency
import com.example.cryptotracker.network.RetrofitInstance
import com.example.cryptotracker.network.apiService
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    var currencyRVModelArrayList = ArrayList<CryptoCurrency>()
    lateinit var currencyRVAdapter: CurrencyRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        setSupportActionBar(findViewById(R.id.my_toolbar))
        supportActionBar?.hide()

        currencyRVAdapter = CurrencyRVAdapter()
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

    fun setupUI(cryptoCurrency: List<CryptoCurrency>){
        currencyRVModelArrayList.clear()
        currencyRVModelArrayList.addAll(cryptoCurrency)
        currencyRVAdapter.setCurrencyList(currencyRVModelArrayList)
        idRVCurrencies.adapter = currencyRVAdapter
        swipeToDelete()

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