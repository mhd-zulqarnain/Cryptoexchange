package com.example.redcode.sellandbye.ui


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import com.example.redcode.sellandbye.R
import com.example.redcode.sellandbye.adapter.TableBuyerAdapater
import com.example.redcode.sellandbye.models.Payments
import java.util.*


class BuyFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_buy, container, false)
        initView(view)
        return view
    }

    private fun initView(view: View) {

        var list = ArrayList<Payments>()
        list.add(Payments("ahmed", "Bank Transfer", "22", "PKR", 500))
        list.add(Payments("ali", "Bank Transfer", "10", "INR", 700))
        list.add(Payments("farukh", "Bank Transfer", "30", "USD", 30))




        Collections.sort(list, Payments.Order.ByPrice.descending());

        var recyclerView: RecyclerView = view.findViewById(R.id.table_recycler)

        var adapter = TableBuyerAdapater(activity!!, list) { position ->
            var intent = Intent(activity!!, BuyActivity::class.java)
            startActivity(intent)
        }
        var layout = LinearLayoutManager(activity!!, LinearLayout.VERTICAL, false)
        recyclerView.layoutManager = layout
        recyclerView.adapter = adapter


        val coin_type_spinner = view.findViewById(R.id.curreny_type_spinner) as Spinner
        var spinnerAdapter = ArrayAdapter.createFromResource(activity!!,
                R.array.array_coin, android.R.layout.simple_spinner_item)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        coin_type_spinner.adapter = spinnerAdapter
        coin_type_spinner!!.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                var item = parent!!.getItemAtPosition(pos);
                Toast.makeText(activity!!, "item " + item, Toast.LENGTH_SHORT).show()
            }
        })

    }


}

