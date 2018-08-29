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
import com.example.redcode.sellandbye.adapter.TableBuyerAdapater
import com.example.redcode.sellandbye.models.Payments

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class BuyMainFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view  = inflater.inflate(R.layout.fragment_buy_main, container, false)
        initView(view)
        return view
    }

    private fun initView(view: View) {

        var list = ArrayList<Payments>()
        list.add(Payments("ahmed", "Bank Transfer", "2000", "PKR", "1000"))
        list.add(Payments("ali", "Bank Transfer", "7000", "INR", "200"))
        list.add(Payments("farukh", "Bank Transfer", "2000", "USD", "4000"))
        var recyclerView: RecyclerView = view.findViewById(R.id.table_recycler)
        var adapter = TableBuyerAdapater(activity!!, list)
        var layout = LinearLayoutManager(activity!!, LinearLayout.VERTICAL, false)
        recyclerView.layoutManager = layout
        recyclerView.adapter = adapter


    }


}
