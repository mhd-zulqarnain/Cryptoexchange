package com.company.redcode.royalcryptoexchange.ui


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


class DashboardFragment : Fragment() {

    var order_recycler: RecyclerView? = null
    var advertisment_recycler: RecyclerView? = null
    var orderAdapater: OrderAdapater? = null
    var adsAdapater: AdsDashboardAdapater? = null
    var orderList: ArrayList<Order> = ArrayList()
    var adsList: ArrayList<Trade> = ArrayList()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_dashboard, container, false)
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

}
