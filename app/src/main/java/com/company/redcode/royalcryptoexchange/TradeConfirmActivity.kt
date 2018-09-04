package com.company.redcode.royalcryptoexchange

import android.os.Bundle
import android.os.CountDownTimer
import android.support.v7.app.AppCompatActivity
import com.company.redcode.royalcryptoexchange.models.Trade
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_trade_confirm.*


class TradeConfirmActivity : AppCompatActivity() {

    var trade = Trade()
    var coinUsed: String? = null
    var price: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trade_confirm)

        var obj = intent.getStringExtra("tradeObj")
        trade = Gson().fromJson(obj, Trade::class.java)
        coinUsed = intent.getStringExtra("coinUsed")
        price = intent.getStringExtra("priceCharged")
        initView()


    }

    private fun initView() {
        var deadline = System.currentTimeMillis() + trade.dead_line!!.toLong()
        println("current deadline "+deadline)
        var time = deadline-System.currentTimeMillis()

        println("time now " + time)

        seller_name.setText("user"+trade.uid)
        btc_amount.setText(coinUsed)
        price_tv.setText(price)
        val countDown = object : CountDownTimer(time, 1000) {

            override fun onTick(millisUntilFinished: Long) {

                timer_tv.setText(" " + formatMilliSecondsToTime(millisUntilFinished));
            }

            override fun onFinish() {}
        }
        countDown.start()
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
