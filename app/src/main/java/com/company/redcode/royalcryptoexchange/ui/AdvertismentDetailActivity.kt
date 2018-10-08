package com.company.redcode.royalcryptoexchange.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.company.redcode.royalcryptoexchange.R
import com.company.redcode.royalcryptoexchange.adapter.OrderAdapater
import com.company.redcode.royalcryptoexchange.models.Order
import com.company.redcode.royalcryptoexchange.models.Trade
import com.company.redcode.royalcryptoexchange.retrofit.ApiClint
import com.company.redcode.royalcryptoexchange.utils.Apputils
import com.company.redcode.royalcryptoexchange.utils.Constants
import com.company.redcode.royalcryptoexchange.utils.SharedPref
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_advertisment_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdvertismentDetailActivity : AppCompatActivity() {

    var set_message : TextView? = null
    var order_recycler: RecyclerView? = null
    var orderAdapater: OrderAdapater? = null
    var progressBar: AlertDialog? = null
    var orderList: ArrayList<Order> = ArrayList()
    var sharedPref: SharedPref = SharedPref.getInstance()!!
    var trade: Trade = Trade()
    var toolbar: Toolbar? = null

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_advertisment_detail)
        val builder = AlertDialog.Builder(this@AdvertismentDetailActivity)
        builder.setView(R.layout.layout_dialog_progress)
        builder.setCancelable(false)
        var obj = intent.getStringExtra("trade");
        trade = Gson().fromJson(obj, Trade::class.java)
        toolbar = findViewById(R.id.toolbar_top)

        progressBar = builder.create()
        initView()
        btn_back.setOnClickListener {
            finish()
        }
        btn_delete.setOnClickListener {
            deletTrade()
        }
    }

    private fun initView() {
        set_message = findViewById(R.id.adv_message)
       // tv_coin_type.text =  "Ecurrency:" + trade.CurrencyType
        tv_details.text = "Amount:" + trade.Amount + "\n" + "Price:" + trade.Price + "\n" + "Type:" + trade.OrderType +'\n'+"Ecurrency:" + trade.CurrencyType+" "
        order_recycler = findViewById(R.id.order_recycler)
        val orderlayout = LinearLayoutManager(this@AdvertismentDetailActivity, LinearLayout.VERTICAL, false)
        order_recycler!!.layoutManager = orderlayout
        orderAdapater = OrderAdapater(this@AdvertismentDetailActivity, orderList) { post ->
            showOrderReleaseDialog(orderList[post])
        }
        order_recycler!!.adapter = orderAdapater

        getOrderList()

    }

    private fun deletTrade() {
        progressBar!!.show()
        ApiClint.getInstance()?.getService()?.delete_trade(trade.UT_Id.toString())!!.enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>?, t: Throwable?) {
                print("error")
                progressBar!!.hide()
            }

            override fun onResponse(call: Call<String>?, response: Response<String>?) {
                if (response != null) {
                    if (response.body() == Constants.STATUS_SUCCESS) {
                        Apputils.showMsg(this@AdvertismentDetailActivity, "Trade removed ")
                        setResult(Activity.RESULT_OK)
                        finish()
                    } else {
                        Apputils.showMsg(this@AdvertismentDetailActivity, "Failed orders are in progress ")

                    }
                }
                progressBar!!.hide()
            }
        })

    }

    private fun showOrderReleaseDialog(order: Order) {

        val view: View = LayoutInflater.from(this@AdvertismentDetailActivity).inflate(R.layout.dialog_order_release, null)
        val alertBox = android.support.v7.app.AlertDialog.Builder(this@AdvertismentDetailActivity)
        alertBox.setView(view)
        val dialog = alertBox.create()

        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        val btnClose: ImageView = view.findViewById(R.id.btn_close)
        val tv_name: TextView = view.findViewById(R.id.tv_name)
        val tv_coin_amount: TextView = view.findViewById(R.id.tv_coin_amount)
        val tv_price: TextView = view.findViewById(R.id.tv_price)
        val btn_release: Button = view.findViewById(R.id.btn_release)
        val btn_cancel: Button = view.findViewById(R.id.btn_cancel)
        val btn_dispute: Button = view.findViewById(R.id.btn_dispute)
        val tv_status: TextView = view.findViewById(R.id.tv_status)

        if (order.Status == Constants.STATUS_CANCEL) {
            tv_status.text = "Cancelled"
        } else
            tv_status.text = order.Status

        if (order.Status == Constants.STATUS_OPEN) {
            btn_release.visibility = View.GONE
        } else if (order.Status == Constants.STATUS_IN_PROGRESS) {
            btn_release.visibility = View.VISIBLE
            btn_cancel.visibility = View.GONE
            btn_dispute.visibility = View.VISIBLE
        } else if (order.Status == Constants.STATUS_COMPLETED) {
            btn_release.visibility = View.GONE
            btn_cancel.visibility = View.GONE
            btn_dispute.visibility = View.GONE
        } else if (order.Status == Constants.STATUS_CANCEL) {
            btn_release.visibility = View.GONE
            btn_cancel.visibility = View.GONE
            btn_dispute.visibility = View.GONE
        } else if (order.Status == Constants.STATUS_DISPUTE) {
            btn_release.visibility = View.GONE
            btn_cancel.visibility = View.GONE
            btn_dispute.visibility = View.GONE
        }
        tv_name.text = "U-" + order.FUAC_Id
        tv_coin_amount.text = order.BitAmount+trade.OrderType
        tv_price.text = order.BitPrice

        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        btn_cancel.setOnClickListener {
            var intent = Intent(this@AdvertismentDetailActivity, DisputeActivity::class.java)
            var obj = Gson().toJson(order)
            intent.putExtra("order", obj)
            intent.putExtra("activity", "cancel")
            startActivityForResult(intent, 44)
        }
        btn_dispute.setOnClickListener {
            //updateStatus(Constants.STATUS_CANCEL, order.ORD_Id!!)
            var intent = Intent(this@AdvertismentDetailActivity, DisputeActivity::class.java)
            var obj = Gson().toJson(order)
            intent.putExtra("order", obj)
            intent.putExtra("activity", "dispute")
            startActivityForResult(intent, 44)
        }

        btn_release.setOnClickListener {
            orderRelease(order.ORD_Id, trade.Fees, trade.Amount, order.BitAmount, order.Amount, trade.UT_Id)
            dialog.dismiss()
        }


        dialog.show()
    }

    private fun orderRelease(orD_Id: String?, fees: String?, amount: String?,
                             bitAmount: String?, amount2: String?, uT_Id: Int?) {
        progressBar!!.show()

        ApiClint.getInstance()?.getService()?.orderIRelease(orD_Id!!, fees!!, amount!!, bitAmount!!,
                amount2!!, uT_Id!!.toString())!!.enqueue(object : Callback<com.company.redcode.royalcryptoexchange.models.Response> {
            override fun onFailure(call: Call<com.company.redcode.royalcryptoexchange.models.Response>?, t: Throwable?) {

                print("error " + t)
                progressBar!!.dismiss()

            }

            override fun onResponse(call: Call<com.company.redcode.royalcryptoexchange.models.Response>?, response: Response<com.company.redcode.royalcryptoexchange.models.Response>?) {

                if (response?.body() != null) {
                    progressBar!!.dismiss()
                    Apputils.showMsg(this@AdvertismentDetailActivity, "Trade release")
                    setResult(RESULT_OK);
                    finish()
                }
            }

        })
    }

    fun getOrderList() {
        var fut_id = trade.UT_Id
        progressBar!!.show()
        ApiClint.getInstance()?.getService()?.getOrderByTradeId(fut_id.toString())!!.enqueue(object : Callback<java.util.ArrayList<Order>> {
            override fun onFailure(call: Call<java.util.ArrayList<Order>>?, t: Throwable?) {
                print("error " + t)
                progressBar!!.dismiss()
            }

            override fun onResponse(call: Call<java.util.ArrayList<Order>>?, response: Response<java.util.ArrayList<Order>>?) {
                if (response?.body() != null) {
                    response?.body()?.forEach { order ->
                        orderList.add(order)
                    }
                    set_message!!.setText("")
                    progressBar!!.dismiss()
                    orderAdapater!!.notifyDataSetChanged()
                }
                if(orderList.size == null || orderList.size == 0 || response?.body() == null) {
                    Log.d("$$$" , "Working")
                    set_message!!.setText("Currently no Trade Available!")

                    //Toast.makeText(this, "Currently no Trade Available!", Toast.LENGTH_LONG).show()

                }
            }

        })
    }

    fun updateStatus(status: String, order_id: String) {
        progressBar!!.show()
        ApiClint.getInstance()?.getService()?.update_order_status(order_id, status)!!.enqueue(object : Callback<com.company.redcode.royalcryptoexchange.models.Response> {
            override fun onFailure(call: Call<com.company.redcode.royalcryptoexchange.models.Response>?, t: Throwable?) {
                print("error " + t)
                progressBar!!.dismiss()
            }

            override fun onResponse(call: Call<com.company.redcode.royalcryptoexchange.models.Response>?, response: Response<com.company.redcode.royalcryptoexchange.models.Response>?) {
                if (response?.body() != null) {
                    setResult(RESULT_OK);
                    finish()
                    progressBar!!.dismiss()
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
