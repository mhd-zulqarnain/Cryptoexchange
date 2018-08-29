package com.example.redcode.sellandbye.ui


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import com.example.redcode.sellandbye.R
import com.example.redcode.sellandbye.adapter.TableSellerAdapater
import com.example.redcode.sellandbye.models.Payments


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
        list.add(Payments("saqib", "Bank Transfer", "1000", "IKR", "1030"))
        list.add(Payments("aslam", "Bank Transfer", "7000", "PNR", "300"))
        list.add(Payments("red", "Bank Transfer", "200", "USD", "3000"))
        var recyclerView: RecyclerView = view!!.findViewById(R.id.table_seller_recycler)
        var adapter = TableSellerAdapater(activity!!, list)
        var layout = LinearLayoutManager(activity!!, LinearLayout.VERTICAL, false)
        recyclerView.layoutManager = layout
        recyclerView.adapter = adapter

        btn_trade!!.setOnClickListener{
            showTradeDialog()
        }

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
