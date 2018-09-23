package com.company.redcode.royalcryptoexchange

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.company.redcode.royalcryptoexchange.R.id.tv_terms
import com.company.redcode.royalcryptoexchange.models.Order
import com.company.redcode.royalcryptoexchange.models.OrderTerms
import com.company.redcode.royalcryptoexchange.models.Trade
import com.company.redcode.royalcryptoexchange.retrofit.ApiClint
import com.company.redcode.royalcryptoexchange.utils.Apputils
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_trade_confirm.*
import retrofit2.Call
import retrofit2.Callback


class OrderDetailActivity : AppCompatActivity() {

    //    var price: String? = null
    var order: Order = Order()
    var progressBar: AlertDialog? = null
    var orderTerms: OrderTerms = OrderTerms()
    var toolbar: Toolbar? = null

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trade_confirm)
        toolbar = findViewById(R.id.toolbar_top)
        /*  var obj = intent.getStringExtra("tradeObj")*/
        var orderObj = intent.getStringExtra("order")

        order = Gson().fromJson(orderObj, Order::class.java)

//        price = intent.getStringExtra("priceCharged")
        initView()
//        order.Price = price

        val builder = AlertDialog.Builder(this@OrderDetailActivity)
        builder.setView(R.layout.layout_dialog_progress)
        builder.setCancelable(false)
        progressBar = builder.create()
        getTerm()

        btn_back.setOnClickListener {
            finish()
        }


    }

    fun getTerm() {
        progressBar!!.show()

        ApiClint.getInstance()?.getService()?.gettermAndPayment(order.User_Id.toString()!!, "3")?.enqueue(object : Callback<OrderTerms> {
            override fun onFailure(call: Call<OrderTerms>?, t: Throwable?) {
                progressBar!!.dismiss()
            }

            override fun onResponse(call: Call<OrderTerms>?, response: retrofit2.Response<OrderTerms>?) {
                progressBar!!.dismiss()
                if (response != null) {
                    orderTerms = response.body()!!
                    tv_terms.setText("Method:" + orderTerms.PaymentMethod!!.BankName)
                    if (orderTerms.PaymentMethod!!.Type == "Bank")
                        tv_terms.text = tv_terms.text.toString() + "\nCode:" + orderTerms!!.PaymentMethod!!.BankCode
                }
            }
        })

    }


    private fun initView() {
        var deadline = Apputils.getTimeStamp(order.Expire.toString())?.toLong();
        val publishedDate = Apputils.getTimeStamp(order.Order_Date.toString())?.toLong();
        println("current deadline " + deadline)
        var currentTime = System.currentTimeMillis()
        var time = deadline?.minus(currentTime)

        println("time now " + time)

        dealer_name.setText("U-" + order.User_Id)
        btc_amount.setText(order.BitAmount)
        price_tv.setText(order.Price + "PKR")
        if (currentTime > deadline!!) {
            timer_tv.setText("Expired")
        } else {
            val countDown = object : CountDownTimer(time!!, 1000) {

                override fun onTick(millisUntilFinished: Long) {

                    timer_tv.setText(" " + formatMilliSecondsToTime(millisUntilFinished));
                }

                override fun onFinish() {}
            }
            countDown.start()


        }

        btn_later.setOnClickListener{
            finish()
        }
    }

    private fun formatMilliSecondsToTime(milliseconds: Long): String {

        val seconds = (milliseconds / 1000).toInt() % 60
        val minutes = (milliseconds / (1000 * 60) % 60).toInt()
        val hours = (milliseconds / (1000 * 60 * 60) % 24).toInt()
        return (twoDigitString(hours.toLong()) + " : " + twoDigitString(minutes.toLong()) + " : "
                + twoDigitString(seconds.toLong()))
    }

    private fun twoDigitString(number: Long): String {

        if (number == 0L) {
            return "00"
        }

        return if (number / 10 == 0L) {
            "0$number"
        } else number.toString()

    }
}
