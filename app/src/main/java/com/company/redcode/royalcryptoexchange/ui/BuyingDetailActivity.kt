package com.company.redcode.royalcryptoexchange.ui


import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.CompoundButton
import android.widget.TextView
import com.company.redcode.royalcryptoexchange.R
import com.company.redcode.royalcryptoexchange.TradeConfirmActivity
import com.company.redcode.royalcryptoexchange.models.Trade
import com.company.redcode.royalcryptoexchange.utils.Apputils
import com.company.redcode.royalcryptoexchange.utils.Constants
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_buying_details.*


class BuyingDetailActivity : AppCompatActivity() {

    val JSON_TRARE: String = "tradeObject"
    var trade: Trade = Trade()
    var isTermAccept:Boolean =false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buying_details)
        var obj = intent.getStringExtra(JSON_TRARE);
        trade = Gson().fromJson(obj, Trade::class.java)

        initView()
    }

    private fun initView() {

        seller_limit.setText(Apputils.formatCurrency(trade.d_limit.toString()) + "-" + Apputils.formatCurrency(trade.u_limit.toString()))
        seller_name.setText("User" + trade.uid.toString())

        pkr_ed!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!pkr_ed!!.text.toString().trim().isEmpty() && pkr_ed!!.text.toString().trim() != "") {

                    if (!pkr_ed!!.text.toString().trim().isEmpty() && pkr_ed!!.text.toString().trim() != "" && pkr_ed!!.text.trim().length > 3) {



                        if (pkr_ed!!.text.toString().toLong() > trade.u_limit.toString().toLong()) {
                            Apputils.showMsg(this@BuyingDetailActivity, "Limit crossed")
                            btn_trade.isEnabled = false
                        } else if (pkr_ed!!.text.toString().toLong() < trade.d_limit.toString().toLong()) {

                            Apputils.showMsg(this@BuyingDetailActivity, "Should exceed lower Limit ")
                            btn_trade.isEnabled = false

                        } else {
                            val amout = pkr_ed!!.text.toString().toInt()
                            val price = amout * Constants.BTC_PKR_RATE
                            btc_ed!!.setText(price.toString())
                            btn_trade.isEnabled = true

                        }

                    }
                }

            }
        })

        chk_terms.setOnCheckedChangeListener(object :CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                isTermAccept= p1;

            }

        })
        tv_terms.setOnClickListener {
            showTradeDialog()
        }

        btn_trade.setOnClickListener {
            if(!isTermAccept){
                Apputils.showMsg(this@BuyingDetailActivity , "Accept the terms ")
            }else if (!pkr_ed!!.text.toString().trim().isEmpty() && pkr_ed!!.text.toString().trim()
                    != "" &&!btc_ed!!.text.toString().trim().isEmpty() && btc_ed!!.text.toString().trim()!= ""
                            ) {
                var intent = Intent(this, TradeConfirmActivity::class.java)
                var obj = Gson().toJson(trade)
                intent.putExtra("tradeObj", obj)
                intent.putExtra("coinUsed", btc_ed.text.toString())
                intent.putExtra("priceCharged", pkr_ed.text.toString())
                startActivity(intent)
                finish()
            }
            else{
                Apputils.showMsg(this@BuyingDetailActivity , "Fill Valid data")
            }
        }
    }

    private fun showTradeDialog() {
        val view: View = LayoutInflater.from(this@BuyingDetailActivity).inflate(R.layout.dilalog_terms_trade, null)
        val alertBox = android.support.v7.app.AlertDialog.Builder(this@BuyingDetailActivity)
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
