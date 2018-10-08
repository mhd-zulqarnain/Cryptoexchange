package com.company.redcode.royalcryptoexchange

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.AttributeSet
import android.view.View
import com.company.redcode.royalcryptoexchange.auth.SignInActivity
import com.company.redcode.royalcryptoexchange.models.Order
import com.company.redcode.royalcryptoexchange.models.OrderTerms
import com.company.redcode.royalcryptoexchange.models.Response
import com.company.redcode.royalcryptoexchange.retrofit.ApiClint
import com.company.redcode.royalcryptoexchange.ui.DisputeActivity
import com.company.redcode.royalcryptoexchange.utils.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_trade_confirm.*
import retrofit2.Call
import retrofit2.Callback
import java.text.SimpleDateFormat
import java.util.*


class OrderDetailActivity : AppCompatActivity() {

    //    var price: String? = null
    var order: Order = Order()
    var progressBar: AlertDialog? = null
    var orderTerms: OrderTerms = OrderTerms()
    var toolbar: Toolbar? = null
    private var pref: SharedPref = SharedPref.getInstance()!!
    var serviceRequest:String = ""

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trade_confirm)
        toolbar = findViewById(R.id.toolbar_top)
        var orderObj = ""

        val builder = AlertDialog.Builder(this@OrderDetailActivity)
        builder.setView(R.layout.layout_dialog_progress)
        builder.setCancelable(false)
        progressBar = builder.create()

        var intentType = intent.getStringExtra("type")
        if (intentType == "activity") {
            orderObj = intent.getStringExtra("order")
            order = Gson().fromJson(orderObj, Order::class.java)
            initView()
        } else if (intentType == "service") {
            var orderId: String = intent.getStringExtra("orderId")
            serviceRequest = intent.getStringExtra("request")
            var userId = pref.getProfilePref(this@OrderDetailActivity).UAC_Id
            if (userId == null) {
                val intent = Intent(this@OrderDetailActivity, SignInActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                getOrder(orderId, object : ServiceListener<Order> {
                    override fun success(obj: Order) {
                        order = obj
                        initView()

                    }

                    override fun fail(error: ServiceError) {
                    }

                })
            }
        }

    }

    fun getTerm(pid: String) {
        progressBar!!.show()
        var ownerId = order.User_Id.toString().substring(2)
        ApiClint.getInstance()?.getService()?.gettermAndPayment(ownerId, pid)?.enqueue(object : Callback<OrderTerms> {
            override fun onFailure(call: Call<OrderTerms>?, t: Throwable?) {
                progressBar!!.dismiss()
            }

            override fun onResponse(call: Call<OrderTerms>?, response: retrofit2.Response<OrderTerms>?) {
                progressBar!!.dismiss()
                if (response != null) {
                    orderTerms = response.body()!!

                    if (orderTerms.PaymentMethod!!.Type == "Bank")
                        tv_terms.text = "Type: " + orderTerms.PaymentMethod!!.Type + "\nCode:" + orderTerms.PaymentMethod!!.BankCode
                    else {
                        tv_terms.setText("Type: " + orderTerms.PaymentMethod!!.Type + "\n Number:" + orderTerms.PaymentMethod!!.BankName)
                    }

                }
            }
        })

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun initView() {

        if(serviceRequest!=""){
            serviceView()
        }
        else
            intentView()

        getPayementId(object : ServiceListener<String> {
            override fun success(obj: String) {
                getTerm(obj)
            }

            override fun fail(error: ServiceError) {}
        })


        var deadline = Apputils.getTimeStamp(order.Expire.toString())?.toLong();
        val publishedDate = Apputils.getTimeStamp(order.Order_Date.toString())?.toLong();
        println("current deadline " + deadline)

        var currentTime = System.currentTimeMillis()
        var time = deadline?.minus(currentTime)

        println("time now " + time)

        dealer_name.setText(order.User_Id)
        btc_amount.setText(order.BitAmount)
        price_tv.setText(order.BitPrice + "PKR")
        if (order.Status == "dispute") {
            timer_tv.text = "Disputed Order"
        } else if (order.Status == "Cancelled") {
            timer_tv.text = "Order Cancelled"
        } else {
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
        }

        btn_later.setOnClickListener {
            finish()
        }
        btn_back.setOnClickListener {
            finish()
        }
        btn_paid.setOnClickListener {

            // updateStatus(Constants.STATUS_IN_PROGRESS, order.ORD_Id!!)

            var intent = Intent(this@OrderDetailActivity, DisputeActivity::class.java)
            var obj = Gson().toJson(order)
            intent.putExtra("order", obj)
            intent.putExtra("activity", "paid")
            startActivityForResult(intent, 44)

        }
        btn_cancel.setOnClickListener {

            // updateStatus(Constants.STATUS_IN_PROGRESS, order.ORD_Id!!)

            var intent = Intent(this@OrderDetailActivity, DisputeActivity::class.java)
            var obj = Gson().toJson(order)
            intent.putExtra("order", obj)
            intent.putExtra("activity", "cancel")
            startActivityForResult(intent, 44)

        }
        btn_dispute.setOnClickListener {
            var intent = Intent(this@OrderDetailActivity, DisputeActivity::class.java)
            var obj = Gson().toJson(order)
            intent.putExtra("order", obj)
            intent.putExtra("activity", "dispute")
            startActivityForResult(intent, 44)
        }
    }

    private fun serviceView() {
        if (serviceRequest == Constants.STATUS_CANCEL) {
            status_tv!!.text = "Canceled"
        } else
            status_tv!!.text = order.Status

        if (serviceRequest == "order") {
            btn_paid.visibility = View.GONE
            btn_later.visibility = View.GONE
            btn_dispute.visibility = View.VISIBLE
            btn_cancel.visibility = View.VISIBLE


        } else if (serviceRequest== Constants.STATUS_DISPUTE) {
            btn_paid.visibility = View.GONE
            btn_later.visibility = View.GONE
            btn_dispute.visibility = View.GONE
            btn_cancel.visibility = View.GONE

        } else if (serviceRequest == "paid") {
            btn_paid.visibility = View.GONE
            btn_later.visibility = View.GONE
            btn_dispute.visibility = View.VISIBLE
            btn_cancel.visibility = View.GONE
        }  else if (serviceRequest == "dispute") {
            btn_paid.visibility = View.GONE
            btn_later.visibility = View.GONE
            btn_dispute.visibility = View.GONE
            btn_cancel.visibility = View.GONE
        } else if (serviceRequest == "release") {
            btn_paid.visibility = View.GONE
            btn_later.visibility = View.GONE
            btn_dispute.visibility = View.GONE
            btn_cancel.visibility = View.GONE
        } else if (order.Status == Constants.STATUS_COMPLETED) {
            btn_paid.visibility = View.GONE
            btn_later.visibility = View.GONE
            btn_dispute.visibility = View.GONE
            btn_cancel.visibility = View.GONE
        } else if (order.Status == Constants.STATUS_CANCEL) {
            btn_paid.visibility = View.GONE
            btn_later.visibility = View.GONE
            btn_dispute.visibility = View.GONE
            btn_cancel.visibility = View.GONE

        }
    }
    private fun intentView() {
        if (order.Status == Constants.STATUS_CANCEL) {
            status_tv!!.text = "Cancelled"
        } else
            status_tv!!.text = order.Status

        if (order.Status == Constants.STATUS_OPEN) {
            btn_paid.visibility = View.VISIBLE
            btn_later.visibility = View.VISIBLE
            btn_dispute.visibility = View.VISIBLE
            btn_cancel.visibility = View.VISIBLE


        } else if (order.Status == Constants.STATUS_DISPUTE) {
            btn_paid.visibility = View.GONE
            btn_later.visibility = View.GONE
            btn_dispute.visibility = View.GONE
            btn_cancel.visibility = View.GONE

        } else if (order.Status == Constants.STATUS_IN_PROGRESS) {
            btn_paid.visibility = View.GONE
            btn_later.visibility = View.GONE
            btn_dispute.visibility = View.VISIBLE
            btn_cancel.visibility = View.GONE
        } else if (order.Status == Constants.STATUS_COMPLETED) {
            btn_paid.visibility = View.GONE
            btn_later.visibility = View.GONE
            btn_dispute.visibility = View.GONE
            btn_cancel.visibility = View.GONE
        } else if (order.Status == Constants.STATUS_CANCEL) {
            btn_paid.visibility = View.GONE
            btn_later.visibility = View.GONE
            btn_dispute.visibility = View.GONE
            btn_cancel.visibility = View.GONE

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

    fun getOrder(order_id: String, serviceListener: ServiceListener<Order>) {
        progressBar!!.show()
        ApiClint.getInstance()?.getService()?.getSingleOrderById(order_id!!)!!.enqueue(object : Callback<Order> {
            override fun onFailure(call: Call<Order>?, t: Throwable?) {
                println("ERROR")
                progressBar!!.dismiss()
                serviceListener.fail(ServiceError("failed"))
                Apputils.showMsg(this@OrderDetailActivity, "Network error")
            }

            override fun onResponse(call: Call<Order>?, response: retrofit2.Response<Order>?) {
                progressBar!!.dismiss()
                if (response != null) {
                    var order: Order = response.body()!!
                    if (order.Status != "") {
                        serviceListener.success(order)
                        progressBar!!.dismiss()

                    }

                }
                progressBar!!.dismiss()

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
