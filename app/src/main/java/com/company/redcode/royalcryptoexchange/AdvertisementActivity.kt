package com.company.redcode.royalcryptoexchange

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import com.company.redcode.royalcryptoexchange.utils.Apputils
import com.company.redcode.royalcryptoexchange.utils.Constants
import kotlinx.android.synthetic.main.activity_advertisement.*

class AdvertisementActivity : AppCompatActivity() {

    var coin: String = "BTC"
    var orderType: String = "Buy"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_advertisement)
        initView()
    }

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
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!ed_amount!!.text.toString().trim().isEmpty() && ed_amount!!.text.toString().trim() != "") {

                    val amount = ed_amount!!.text.toString().toDouble()
                    val remCoin: Double = getCoinAfterFee(amount)

                    tv_fees!!.setText((amount * 4 / 100).toString())
                    tv_total!!.setText(remCoin.toString())

                }
            }
        })
    }


    fun validation(){

        if (!ed_amount!!.text.toString().trim().isEmpty() && ed_amount!!.text.toString().trim() != "" &&
                !ed_price!!.text.toString().trim().isEmpty() && ed_price!!.text.toString().trim() != "") {

            var amount = ed_amount!!.text.toString().toDouble()
            var price = ed_price!!.text.toString().toDouble()
            var total = amount * price
            if (total < Constants.TRADE_LIMIT_AMOUNT){
                Apputils.showMsg(this@AdvertisementActivity, "Minimum 5000 transaction allowed ")
                return
            }
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
    }

    fun getCoinAfterFee(coinNum: Double): Double {

        val feeAmount = 4
        var fees: Double = coinNum * feeAmount / 100
        var coinRem: Double = coinNum - fees

        return coinRem
    }
}
