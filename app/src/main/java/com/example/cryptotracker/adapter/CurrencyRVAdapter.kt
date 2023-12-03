package com.example.cryptotracker.adapter

import android.app.Dialog
import android.content.Context
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptotracker.R
import com.example.cryptotracker.apiresponse.TopCoinsResponse.CryptoCurrency
import kotlinx.android.synthetic.main.currency_rv_item.view.*
import java.text.DecimalFormat

class CurrencyRVAdapter: RecyclerView.Adapter<CurrencyRVAdapter.ViewHolder>() {

    private lateinit var currencyRvModalArrayList: ArrayList<CryptoCurrency>
    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    fun setCurrencyList( currencyRvModalArrayList: ArrayList<CryptoCurrency>){
        this.currencyRvModalArrayList = currencyRvModalArrayList
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.currency_rv_item, parent, false)
        return ViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currencyRvModalArrayList[position])

    }

    override fun getItemCount(): Int {
        return currencyRvModalArrayList.size
    }



    class ViewHolder(view: View, listener: onItemClickListener): RecyclerView.ViewHolder(view) {
        var currencyNameTv: TextView = view.idTVCurrencyName
        var symbolTv: TextView = view.idTVSymbol
        var rateTv: TextView = view.idTVCurrencyRate
        var df2: DecimalFormat = DecimalFormat("#.##")

        init {
            view.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
        }

        fun bind(data: CryptoCurrency){
            currencyNameTv.text = data.name
            symbolTv.text = data.symbol
            rateTv.text = "$ " + df2.format(data.quotes[0].price)
        }
    }

    fun deleteItem(position: Int){
        currencyRvModalArrayList.removeAt(position)
        notifyDataSetChanged()
    }

    fun showDialog(position: Int, context: Context, currencyRvModalArrayList: ArrayList<CryptoCurrency>) {

//        if(currencyRvModalArrayList.size == 0) {
//            Toast.makeText(context,"Error", Toast.LENGTH_SHORT).show()
//        } else {
            val dialogView =
                LayoutInflater.from(context).inflate(R.layout.audit_report_dialog, null)
            val dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(dialogView)
            dialog.show()

            var abc = dialogView.findViewById<TextView>(R.id.idTVAudit)

            var auditReport: Boolean = currencyRvModalArrayList[position].isAudited
            if (auditReport) {
                abc.text = "Audit : Approved"
            } else {
                abc.text = "Audit : Unapproved"
            }

            object : CountDownTimer(3000, 3000) {
                override fun onTick(millisUntilFinished: Long) {
                    // TODO Auto-generated method stub
                }

                override fun onFinish() {
                    // TODO Auto-generated method stub
                    dialog.dismiss()
                }
            }.start()

            notifyItemChanged(position)
//        }
    }


}