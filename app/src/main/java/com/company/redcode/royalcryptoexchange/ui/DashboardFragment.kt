package com.company.redcode.royalcryptoexchange.ui


import android.app.AlertDialog
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast

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
    var progressBar: AlertDialog? = null
    var adsAdapater: AdsDashboardAdapater? = null
    var orderList: ArrayList<Order> = ArrayList()
    var adsList: ArrayList<Trade> = ArrayList()
    var sharedpref : SharedPref = SharedPref.getInstance()!!;
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val builder = AlertDialog.Builder(activity!!)
        builder.setView(R.layout.layout_dialog_progress)
        builder.setCancelable(false)
        progressBar = builder.create()

        initView(view)



        return view
    }

    private fun initView(view: View?) {
        orderList.add(Order())
        orderList.add(Order())
        orderList.add(Order())
        orderList.add(Order())



        order_recycler = view!!.findViewById(R.id.order_recycler)
        advertisment_recycler = view!!.findViewById(R.id.advertisment_recycler)
        val orderlayout = LinearLayoutManager(activity!!, LinearLayout.VERTICAL, false)
        val adslayout = LinearLayoutManager(activity!!, LinearLayout.VERTICAL, false)

        order_recycler!!.layoutManager =orderlayout
        advertisment_recycler!!.layoutManager =adslayout

        orderAdapater = OrderAdapater(activity!!, orderList){ post ->
            //action
        }

        generatetrade();

        adsAdapater = AdsDashboardAdapater(activity!!, adsList){ post ->
            //action
        }
        advertisment_recycler!!.adapter = adsAdapater
        order_recycler!!.adapter = orderAdapater




    }

    private fun generatetrade() {
        progressBar!!.show();
//        generateorder();
        var userId = sharedpref.getProfilePref(activity!!).UAC_Id
        if (userId != null) {

            ApiClint.getInstance()?.getService()?.getdashboardorder(userId)?.enqueue(object : Callback<java.util.ArrayList<Trade>> {
                override fun onFailure(call: Call<java.util.ArrayList<Trade>>?, t: Throwable?) {
                    Toast.makeText(activity!!,"Network Error!",Toast.LENGTH_SHORT).show()
                }
                override fun onResponse(call: Call<java.util.ArrayList<Trade>>?, response: Response<java.util.ArrayList<Trade>>?) {
                    if (response?.body() != null) {
                        response?.body()?.forEach { trade ->
                            adsList.add(trade)
                        }
                        progressBar!!.dismiss()
                        adsAdapater!!.notifyDataSetChanged()
                    }
                }
            })


}
    }
}
