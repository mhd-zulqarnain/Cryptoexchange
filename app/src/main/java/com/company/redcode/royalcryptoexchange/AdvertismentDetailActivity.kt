package com.company.redcode.royalcryptoexchange

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.widget.LinearLayout
import com.company.redcode.royalcryptoexchange.adapter.OrderAdapater
import com.company.redcode.royalcryptoexchange.models.Order
import com.company.redcode.royalcryptoexchange.models.Trade
import com.company.redcode.royalcryptoexchange.retrofit.ApiClint
import com.company.redcode.royalcryptoexchange.utils.SharedPref
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_advertisment_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdvertismentDetailActivity : AppCompatActivity() {

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
    }

    private fun initView() {

        tv_details.text = "Amount:"+trade.Amount+"\n"+"Price:"+trade.Price
        order_recycler = findViewById(R.id.order_recycler)
        val orderlayout = LinearLayoutManager(this@AdvertismentDetailActivity, LinearLayout.VERTICAL, false)
        order_recycler!!.layoutManager =orderlayout
        orderAdapater = OrderAdapater(this@AdvertismentDetailActivity, orderList){ post ->
            //action
            val intent = Intent(this@AdvertismentDetailActivity,OrderDetailActivity::class.java)
            var obj = Gson().toJson(orderList[post])
            intent.putExtra("order",obj)
            startActivity(intent)
        }
        order_recycler!!.adapter = orderAdapater

        getOrderList()

    }

    fun getOrderList(){
        var fut_id=trade.UT_Id
        progressBar!!.show()
        ApiClint.getInstance()?.getService()?.getOrderByTradeId(fut_id.toString())!!.enqueue(object : Callback<java.util.ArrayList<Order>> {
            override fun onFailure(call: Call<java.util.ArrayList<Order>>?, t: Throwable?) {
                print("error "+t)
                progressBar!!.dismiss()

            }
            override fun onResponse(call: Call<java.util.ArrayList<Order>>?, response: Response<java.util.ArrayList<Order>>?) {
                if (response?.body() != null) {
                    response?.body()?.forEach { order ->
                        orderList.add(order)
                    }
                    progressBar!!.dismiss()
                    orderAdapater!!.notifyDataSetChanged()
                }
            }

        })
    }

}
