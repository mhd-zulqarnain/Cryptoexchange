package com.company.redcode.royalcryptoexchange.ui


import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import com.company.redcode.royalcryptoexchange.R
import com.company.redcode.royalcryptoexchange.models.Trade
import com.company.redcode.royalcryptoexchange.utils.Apputils
import com.company.redcode.royalcryptoexchange.utils.Constants
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_buy.*


class BuyActivity : AppCompatActivity() {

    val JSON_TRARE: String = "tradeObject"
    var trade: Trade = Trade()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buy)
        var obj = intent.getStringExtra(JSON_TRARE);
        trade = Gson().fromJson(obj, Trade::class.java)


        initView()
    }

    private fun initView() {

        seller_limit.setText(trade.d_limit.toString() + "-" + trade.u_limit.toString())
        seller_name.setText("User" + trade.uid.toString())

        pkr_ed!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!pkr_ed!!.text.toString().trim().isEmpty() && pkr_ed!!.text.toString().trim() != "") {

                    if (!pkr_ed!!.text.toString().trim().isEmpty() && pkr_ed!!.text.toString().trim() != "" && pkr_ed!!.text.trim().length > 3) {

                        val amout = pkr_ed!!.text.toString().toInt()

                        val price = amout * Constants.BTC_PKR_RATE

                        if (pkr_ed!!.text.toString().toLong() > trade.u_limit.toString().toLong()) {
                            Apputils.showMsg(this@BuyActivity, "Limit crossed")
                        } else if (pkr_ed!!.text.toString().toLong() < trade.d_limit.toString().toLong()) {

                            Apputils.showMsg(this@BuyActivity, "Should exceed lower Limit ")

                        }

                        btc_ed!!.setText(price.toString())

                    }
                }

            }
        })

        tv_terms.setOnClickListener {
            showTradeDialog()

        }
    }


    private fun showTradeDialog() {
        val view: View = LayoutInflater.from(this@BuyActivity).inflate(R.layout.dilalog_terms_trade, null)
        val alertBox = android.support.v7.app.AlertDialog.Builder(this@BuyActivity)
        alertBox.setView(view)
        alertBox.setCancelable(false)
        val dialog = alertBox.create()

        val btnSave: Button = view.findViewById(R.id.btn_save)
        val terms_tv: TextView = view.findViewById(R.id.terms_tv)
        terms_tv.setText(trade.terms.toString())

        btnSave.setOnClickListener {
            dialog.dismiss()
        }


        dialog.show()
    }

}
