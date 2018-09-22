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
import com.company.redcode.royalcryptoexchange.models.Order
import com.company.redcode.royalcryptoexchange.models.Trade
import com.company.redcode.royalcryptoexchange.utils.Apputils
import com.company.redcode.royalcryptoexchange.utils.Constants
import com.company.redcode.royalcryptoexchange.utils.SharedPref
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_buying_details.*


class BuyingDetailActivity : AppCompatActivity() {

    val JSON_TRARE: String = "tradeObject"
    val ORDER_TYPE: String = "orderType"
    var trade: Trade = Trade()
    var orderType: String? = null
    var order:Order = Order()
    var isTermAccept: Boolean = false
    var sharedPref = SharedPref.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buying_details)
        var obj = intent.getStringExtra(JSON_TRARE);
        orderType = intent.getStringExtra(ORDER_TYPE);
        trade = Gson().fromJson(obj, Trade::class.java)

        order.UpperLimit = trade.UpperLimit.toString()
        order.LowerLimit = trade.LowerLimit.toString()
        order.FUAC_Id = trade.FUAC_Id.toString()
        order.FUT_Id = trade.UT_Id.toString()
        order.Notify_Status = "true"
        order.PaymentMethod = "Bank"
        order.Status = "Open"

        order.ORD_UserId = sharedPref!!.getProfilePref(this@BuyingDetailActivity).UserId

        initView()

    }

    private fun initView() {

//        seller_limit.setText(Apputils.formatCurrency(trade.d_limit.toString()) + "-" + Apputils.formatCurrency(trade.u_limit.toString()))
//        seller_name.setText("User" + trade.uid.toString())
        if (orderType == "buy")
            tv_title_trade.setText("Buying trade details")
        else (orderType == "sell")
        tv_title_trade.setText("Selling trade details")
        tv_name.setText("U-" + trade.FUAC_Id)
        tv_coin_amount.setText(trade.Amount + trade.CurrencyType)
        tv_limit.setText(trade.UpperLimit.toString() + "-" + trade.LowerLimit.toString())

        pkr_ed!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!pkr_ed!!.text.toString().trim().isEmpty() && pkr_ed!!.text.toString().trim() != "") {
                    if ( pkr_ed!!.text.trim().length > 3) {
                        //btc_ed.setText()
                        var price = trade.UpperLimit.toString().toLong()
                        if(pkr_ed!!.text.toString().toLong() > price){

                            //Apputils.showMsg(this@BuyingDetailActivity, " Limit ")
                            ecuurency_ed.setText((trade.Price!!.toDouble()/pkr_ed.text.toString().toDouble()).toString())

                        }
                        else{

                        }

                    }
                }
            }
        })

        chk_terms.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                isTermAccept = p1;

            }

        })
        tv_terms.setOnClickListener {
            showTradeDialog()
        }

        btn_trade.setOnClickListener {
            validation()
        }
    }

    private fun validation() {

        if (!pkr_ed!!.text.toString().trim().isEmpty() && pkr_ed!!.text.toString().trim() != "") {

            if (pkr_ed!!.text.trim().length > 3) {

                if (pkr_ed!!.text.toString().toLong() > trade.UpperLimit.toString().toLong()) {
                    Apputils.showMsg(this@BuyingDetailActivity, "Limit crossed")
                    return
                } else if (pkr_ed!!.text.toString().toLong() < trade.LowerLimit.toString().toLong()) {

                    Apputils.showMsg(this@BuyingDetailActivity, "Should exceed lower Limit ")
                    return

                }

            }
        }
        if (isTermAccept) {
//            order.Amount =
            var intent = Intent(this, TradeConfirmActivity::class.java)
            var obj = Gson().toJson(trade)
            intent.putExtra("tradeObj", obj)
            intent.putExtra("coinUsed", ecuurency_ed.text.toString())
            intent.putExtra("priceCharged", pkr_ed.text.toString())
            startActivity(intent)
            finish()
        } else {
            Apputils.showMsg(this@BuyingDetailActivity, "Fill Valid data")
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
//        terms_tv.setText(trade.terms.toString())

        btnSave.setOnClickListener {
            dialog.dismiss()
        }


        dialog.show()
    }

}
