package com.company.redcode.royalcryptoexchange

import android.annotation.TargetApi
import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.company.redcode.royalcryptoexchange.models.ApiResponse
import com.company.redcode.royalcryptoexchange.models.Trade
import com.company.redcode.royalcryptoexchange.models.Users
import com.company.redcode.royalcryptoexchange.retrofit.ApiClint
import com.company.redcode.royalcryptoexchange.ui.BuyActivity
import com.company.redcode.royalcryptoexchange.ui.SellActivity
import com.company.redcode.royalcryptoexchange.utils.Apputils
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_advertisement.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdvertisementActivity : AppCompatActivity() {

    var coin: String = "BTC"
    var orderType: String = "Buy"
    var progressBar: AlertDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_advertisement)
        initView()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun initView() {

        spinner_coin_type.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                var item = parent!!.getItemAtPosition(pos);
                coin = item as String
            }
        })
        spinner_order_type!!.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                var item = parent!!.getItemAtPosition(pos);
                orderType = item as String
            }
        })

        ed_amount!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (!ed_amount!!.text.toString().trim().isEmpty() && ed_amount!!.text.toString().trim() != "") {
                    try {

                        val amount = ed_amount!!.text.toString().toDouble()
                        val remCoin: Double = getCoinAfterFee(amount)

                        tv_fees!!.setText((amount * 4 / 100).toString())
                        tv_total!!.setText(remCoin.toString())
                    } catch (e: NumberFormatException) {
                        println("error")
                    }

                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })
        btn_post.setOnClickListener{
            validation()
        }
        btn_cancel.setOnClickListener {
            finish()
        }
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun validation() {

        if (!ed_amount!!.text.toString().trim().isEmpty() && ed_amount!!.text.toString().trim() != "" &&
                !ed_price!!.text.toString().trim().isEmpty() && ed_price!!.text.toString().trim() != "") {

            var amount = ed_amount!!.text.toString().toDouble()
            var price = ed_price!!.text.toString().toDouble()
            var total = amount * price

        }
        if (u_limit!!.text.toString() == "") {
            u_limit!!.error = "Enter the Limit"
            u_limit!!.requestFocus()
            return

        }
        if (l_limit!!.text.toString() == "") {
            l_limit!!.error = "Enter the Limit"
            l_limit!!.requestFocus()
            return
        }


        if (u_limit!!.text.trim().length < 4) {
            u_limit!!.error = "It should be in four figures"
            u_limit!!.requestFocus()
            return

        }

        if (l_limit!!.text.trim().length < 4) {
            l_limit!!.error = "It should be in four figures"
            l_limit!!.requestFocus()
            return

        }
        if (l_limit!!.text.toString().toDouble() > u_limit!!.text.toString().toDouble()) {
            Apputils.showMsg(this@AdvertisementActivity, "Upper limit should  be greater")
            return

        }

        postNewTrade()
    }

    fun getCoinAfterFee(coinNum: Double): Double {

        val feeAmount = 4
        var fees: Double = coinNum * feeAmount / 100
        var coinRem: Double = coinNum - fees

        return coinRem
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun postNewTrade() {

        val builder = AlertDialog.Builder(this@AdvertisementActivity!!)
        builder.setView(R.layout.layout_dialog_progress)
        builder.setCancelable(false)
        progressBar = builder.create()

        progressBar!!.show()
        var mtrade = Trade(null, "322", Users("user3322"), "bankid", u_limit?.text.toString().toLong(), l_limit?.text.toString().toLong()
                , null, coin, ed_amount!!.text.toString(), ed_price!!.text.toString(), "", type = orderType)

        println(Gson().toJson(mtrade))
        ApiClint.getInstance()?.getService()?.addTrade(trade = mtrade)?.enqueue(object : Callback<ApiResponse> {

            override fun onResponse(call: Call<ApiResponse>?, response: Response<ApiResponse>?) {
                println("error  " + response.toString())
                Toast.makeText(this@AdvertisementActivity!!, "Deal added successfully ", Toast.LENGTH_LONG).show()
                progressBar!!.dismiss()
                if(orderType.equals("Buy"))
                {
                    startActivity(Intent(this@AdvertisementActivity, BuyActivity::class.java))
                    finish()
                }
                if(orderType.equals("Sell")){

                    startActivity(Intent(this@AdvertisementActivity, SellActivity::class.java))
                    finish()
                }

                finish()
//                dialog.dismiss()
//                getAllTrade()

            }

            override fun onFailure(call: Call<ApiResponse>?, t: Throwable?) {
                Toast.makeText(this@AdvertisementActivity!!, "failed ", Toast.LENGTH_LONG).show()
                println("error  " + t.toString())
                progressBar!!.dismiss()
//                dialog.dismiss()

            }

        })

    }
}
