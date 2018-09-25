package com.company.redcode.royalcryptoexchange.ui

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.company.redcode.royalcryptoexchange.OrderDetailActivity
import com.company.redcode.royalcryptoexchange.R
import com.company.redcode.royalcryptoexchange.R.id.btn_back
import com.company.redcode.royalcryptoexchange.adapter.OrderAdapater
import com.company.redcode.royalcryptoexchange.adapter.UserBankAdapater
import com.company.redcode.royalcryptoexchange.models.Bank
import com.company.redcode.royalcryptoexchange.models.Order
import com.company.redcode.royalcryptoexchange.models.Trade
import com.company.redcode.royalcryptoexchange.retrofit.ApiClint
import com.company.redcode.royalcryptoexchange.utils.Constants
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

        tv_details.text = "Amount:"+trade.Amount+"\n"+"Price:"+trade.Price+"\n"+"Type:"+trade.OrderType
        order_recycler = findViewById(R.id.order_recycler)
        val orderlayout = LinearLayoutManager(this@AdvertismentDetailActivity, LinearLayout.VERTICAL, false)
        order_recycler!!.layoutManager =orderlayout
        orderAdapater = OrderAdapater(this@AdvertismentDetailActivity, orderList){ post ->
            //action
           /* val intent = Intent(this@AdvertismentDetailActivity, OrderDetailActivity::class.java)
            var obj = Gson().toJson(orderList[post])
            intent.putExtra("order",obj)
            startActivity(intent)*/

            showOrderReleaseDialog(orderList[post])
        }
        order_recycler!!.adapter = orderAdapater

        getOrderList()

    }

    private fun showOrderReleaseDialog(order:Order) {

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
        val tv_status: TextView = view.findViewById(R.id.tv_status)

        if(order.Status==Constants.STATUS_CANCEL){
            tv_status.text = "Cancelled"
        }else
            tv_status.text = order.Status

        if(order.Status==Constants.STATUS_OPEN){
            btn_release.visibility = View.GONE
        }
        else if(order.Status==Constants.STATUS_IN_PROGRESS){
            btn_release.visibility = View.VISIBLE
            btn_cancel.visibility = View.GONE
        }
        else if(order.Status==Constants.STATUS_COMPLETED){
            btn_release.visibility = View.GONE
            btn_cancel.visibility = View.GONE
        }
        else if(order.Status==Constants.STATUS_CANCEL){
            btn_release.visibility = View.GONE
            btn_cancel.visibility = View.GONE
        } else if(order.Status==Constants.STATUS_DISPUTE){
            btn_release.visibility = View.GONE
            btn_cancel.visibility = View.GONE
        }
        tv_name.text = "U-"+order.FUAC_Id
        tv_coin_amount.text = order.BitAmount
        tv_price.text = order.BitPrice

        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        btn_cancel.setOnClickListener {
            updateStatus(Constants.STATUS_CANCEL, order.ORD_Id!!)
        }

        btn_release.setOnClickListener {
            updateStatus(Constants.STATUS_COMPLETED,order.ORD_Id!!)
        }


        dialog.show()
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

    fun updateStatus(status:String,order_id:String){
        progressBar!!.show()
        ApiClint.getInstance()?.getService()?.update_order_status(order_id,status)!!.enqueue(object :Callback<com.company.redcode.royalcryptoexchange.models.Response>{
            override fun onFailure(call: Call<com.company.redcode.royalcryptoexchange.models.Response>?, t: Throwable?) {
                print("error "+t)
                progressBar!!.dismiss()
            }

            override fun onResponse(call: Call<com.company.redcode.royalcryptoexchange.models.Response>?, response: Response<com.company.redcode.royalcryptoexchange.models.Response>?) {
                if (response?.body() != null) {
                    finish()
                    progressBar!!.dismiss()
                }
            }

        })
        }

}
