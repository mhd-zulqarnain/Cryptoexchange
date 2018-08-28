package com.example.redcode.sellandbye.auth.auth.ui


import android.app.AlertDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import com.example.redcode.sellandbye.R
import com.example.redcode.sellandbye.auth.auth.adapter.TableBuyerAdapater
import com.example.redcode.sellandbye.auth.auth.adapter.TableSellerAdapater
import com.example.redcode.sellandbye.auth.auth.models.Payments
import android.widget.Toast
import android.content.DialogInterface




class HomeFragment : Fragment() {

    var upper_border_sell: View? = null
    var upper_border_buy: View? = null
    var main_view: View? = null
    var buy_btn: Button? = null
    var main_des_tv: TextView? = null
    var sell_btn: Button? = null
    var table_recycler: RecyclerView? = null
    var buyer_view: Boolean? = true
    var buyer_table: LinearLayout? = null
    var seller_table: LinearLayout? = null
    var ed_price: EditText? = null
    var ed_amount: EditText? = null
    var ed_total: EditText? = null
    var btn_trade: Button? = null
    var curreny_type_spinner: Spinner? = null
    var selectedCurrency :String = "PKR"
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        main_view = inflater.inflate(R.layout.fragment_home, container, false)
        initView(main_view)
        initTable(main_view)
        return main_view

    }

    private fun initTable(view: View?) {

        if (buyer_view!!) {

            table_recycler = view!!.findViewById(R.id.table_recycler)
            var list = ArrayList<Payments>()
            list.add(Payments("ahmed", "Bank Transfer", "2000", "PKR", "1000"))
            list.add(Payments("ali", "Bank Transfer", "7000", "INR", "200"))
            list.add(Payments("farukh", "Bank Transfer", "2000", "USD", "4000"))
            var recyclerView: RecyclerView = view.findViewById(R.id.table_recycler)
            var adapter = TableBuyerAdapater(activity!!, list)
            var layout = LinearLayoutManager(activity!!, LinearLayout.VERTICAL, false)
            recyclerView.layoutManager = layout
            recyclerView.adapter = adapter

            main_des_tv!!.setText("Buy bitcoins online in Pakistan")
        } else {

            var list = ArrayList<Payments>()
            list.add(Payments("saqib", "Bank Transfer", "1000", "IKR", "1030"))
            list.add(Payments("aslam", "Bank Transfer", "7000", "PNR", "300"))
            list.add(Payments("red", "Bank Transfer", "200", "USD", "3000"))
            var recyclerView: RecyclerView = view!!.findViewById(R.id.table_seller_recycler)
            var adapter = TableSellerAdapater(activity!!, list)
            var layout = LinearLayoutManager(activity!!, LinearLayout.VERTICAL, false)
            recyclerView.layoutManager = layout
            recyclerView.adapter = adapter
            main_des_tv!!.setText("Sell bitcoins online in Pakistan")
        }
    }

    private fun initView(view: View?) {
        upper_border_sell = view!!.findViewById(R.id.upper_border_sell)
        upper_border_buy = view!!.findViewById(R.id.upper_border_buy)
        buy_btn = view!!.findViewById(R.id.buy_btn)
        sell_btn = view!!.findViewById(R.id.sell_btn)
        main_des_tv = view!!.findViewById(R.id.main_des_tv)
        buyer_table = view!!.findViewById(R.id.buyer_table)
        seller_table = view!!.findViewById(R.id.seller_table)
        ed_price = view!!.findViewById(R.id.ed_price)
        ed_total = view!!.findViewById(R.id.ed_total)
        ed_amount = view!!.findViewById(R.id.ed_amount)
        curreny_type_spinner = view!!.findViewById(R.id.curreny_type_spinner)
        btn_trade = view!!.findViewById(R.id.btn_trade)

        main_des_tv!!.setText("Buy bitcoins online in Pakistan")

        buy_btn!!.setOnClickListener {
            upper_border_sell!!.visibility = View.GONE
            upper_border_buy!!.visibility = View.VISIBLE
            buyer_view = true

            initTable(main_view)
            seller_table!!.visibility = View.GONE
            buyer_table!!.visibility = View.VISIBLE

        }
        sell_btn!!.setOnClickListener {
            upper_border_buy!!.visibility = View.GONE
            upper_border_sell!!.visibility = View.VISIBLE
            buyer_view = false
            initTable(main_view)
            seller_table!!.visibility = View.VISIBLE
            buyer_table!!.visibility = View.GONE
        }

        ed_price!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!ed_amount!!.text.toString().trim().isEmpty() && ed_amount!!.text.toString().trim() != "") {

                    if (!ed_price!!.text.toString().trim().isEmpty() && ed_price!!.text.toString().trim() != "") {
                        var amout = ed_amount!!.text.toString().toInt()
                        var price = ed_price!!.text.toString().toInt()
                        var total = amout * price
                        ed_total!!.setText(total.toString())
                    }
                }
                if (ed_price!!.text.toString().trim().isEmpty() && ed_price!!.text.toString().trim() != ""){
                    ed_total!!.setText("")
                }
            }
        })

        curreny_type_spinner!!.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                var  item = parent!!.getItemAtPosition(pos);
                selectedCurrency= item.toString()
            }
        })

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


        var paymentMethod : String = spinner_method.selectedItem.toString()

        spinner_method!!.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                var  item = parent!!.getItemAtPosition(pos);
                paymentMethod= item.toString()
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
