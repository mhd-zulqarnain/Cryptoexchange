package com.example.redcode.sellandbye.ui


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

import com.example.redcode.sellandbye.R
import com.example.redcode.sellandbye.adapter.TableSellerAdapater
import com.example.redcode.sellandbye.models.Payments


class SellFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_sell, container, false)
        initView(view)
        return view
    }

    fun initView(view:View){
        var list = ArrayList<Payments>()
        list.add(Payments("saqib", "Bank Transfer", "1000", "IKR", "1030"))
        list.add(Payments("aslam", "Bank Transfer", "7000", "PNR", "300"))
        list.add(Payments("red", "Bank Transfer", "200", "USD", "3000"))
        var recyclerView: RecyclerView = view!!.findViewById(R.id.table_seller_recycler)
        var adapter = TableSellerAdapater(activity!!, list)
        var layout = LinearLayoutManager(activity!!, LinearLayout.VERTICAL, false)
        recyclerView.layoutManager = layout
        recyclerView.adapter = adapter
    }

}
