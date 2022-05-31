package com.example.cryptotracker.activity

import android.app.Dialog
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.example.cryptotracker.R
import kotlinx.android.synthetic.main.activity_chart.*
import java.text.DecimalFormat


class ChartActivity : AppCompatActivity() {

    var name = ""
    var symbol = ""
    var interval = ""
    var percent: Double = 0.00
    var df2: DecimalFormat = DecimalFormat("#.##")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart)
         supportActionBar?.hide()

        val dialog = Dialog(this)
        name = intent.extras?.getString("coin_name").toString()
        symbol = intent.extras?.getString("sym_bol").toString()
        percent = intent.extras?.getDouble("percentChange")!!.toDouble()

        chartText.text = name
        percentchange.text = df2.format(percent) + "%"

        if (percent > 0.0){
            percentchange.setTextColor(resources.getColor(R.color.green))
        }else{
            percentchange.setTextColor(resources.getColor(R.color.red))
        }

        interval = "15"

        wallet_connect.setOnClickListener {

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.false_dialog)
            dialog.show()

            object : CountDownTimer(1000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    // TODO Auto-generated method stub
                }

                override fun onFinish() {
                    // TODO Auto-generated method stub
                    dialog.dismiss()
                }
            }.start()
        }


        one_min.setOnClickListener {
            interval = "1"
            loadChart()
        }

        min.setOnClickListener {
            interval = "15"
            loadChart()
        }
        hour.setOnClickListener {
            interval = "1H"
            loadChart()
        }
        day.setOnClickListener {
            interval = "D"
            loadChart()
        }

        loadChart()
       // walletConnect()
    }

    private fun loadChart() {
        webview.settings.javaScriptEnabled = true
        webview.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        webview.loadUrl(
            "https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol=" + symbol
                    + "USD&interval=${interval}&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg=F1F3F6&studies=[]&hideideas=1&theme=Dark&style=1&timezone=Etc%2FUTC&studies_overrides={}&overrides={}&enabled_features=[]&disabled_features=[]&locale=en&utm_source=coinmarketcap.com&utm_medium=widget&utm_campaign=chart&utm_term=${symbol}USDT"
        )
    }


    /*private fun walletConnect() {
        val config = WalletConnectKitConfig(
            context = this,
            bridgeUrl = "https://example.walletconnect.org/",
            appUrl = "https://example.walletconnect.org/",
            appName = "DApp Name",
            appDescription = "My first Android DApp!"
        )

        val walletConnectKit = WalletConnectKit.Builder(config).build()

        fun onConnected(address: String) {
            println("You are connected with account: $address")
        }

        fun onDisconnected() {
            println("Account disconnected!")
        }

        wallet_connect.start(walletConnectKit, ::onConnected, ::onDisconnected)

    }*/


}