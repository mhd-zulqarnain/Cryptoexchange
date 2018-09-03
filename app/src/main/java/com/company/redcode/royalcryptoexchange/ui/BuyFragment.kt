package com.company.redcode.royalcryptoexchange.ui


import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
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
import com.company.redcode.royalcryptoexchange.models.Trade
import com.company.redcode.royalcryptoexchange.models.Users
import java.util.*
import android.text.Editable
import android.text.TextWatcher
import com.company.redcode.royalcryptoexchange.models.ApiResponse
import com.company.redcode.royalcryptoexchange.retrofit.ApiClint
import com.company.redcode.royalcryptoexchange.utils.Apputils
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat


class BuyFragment : Fragment() {

    var seller_filter_group: RadioGroup? = null
    var seller_limit_filter: RadioButton? = null
    var seller_price_filter: RadioButton? = null
    var ed_amount: EditText? = null
    var ed_price: EditText? = null
    var ed_total: EditText? = null
    var btn_trade: Button? = null
    var seller_coin_filter: RadioButton? = null
    var progressBar: AlertDialog? = null
    var coin: String = "BTC"
    var tradelist = ArrayList<Trade>()
    var adapter: TableBuyerAdapater? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_buy, container, false)
        initView(view)
        return view
    }

    @SuppressLint("NewApi")
    private fun initView(view: View) {


        seller_coin_filter = view.findViewById(R.id.seller_coin_filter)
        seller_filter_group = view.findViewById(R.id.seller_filter_group)
        seller_price_filter = view.findViewById(R.id.seller_price_filter)
        seller_limit_filter = view.findViewById(R.id.seller_limit_filter)
        ed_amount = view.findViewById(R.id.ed_amount)
        ed_price = view.findViewById(R.id.ed_price)
        btn_trade = view.findViewById(R.id.btn_trade)
        ed_total = view.findViewById(R.id.ed_total)
        val coin_type_spinner = view.findViewById(R.id.curreny_type_spinner) as Spinner

        val builder = AlertDialog.Builder(activity!!)
        builder.setView(R.layout.layout_dialog_progress)
        builder.setCancelable(false)
        progressBar = builder.create()
        getAllTrade()


        val recyclerView: RecyclerView = view.findViewById(R.id.table_recycler)
        Collections.sort(tradelist, Trade.Order.ByLimit.ascending());

        adapter = TableBuyerAdapater(activity!!, tradelist) { position ->

            var obj = Gson().toJson(tradelist[position])
            val intent = Intent(activity!!, BuyActivity::class.java)
            intent.putExtra("tradeObject", obj)
            startActivity(intent)
        }

        val layout = LinearLayoutManager(activity!!, LinearLayout.VERTICAL, false)
        recyclerView.layoutManager = layout
        recyclerView.adapter = adapter


        val spinnerAdapter = ArrayAdapter.createFromResource(activity!!,
                R.array.array_coin, android.R.layout.simple_spinner_item)

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        coin_type_spinner.adapter = spinnerAdapter
        coin_type_spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                val item = parent!!.getItemAtPosition(pos);
                coin = item.toString()
            }
        })

        seller_filter_group!!.setOnCheckedChangeListener(
                RadioGroup.OnCheckedChangeListener { group, checkId ->
                    if (seller_limit_filter!!.isChecked) {
                        Collections.sort(tradelist, Trade.Order.ByLimit.ascending());
                        adapter?.notifyDataSetChanged()
                    }
                    if (seller_price_filter!!.isChecked) {
                        Collections.sort(tradelist, Trade.Order.ByPrice.ascending());
                        adapter?.notifyDataSetChanged()
                    }
                    if (seller_coin_filter!!.isChecked) {
                        Collections.sort(tradelist, Trade.Order.ByAmount.ascending());
                        adapter?.notifyDataSetChanged()
                    }
                })

        btn_trade!!.setOnClickListener {

            showTradeDialog()

        }

        ed_price!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!ed_amount!!.text.toString().trim().isEmpty() && ed_amount!!.text.toString().trim() != "") {

                    if (!ed_price!!.text.toString().trim().isEmpty() && ed_price!!.text.toString().trim() != "") {
                        val amout = ed_amount!!.text.toString().toInt()
                        val price = ed_price!!.text.toString().toInt()
                        val total = amout * price
                        val df = DecimalFormat("#,###,##0.00")
                        var formated = df.format(total)
                        ed_total!!.setText(formated.toString())
                    }
                }
                if (ed_price!!.text.toString().trim().isEmpty() && ed_price!!.text.toString().trim() != "") {
                    ed_total!!.setText("")
                }
            }
        })

    }

    private fun showTradeDialog() {

        if (ed_amount!!.text.toString() == "") {
            ed_amount!!.error = "Enter the amount i.e the number of coin"
            ed_amount!!.requestFocus()
            return
        }
        if (ed_price!!.text.toString() == "") {
            ed_price!!.error = "Enter the price of one coin"
            ed_price!!.requestFocus()
            return
        }

        val view: View = LayoutInflater.from(activity!!).inflate(R.layout.dilalog_view_trade, null)
        val alertBox = android.support.v7.app.AlertDialog.Builder(activity!!)
        alertBox.setView(view)
        alertBox.setCancelable(false)
        val dialog = alertBox.create()

        var saveAble = true

        val btnCancel: Button = view.findViewById(R.id.btn_cancel)
        val ed_currency: TextView = view.findViewById(R.id.ed_currency)
        val u_limit: EditText = view.findViewById(R.id.u_limit)
        val ed_terms: EditText = view.findViewById(R.id.ed_terms)
        val l_limit: EditText = view.findViewById(R.id.l_limit)
        val btnSave: Button = view.findViewById(R.id.btn_save)

        ed_currency.setText(coin.toUpperCase())
        val spinner_time: Spinner = view.findViewById(R.id.spinner_time)
        var time: String = spinner_time.selectedItem.toString()

        spinner_time!!.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                var item = parent!!.getItemAtPosition(pos);
                time = item.toString()
            }
        })

        btnSave.setOnClickListener {
            if (u_limit!!.text.toString() == "") {
                u_limit!!.error = "Enter the Limit"
                u_limit!!.requestFocus()
                saveAble = false

            } else
                saveAble = true
            if (l_limit!!.text.toString() == "") {
                l_limit!!.error = "Enter the Limit"
                l_limit!!.requestFocus()
                saveAble = false

            } else
                saveAble = true

            if (ed_terms!!.text.toString() == "") {
                ed_terms!!.error = "Enter your terms"
                ed_terms!!.requestFocus()
                saveAble = false

            } else
                saveAble = true

            if (u_limit!!.text.trim().length < 4) {
                u_limit!!.error = "It should be in four figures"
                u_limit!!.requestFocus()
                saveAble = false
            } else
                saveAble = true

            if (l_limit!!.text.trim().length < 4) {
                l_limit!!.error = "It should be in four figures"
                l_limit!!.requestFocus()
                saveAble = false
            } else
                saveAble = true
            if (saveAble) {
                if (l_limit.text.toString().toLong() > u_limit.text.toString().toLong()) {
                    Apputils.showMsg(context as Activity, "Upper limit should  be greater")
                    saveAble = false
                } else
                    saveAble = true
            }
            if (saveAble) {

                progressBar!!.show()

                var mtrade = Trade(null, "322", Users("user3322"), "bankid", u_limit.text.toString().toLong(), l_limit.text.toString().toLong()
                        , time, coin, ed_amount!!.text.toString(), ed_price!!.text.toString(), ed_terms!!.text.toString(), "buy")

                ApiClint.getInstance()?.getService()?.addTrade(trade = mtrade)?.enqueue(object : Callback<ApiResponse> {

                    override fun onResponse(call: Call<ApiResponse>?, response: Response<ApiResponse>?) {
                        println("error  " + response.toString())
                        Toast.makeText(activity!!, "Deal added successfully ", Toast.LENGTH_LONG).show()
                        progressBar!!.dismiss()
                        dialog.dismiss()
                        getAllTrade()

                    }

                    override fun onFailure(call: Call<ApiResponse>?, t: Throwable?) {
                        Toast.makeText(activity!!, "failed ", Toast.LENGTH_LONG).show()
                        println("error  " + t.toString())
                        progressBar!!.dismiss()
                        dialog.dismiss()

                    }

                })

            }
        }

        btnCancel.setOnClickListener {

            dialog.dismiss()
        }
        dialog.show()
    }

    private fun getAllTrade() {
        tradelist.clear()

        progressBar!!.show()
        ApiClint.getInstance()?.getService()?.getTrade()?.enqueue(object : Callback<ArrayList<Trade>> {
            override fun onResponse(call: Call<ArrayList<Trade>>?, response: Response<ArrayList<Trade>>?) {
                response?.body()?.forEach { trade ->
                    tradelist.add(trade)
                }
                progressBar!!.dismiss()
                adapter!!.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<ArrayList<Trade>>?, t: Throwable?) {
                println("error tpye  " + t.toString())
                progressBar!!.dismiss()
                Toast.makeText(activity!!, "Network error ", Toast.LENGTH_LONG).show()

            }
        })
    }
}

