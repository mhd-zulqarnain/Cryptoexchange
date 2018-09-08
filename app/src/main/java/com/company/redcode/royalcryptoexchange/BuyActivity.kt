package com.company.redcode.royalcryptoexchange

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.*
import com.company.redcode.royalcryptoexchange.adapter.TableBuyerAdapater
import com.company.redcode.royalcryptoexchange.models.Trade
import com.company.redcode.royalcryptoexchange.retrofit.ApiClint
import com.company.redcode.royalcryptoexchange.ui.BuyingDetailActivity
import com.company.redcode.royalcryptoexchange.utils.OnLoadMoreListener
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class BuyActivity : AppCompatActivity() {



    var seller_filter_group: RadioGroup? = null
    var seller_limit_filter: RadioButton? = null
    var seller_price_filter: RadioButton? = null
    var seller_coin_filter: RadioButton? = null

    var progressBar: AlertDialog? = null
    var coin: String = "BTC"
    var tradelist = ArrayList<Trade>()
    var adapter: TableBuyerAdapater? = null
    var total:Double? = null
    /*Dialog item */
    var btnCancel: Button? = null
    var ed_currency: TextView? = null
    var u_limit: EditText? = null

    var l_limit: EditText? = null
    var btnSave: Button? = null
    var spinner_time: Spinner? = null
    var time: String? = null

    /*scroll recycler */
    var visibleThreshold:Int ? =5
    var lastVisibleItem:Int ? = null
    var totalItemCount:Int ? = null
    var  onLoadMoreListener: OnLoadMoreListener? =null;
    var loading: Boolean? = false

    var toolbar :Toolbar ? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buy)
        toolbar = findViewById(R.id.toolbar_top)
        initView()
    }

    @SuppressLint("NewApi")
    private fun initView() {


        seller_coin_filter = findViewById(R.id.seller_coin_filter)
        seller_filter_group =findViewById(R.id.seller_filter_group)
        seller_price_filter = findViewById(R.id.seller_price_filter)
        seller_limit_filter = findViewById(R.id.seller_limit_filter)


        val coin_type_spinner =findViewById(R.id.curreny_type_spinner) as Spinner

        val builder = AlertDialog.Builder(this@BuyActivity!!)
        builder.setView(R.layout.layout_dialog_progress)
        builder.setCancelable(false)
        progressBar = builder.create()

        val recyclerView: RecyclerView = findViewById(R.id.table_recycler)
        Collections.sort(tradelist, Trade.Order.ByPrice.descending());

        adapter = TableBuyerAdapater(this@BuyActivity, tradelist) { position ->
            var obj = Gson().toJson(tradelist[position])
            val intent = Intent(this@BuyActivity, BuyingDetailActivity::class.java)
            intent.putExtra("tradeObject", obj)
            startActivity(intent)
        }

        val layoutManager = LinearLayoutManager(this@BuyActivity, LinearLayout.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        recyclerView.itemAnimator = DefaultItemAnimator()

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

//                 = layoutManager?.childCount

                totalItemCount  = layoutManager?.itemCount

                lastVisibleItem  = layoutManager?.findFirstVisibleItemPosition()

                if (loading!! && ((lastVisibleItem!! + visibleThreshold!!) >= totalItemCount!!) && dy > 0) {
                    loading = false
                  //  loadDataFromArrayList()
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                   // isScrolling = true
                }
            }
        })
        val spinnerAdapter = ArrayAdapter.createFromResource(this@BuyActivity,
                R.array.array_coin, android.R.layout.simple_spinner_item)

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        coin_type_spinner.adapter = spinnerAdapter
        coin_type_spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                val item = parent!!.getItemAtPosition(pos);
                coin = item.toString()
                getAllTrade()
            }
        })

        seller_filter_group!!.setOnCheckedChangeListener(
                RadioGroup.OnCheckedChangeListener { group, checkId ->
                    if (seller_limit_filter!!.isChecked) {
                        Collections.sort(tradelist, Trade.Order.ByLimit.ascending());
                        adapter?.notifyDataSetChanged()
                    }
                    if (seller_price_filter!!.isChecked) {
                        Collections.sort(tradelist, Trade.Order.ByPrice.descending());
                        adapter?.notifyDataSetChanged()
                    }
                    if (seller_coin_filter!!.isChecked) {
                        Collections.sort(tradelist, Trade.Order.ByAmount.ascending());
                        adapter?.notifyDataSetChanged()
                    }
                })

    }


    private fun getAllTrade() {
        tradelist.clear()

        progressBar!!.show()
        ApiClint.getInstance()?.getService()?.getTradeByType(coin, "buy")?.enqueue(object : Callback<ArrayList<Trade>> {
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
                Toast.makeText(this@BuyActivity, "Network error ", Toast.LENGTH_LONG).show()

            }
        })
    }

    fun getCoinAfterFee(coinNum: Double, price: Double): Double {

        var feeAmount = 4
        var totalPrice: Double = coinNum * price
        var fees: Double = totalPrice * feeAmount / 100
        var actualPrice: Double = totalPrice - fees

        var coinRem: Double = actualPrice / price

        return coinRem
    }



}
