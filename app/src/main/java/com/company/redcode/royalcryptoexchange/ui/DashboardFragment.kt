package com.company.redcode.royalcryptoexchange.ui


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

import com.company.redcode.royalcryptoexchange.R
import com.company.redcode.royalcryptoexchange.adapter.AdsDashboardAdapater
import com.company.redcode.royalcryptoexchange.adapter.OrderAdapater
import com.company.redcode.royalcryptoexchange.models.Order
import com.company.redcode.royalcryptoexchange.models.Trade
import com.company.redcode.royalcryptoexchange.retrofit.ApiClint
import com.company.redcode.royalcryptoexchange.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DashboardFragment : Fragment() {

    var order_recycler: RecyclerView? = null
    var advertisment_recycler: RecyclerView? = null
    var orderAdapater: OrderAdapater? = null
    var adsAdapater: AdsDashboardAdapater? = null
    var orderList: ArrayList<Order> = ArrayList()
    var adsList: ArrayList<Trade> = ArrayList()
    var sharedPref:SharedPref = SharedPref.getInstance()!!

    var progressBar: AlertDialog? = null
    @SuppressLint("NewApi")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val builder = AlertDialog.Builder(activity!!)
        builder.setView(R.layout.layout_dialog_progress)
        builder.setCancelable(false)
        progressBar = builder.create()

        var view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        initView(view)
        return view
    }

    private fun initView(view: View?) {
        order_recycler = view!!.findViewById(R.id.order_recycler)
        advertisment_recycler = view!!.findViewById(R.id.advertisment_recycler)
        val orderlayout = LinearLayoutManager(activity!!, LinearLayout.VERTICAL, false)
        val adslayout = LinearLayoutManager(activity!!, LinearLayout.VERTICAL, false)

        order_recycler!!.layoutManager =orderlayout
        advertisment_recycler!!.layoutManager =adslayout

        orderAdapater = OrderAdapater(activity!!, orderList){ post ->
            //action
        }
        getOrderList()
        adsList.add(Trade())
        adsList.add(Trade())
        adsList.add(Trade())
        adsList.add(Trade())

        adsAdapater = AdsDashboardAdapater(activity!!, adsList){ post ->
            //action
        }
        advertisment_recycler!!.adapter = adsAdapater
        order_recycler!!.adapter = orderAdapater

    }

    fun getOrderList(){
        var fuac_id = sharedPref.getProfilePref(activity!!).UAC_Id
        progressBar!!.show()
        ApiClint.getInstance()?.getService()?.getOrderById(fuac_id!!)!!.enqueue(object :Callback<java.util.ArrayList<Order>>{
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
