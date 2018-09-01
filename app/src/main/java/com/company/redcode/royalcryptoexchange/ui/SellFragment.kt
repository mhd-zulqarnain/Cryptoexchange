package com.company.redcode.royalcryptoexchange.ui


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import com.company.redcode.royalcryptoexchange.R
import com.company.redcode.royalcryptoexchange.adapter.TableSellerAdapater
import com.company.redcode.royalcryptoexchange.models.Payments
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.company.redcode.royalcryptoexchange.models.Trade
import com.company.redcode.royalcryptoexchange.models.Users
import com.company.redcode.royalcryptoexchange.retrofit.ApiClint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SellFragment : Fragment() {
    var btn_trade: Button? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_sell, container, false)
        initView(view)
        return view
    }

    fun initView(view: View) {

        btn_trade = view.findViewById(R.id.btn_trade)
        var list = ArrayList<Payments>()
        list.add(Payments("saqib", "Bank Transfer", "1000", "IKR", 1030))
        list.add(Payments("aslam", "Bank Transfer", "7000", "PNR", 300))
        list.add(Payments("red", "Bank Transfer", "200", "USD", 3000))
        var recyclerView: RecyclerView = view!!.findViewById(R.id.table_seller_recycler)
        var adapter = TableSellerAdapater(activity!!, list)
        var layout = LinearLayoutManager(activity!!, LinearLayout.VERTICAL, false)
        recyclerView.layoutManager = layout
        recyclerView.adapter = adapter

        btn_trade!!.setOnClickListener{
           // showTradeDialog()
            //getReq()
            postReq()
        }

        val coin_type_spinner = view.findViewById(R.id.curreny_type_spinner) as Spinner
        // Creating ArrayAdapter using the string array and default spinner layout
        var spinnerAdapter = ArrayAdapter.createFromResource(activity!!,
                R.array.array_coin, android.R.layout.simple_spinner_item)
        // Specify layout to be used when list of choices appears
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Applying the adapter to our spinner
        coin_type_spinner.adapter = spinnerAdapter
        coin_type_spinner!!.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                var  item = parent!!.getItemAtPosition(pos);
               Toast.makeText(activity!!,"item "+item,Toast.LENGTH_SHORT).show()
            }
        })



    }

    private fun postReq() {
       var mtrade=  Trade("233","user3322", Users("user3322"),"bankid",1200000,50000
                ,"3 hours","BTC","3","120000")
      /*  ApiClint.getInstance()?.getService()?.addTrade(trade = mtrade)?.enqueue(object :Callback<Response>{

        })*/
    }

    private fun getReq() {
        ApiClint.getInstance()?.getService()?.getUser()?.enqueue(object : Callback<ArrayList<Users>> {
            override fun onResponse(call: Call<ArrayList<Users>>?, response: Response<ArrayList<Users>>?) {

                response?.body()?.forEach{user->
                    println(" ")
                    println("User data "+user.email)
                    println("User data "+user.password)
                    println(" ")
                }
            }

            override fun onFailure(call: Call<ArrayList<Users>>?, t: Throwable?) {
                println("error  "+ t.toString())

             }
        })
    }


    private fun showTradeDialog() {
        val view: View = LayoutInflater.from(activity!!).inflate(R.layout.dilalog_view_trade, null)
        val alertBox = android.support.v7.app.AlertDialog.Builder(activity!!)
        alertBox.setView(view)
        alertBox.setCancelable(false)
        val dialog = alertBox.create()


        val btnCancel: Button = view.findViewById(R.id.btn_cancel)
        val btnSave: Button = view.findViewById(R.id.btn_save)

        val spinner_method: Spinner = view.findViewById(R.id.spinner_method)
        var paymentMethod: String = spinner_method.selectedItem.toString()

        spinner_method!!.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                var item = parent!!.getItemAtPosition(pos);
                paymentMethod = item.toString()
            }
        })


        btnSave.setOnClickListener {
            Toast.makeText(activity!!, "Do Something", Toast.LENGTH_SHORT).show()
            var saveable = true

        }

        btnCancel.setOnClickListener {

            dialog.dismiss()
        }
        dialog.show()
    }


}
