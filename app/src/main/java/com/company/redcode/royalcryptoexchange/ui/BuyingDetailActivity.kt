package com.company.redcode.royalcryptoexchange.ui


import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.CompoundButton
import android.widget.TextView
import com.company.redcode.royalcryptoexchange.R
import com.company.redcode.royalcryptoexchange.models.Order
import com.company.redcode.royalcryptoexchange.models.OrderTerms
import com.company.redcode.royalcryptoexchange.models.Response
import com.company.redcode.royalcryptoexchange.models.Trade
import com.company.redcode.royalcryptoexchange.retrofit.ApiClint
import com.company.redcode.royalcryptoexchange.utils.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_buying_details.*
import retrofit2.Call
import retrofit2.Callback
import java.lang.reflect.Method


class BuyingDetailActivity : AppCompatActivity() {

    val JSON_TRARE: String = "tradeObject"
    val ORDER_TYPE: String = "orderType"
    var trade: Trade = Trade()
    var orderType: String? = null
    var order:Order = Order()
    var isTermAccept: Boolean = false
    var progressBar: AlertDialog? = null
    var sharedPref = SharedPref.getInstance()
    var orderTerms:OrderTerms = OrderTerms()
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buying_details)
        var obj = intent.getStringExtra(JSON_TRARE);
        orderType = intent.getStringExtra(ORDER_TYPE);
        trade = Gson().fromJson(obj, Trade::class.java)

        initView()
        getTerm()

    }

    fun getTerm(){
        progressBar!!.show()

        ApiClint.getInstance()?.getService()?.gettermAndPayment(trade.FUAC_Id.toString()!!,"3")?.enqueue(object :Callback<OrderTerms>{
            override fun onFailure(call: Call<OrderTerms>?, t: Throwable?) {
                progressBar!!.dismiss()
            }

            override fun onResponse(call: Call<OrderTerms>?, response: retrofit2.Response<OrderTerms>?) {
                progressBar!!.dismiss()
                if (response != null) {
                    orderTerms = response.body()!!
                    tv_payment_method.setText(orderTerms.PaymentMethod!!.BankName)
                    if (orderTerms.PaymentMethod!!.Type=="Bank")
                    tv_payment_method.text = tv_payment_method.text.toString()+"\nCode:"+orderTerms!!.PaymentMethod!!.BankCode
                }
            }
        })

    }
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun initView() {
        val builder = AlertDialog.Builder(this@BuyingDetailActivity)
        builder.setView(R.layout.layout_dialog_progress)
        builder.setCancelable(false)
        progressBar = builder.create()

        if (orderType == "buy")
            tv_title_trade.setText("Buying trade details")
        else (orderType == "sell")
        tv_title_trade.setText("Selling trade details")
        tv_name.setText("U-" + trade.FUAC_Id)
        tv_coin_amount.setText(trade.Amount + trade.CurrencyType)
        tv_limit.setText(trade.UpperLimit.toString() + "-" + trade.LowerLimit.toString())
        tv_price.setText(trade.Price)

        pkr_ed!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!pkr_ed!!.text.toString().trim().isEmpty() && pkr_ed!!.text.toString().trim() != "") {
                    if ( pkr_ed!!.text.trim().length > 2) {

                        var price = trade.LowerLimit.toString().toLong()
                        if(pkr_ed!!.text.toString().toLong() >= price){
                            //Apputils.showMsg(this@BuyingDetailActivity, " Limit ")
                            ecuurency_ed.setText((trade.Price!!.toDouble()/pkr_ed.text.toString().toDouble()).toString())
                        }
                        else{
                            ecuurency_ed.setText("")

                        }

                    }
                }
            }
        })

        chk_terms.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                isTermAccept = p1;

            }

        })
        tv_terms.setOnClickListener {
            showTradeDialog()
        }

        btn_trade.setOnClickListener {
            validation()
        }
    }

    private fun validation() {

        if (!pkr_ed!!.text.toString().trim().isEmpty() && pkr_ed!!.text.toString().trim() != "") {

            if (pkr_ed!!.text.trim().length > 2) {

                if (pkr_ed!!.text.toString().toLong() > trade.UpperLimit.toString().toLong()) {
                    Apputils.showMsg(this@BuyingDetailActivity, "Limit crossed")
                    return
                } else if (pkr_ed!!.text.toString().toLong() < trade.LowerLimit.toString().toLong()) {

                    Apputils.showMsg(this@BuyingDetailActivity, "Should exceed lower Limit ")
                    return
                }
            }
        }
        if (isTermAccept) {
//            order.Amount =
           /* var intent = Intent(this, OrderDetailActivity::class.java)
            var obj = Gson().toJson(trade)
            val order = Gson().toJson(order)
            intent.putExtra("tradeObj", obj)
            intent.putExtra("order", order)
            intent.putExtra("coinUsed", ecuurency_ed.text.toString())
            intent.putExtra("priceCharged", pkr_ed.text.toString())
            startActivity(intent)
            finish()*/

            addOrder(serviceListener = object :ServiceListener<String>{
                override fun success(obj: String) {
                    Apputils.showMsg(this@BuyingDetailActivity,obj)
                    finish()
                }

                override fun fail(error: ServiceError) {
                    Apputils.showMsg(this@BuyingDetailActivity,"Fail to add new order")
                }
            })
        } else {
            Apputils.showMsg(this@BuyingDetailActivity, "Please accept the terms")
        }

    }

    private fun addOrder(serviceListener:ServiceListener<String>) {
        order.Amount = trade.Amount
        order.Price = trade.Price
        order.BitAmount = ecuurency_ed.text.toString()
        order.BitPrice = pkr_ed.text.toString()
        order.UpperLimit = trade.UpperLimit.toString()
        order.LowerLimit = trade.LowerLimit.toString()
        order.FUAC_Id = sharedPref!!.getProfilePref(this@BuyingDetailActivity).UAC_Id
        order.FUT_Id = trade.UT_Id.toString()
        order.Notify_Status = "true"
        order.PaymentMethod = "bank"
        order.Status = "open"
        order.ORD_UserId = sharedPref!!.getProfilePref(this@BuyingDetailActivity).UAC_Id
        order.User_Id = trade.FUAC_Id.toString()

        if(orderType=="buy")
            order.Description = "bought"
        else
            order.Description = "sold"


        ApiClint.getInstance()?.getService()?.addNewOrder(
                order.User_Id!!, order.ORD_UserId!!, order.FUAC_Id!!, order.FUT_Id!!
                ,order.Price!!,order.Amount!!,order.PaymentMethod!!,order.UpperLimit!!,order.LowerLimit!!,
                order.BitAmount!!, order.BitPrice!!, order.Status!!,order.Description!!,order.Notify_Status!!)?.
                enqueue(object: Callback<Response>{
            override fun onFailure(call: Call<Response>?, t: Throwable?) {
                print("error"+t)
                serviceListener.fail(ServiceError("failed"))

            }
            override fun onResponse(call: Call<Response>?, response: retrofit2.Response<Response>?) {
                print("success")
                print(response!!.body())
                serviceListener.success("Order placed Succcessfully")

            }
        })
    }

    private fun showTradeDialog() {
        val view: View = LayoutInflater.from(this@BuyingDetailActivity).inflate(R.layout.dilalog_terms_trade, null)
        val alertBox = android.support.v7.app.AlertDialog.Builder(this@BuyingDetailActivity)
        alertBox.setView(view)
        alertBox.setCancelable(false)
        val dialog = alertBox.create()

        val btnSave: Button = view.findViewById(R.id.btn_save)
        val terms_tv: TextView = view.findViewById(R.id.terms_tv)
        terms_tv.setText(orderTerms.Terms)

        btnSave.setOnClickListener {
            dialog.dismiss()
        }


        dialog.show()
    }

}
