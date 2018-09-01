package com.company.redcode.royalcryptoexchange.ui


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import com.company.redcode.royalcryptoexchange.R
import com.company.redcode.royalcryptoexchange.adapter.TableBuyerAdapater
import com.company.redcode.royalcryptoexchange.models.Payments
import com.company.redcode.royalcryptoexchange.models.Trade
import com.company.redcode.royalcryptoexchange.models.Users
import java.util.*


class BuyFragment : Fragment() {

    var seller_filter_group: RadioGroup? = null
    var seller_limit_filter: RadioButton? = null
    var seller_price_filter: RadioButton? = null
    var seller_coin_filter: RadioButton? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_buy, container, false)
        initView(view)
        return view
    }

    private fun initView(view: View) {

        seller_coin_filter = view.findViewById(R.id.seller_coin_filter)
        seller_filter_group = view.findViewById(R.id.seller_filter_group)
        seller_price_filter = view.findViewById(R.id.seller_price_filter)
        seller_limit_filter = view.findViewById(R.id.seller_limit_filter)

        var list = ArrayList<Trade>()
        list.add(Trade("233","user3322", Users("user3322"),"bankid",1200000,50000
                        ,"3 hours","BTC","3","120000"))
         list.add(Trade("633","user6322", Users("user88"),"bankid",2700000,30000
                        ,"5 hours","BTC","1","110000"))
         list.add(Trade("133","user3322", Users("user377"),"bankid",1600000,40000
                        ,"2 hours","BTC","2","166000"))


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

        seller_filter_group!!.setOnCheckedChangeListener(
                RadioGroup.OnCheckedChangeListener { group, checkId ->
                    if (seller_limit_filter!!.isChecked) {
                        Collections.sort(list, Trade.Order.ByLimit.ascending());
                        adapter.notifyDataSetChanged()
                    }
                    if (seller_price_filter!!.isChecked) {
                        Collections.sort(list, Trade.Order.ByPrice.ascending());
                        adapter.notifyDataSetChanged()
                    }
                    if (seller_coin_filter!!.isChecked) {
                        Collections.sort(list, Trade.Order.ByAmount.ascending());
                        adapter.notifyDataSetChanged()
                    }
                })


    }


}

