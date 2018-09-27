package com.company.redcode.royalcryptoexchange

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import com.company.redcode.royalcryptoexchange.models.Order
import com.company.redcode.royalcryptoexchange.models.OrderTerms
import com.company.redcode.royalcryptoexchange.models.Response
import com.company.redcode.royalcryptoexchange.retrofit.ApiClint
import com.company.redcode.royalcryptoexchange.ui.DisputeActivity
import com.company.redcode.royalcryptoexchange.utils.Apputils
import com.company.redcode.royalcryptoexchange.utils.Constants
import com.company.redcode.royalcryptoexchange.utils.ServiceError
import com.company.redcode.royalcryptoexchange.utils.ServiceListener
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
        var orderObj = intent.getStringExtra("order")
        order = Gson().fromJson(orderObj, Order::class.java)

        initView()

        val builder = AlertDialog.Builder(this@OrderDetailActivity)
        builder.setView(R.layout.layout_dialog_progress)
        builder.setCancelable(false)
        progressBar = builder.create()


        if (order.Status == Constants.STATUS_CANCEL) {
            status_tv!!.text = "cancelled"
        } else
            status_tv!!.text = order.Status

        if (order.Status == Constants.STATUS_OPEN) {
            btn_paid.visibility = View.VISIBLE
            btn_later.visibility = View.VISIBLE
            btn_dispute.visibility = View.VISIBLE


        } else if (order.Status == Constants.STATUS_DISPUTE) {
            btn_paid.visibility = View.GONE
            btn_later.visibility = View.GONE
            btn_dispute.visibility = View.GONE

        } else if (order.Status == Constants.STATUS_IN_PROGRESS) {
            btn_paid.visibility = View.GONE
            btn_later.visibility = View.GONE
            btn_dispute.visibility = View.VISIBLE
        } else if (order.Status == Constants.STATUS_COMPLETED) {
            btn_paid.visibility = View.GONE
            btn_later.visibility = View.GONE
            btn_dispute.visibility = View.GONE
        } else if (order.Status == Constants.STATUS_CANCEL) {
            btn_paid.visibility = View.GONE
            btn_later.visibility = View.GONE
            btn_dispute.visibility = View.GONE

        }

        getPayementId(object :ServiceListener<String>{
            override fun success(obj: String) {
                getTerm(obj)
            }
            override fun fail(error: ServiceError) {}
        })

    }

    fun getTerm(pid: String) {
        progressBar!!.show()
        ApiClint.getInstance()?.getService()?.gettermAndPayment(order.User_Id.toString()!!, pid)?.enqueue(object : Callback<OrderTerms> {
            override fun onFailure(call: Call<OrderTerms>?, t: Throwable?) {
                progressBar!!.dismiss()
            }

            override fun onResponse(call: Call<OrderTerms>?, response: retrofit2.Response<OrderTerms>?) {
                progressBar!!.dismiss()
                if (response != null) {
                    orderTerms = response.body()!!

                    if (orderTerms.PaymentMethod!!.Type == "Bank")
                        tv_terms.text = "Type: "+ orderTerms.PaymentMethod!!.Type+ "\nCode:" + orderTerms!!.PaymentMethod!!.BankCode
                    else {
                        tv_terms.setText("Type: "+ orderTerms.PaymentMethod!!.Type+"\n Number:"+orderTerms.PaymentMethod!!.BankName)
                    }

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

        btn_later.setOnClickListener {
            finish()
        }
        btn_back.setOnClickListener {
            finish()
        }
        btn_paid.setOnClickListener {
            updateStatus(Constants.STATUS_IN_PROGRESS, order.ORD_Id!!)
        }
        btn_dispute.setOnClickListener {
            var intent = Intent(this@OrderDetailActivity, DisputeActivity::class.java)
            var obj = Gson().toJson(order)
            intent.putExtra("order", obj)
            startActivityForResult(intent, 44)
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

    fun updateStatus(status: String, order_id: String) {
        progressBar!!.show()
        ApiClint.getInstance()?.getService()?.update_order_status(order_id, status)!!.enqueue(object : Callback<com.company.redcode.royalcryptoexchange.models.Response> {
            override fun onFailure(call: Call<com.company.redcode.royalcryptoexchange.models.Response>?, t: Throwable?) {
                print("error " + t)
                progressBar!!.dismiss()
            }
            override fun onResponse(call: Call<com.company.redcode.royalcryptoexchange.models.Response>?, response: retrofit2.Response<Response>?) {
                if (response?.body() != null) {
                    setResult(RESULT_OK);
                    finish()
                    progressBar!!.dismiss()
                }
            }

        })
    }

    fun getPayementId(serviceListener: ServiceListener<String>) {
        progressBar!!.show()
        ApiClint.getInstance()?.getService()?.getUserPaymentId(order.FUT_Id!!)!!.enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>?, t: Throwable?) {
                println("ERROR")
                progressBar!!.dismiss()
                Apputils.showMsg(this@OrderDetailActivity, "Network error")
            }

            override fun onResponse(call: Call<String>?, response: retrofit2.Response<String>?) {
                progressBar!!.dismiss()
                if (response != null) {
                    if (response!!.body() != "0") {
                        serviceListener.success(response.body()!!)
                    }
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            setResult(Activity.RESULT_OK)
            finish()
        }
    }
}
