package com.company.redcode.royalcryptoexchange.ui

import android.app.AlertDialog
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.view.View
import com.company.redcode.royalcryptoexchange.R
import com.company.redcode.royalcryptoexchange.models.Order
import com.company.redcode.royalcryptoexchange.models.OrderTerms
import com.company.redcode.royalcryptoexchange.models.Response
import com.company.redcode.royalcryptoexchange.models.UserOrderDispute
import com.company.redcode.royalcryptoexchange.retrofit.ApiClint
import com.company.redcode.royalcryptoexchange.utils.Apputils
import com.company.redcode.royalcryptoexchange.utils.Constants
import com.company.redcode.royalcryptoexchange.utils.ServiceError
import com.company.redcode.royalcryptoexchange.utils.ServiceListener
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_dispute.*
import retrofit2.Call
import retrofit2.Callback

class DisputeActivity : AppCompatActivity() {
    var order: Order = Order()
    var activityType: String = ""
    var progressBar: AlertDialog? = null

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dispute)

        var obj = intent.getStringExtra("order");
        activityType = intent.getStringExtra("activity");
        order = Gson().fromJson(obj, Order::class.java)

        val builder = AlertDialog.Builder(this@DisputeActivity)
        builder.setView(R.layout.layout_dialog_progress)
        builder.setCancelable(false)
        progressBar = builder.create()

        initView()

    }

    private fun initView() {

        ed_seller_id.setText("U-" + order.User_Id)
        ed_Buyer_id.setText("U-" + order.FUAC_Id)
        ed_price_id.setText(order.BitPrice)

        getPayementId(object : ServiceListener<String> {
            override fun success(obj: String) {
                getTerm(obj)
            }

            override fun fail(error: ServiceError) {}
        })
        btn_submit_dispute.setOnClickListener {
            //            orderRelease(order.ORD_Id, trade.Fees, trade.Amount, order.BitAmount, order.Amount, trade.UT_Id)
            addispute()
            updateStatus(Constants.STATUS_DISPUTE, order.ORD_Id!!)
        }

        if(activityType=="dispute"){
            image_view.visibility = View.VISIBLE
            tv_title.text ="Create Dispute"
            ed_dispute_msg.hint ="Enter the dispute messege"
            add_image.text ="Enter the dispute messege"
        }
        if(activityType=="paid"){
            image_view.visibility = View.VISIBLE
            tv_title.text ="Upload the Recipt"
            ed_dispute_msg.hint ="Enter a messege for dealer"
            add_image.text ="Upload script"
            btn_submit_dispute.text ="Done"
        }
        if(activityType=="cancel"){
            image_view.visibility = View.GONE
            tv_title.text ="Upload the Recipt"
            ed_dispute_msg.hint ="Enter a Reason for order cancelling"
            add_image.visibility = View.GONE
            image_view.visibility = View.GONE
            btn_submit_dispute.text ="Done"
        }

    }

    fun getPayementId(serviceListener: ServiceListener<String>) {
        progressBar!!.show()
        ApiClint.getInstance()?.getService()?.getUserPaymentId(order.FUT_Id!!)!!.enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>?, t: Throwable?) {
                println("ERROR")
                progressBar!!.dismiss()
                Apputils.showMsg(this@DisputeActivity, "Network error")
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

    fun getTerm(pid: String) {
        progressBar!!.show()
        ApiClint.getInstance()?.getService()?.gettermAndPayment(order.User_Id.toString()!!, pid)?.enqueue(object : Callback<OrderTerms> {
            override fun onFailure(call: Call<OrderTerms>?, t: Throwable?) {
                progressBar!!.dismiss()
            }

            override fun onResponse(call: Call<OrderTerms>?, response: retrofit2.Response<OrderTerms>?) {
                progressBar!!.dismiss()
                if (response != null) {
                    var orderTerms = response.body()!!

                    if (orderTerms.PaymentMethod!!.Type == "Bank")
                        tv_payment.text = "Type: " + orderTerms.PaymentMethod!!.Type + "\nCode:" + orderTerms!!.PaymentMethod!!.BankCode
                    else {
                        tv_payment.setText("Type: " + orderTerms.PaymentMethod!!.Type + "\n Number:" + orderTerms.PaymentMethod!!.BankName)
                    }

                }
            }
        })

    }


    fun updateStatus(status: String, order_id: String) {
        ApiClint.getInstance()?.getService()?.update_order_status(order_id, status)!!.enqueue(object : Callback<com.company.redcode.royalcryptoexchange.models.Response> {
            override fun onFailure(call: Call<com.company.redcode.royalcryptoexchange.models.Response>?, t: Throwable?) {
                print("error " + t)
            }

            override fun onResponse(call: Call<com.company.redcode.royalcryptoexchange.models.Response>?, response: retrofit2.Response<Response>?) {
                if (response?.body() != null) {
                }
            }

        })
    }

    fun addispute() {

        if (ed_dispute_msg.text.trim() == "") {
            Apputils.showMsg(this@DisputeActivity, "Please set a messege")
            ed_dispute_msg!!.requestFocus()
            return
        }
        var userOrderDispute = UserOrderDispute()

        userOrderDispute.FUAC_Id=order.FUAC_Id
        userOrderDispute.FUT_Id=order.FUT_Id
        userOrderDispute.Image="test"
        userOrderDispute.Message=ed_dispute_msg.text.toString()
        userOrderDispute.UOD_Id="0"
        userOrderDispute.UserId=order.User_Id

        progressBar!!.show()
        ApiClint.getInstance()?.getService()?.addDispute(userOrderDispute)!!.enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>?, t: Throwable?) {
                print("error " + t)
                progressBar!!.dismiss()
            }

            override fun onResponse(call: Call<String>?, response: retrofit2.Response<String>?) {
                if (response?.body() != null) {
                    setResult(RESULT_OK);
                    finish()
                    progressBar!!.dismiss()
                }
            }

        })
    }
}
