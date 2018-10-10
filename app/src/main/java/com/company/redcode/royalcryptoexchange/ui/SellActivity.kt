package com.company.redcode.royalcryptoexchange.ui


import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.widget.*
import com.company.redcode.royalcryptoexchange.R
import com.company.redcode.royalcryptoexchange.adapter.TableBuyerAdapater
import com.company.redcode.royalcryptoexchange.adapter.TableSellerAdapater
import com.company.redcode.royalcryptoexchange.models.Trade
import com.company.redcode.royalcryptoexchange.retrofit.ApiClint
import com.company.redcode.royalcryptoexchange.utils.AppExecutors
import com.company.redcode.royalcryptoexchange.utils.Apputils
import com.company.redcode.royalcryptoexchange.utils.SharedPref
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_buy.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class SellActivity : AppCompatActivity() {


    var set_message : TextView? = null
    var seller_filter_group: RadioGroup? = null
    var seller_limit_filter: RadioButton? = null
    var seller_price_filter: RadioButton? = null
    var seller_coin_filter: RadioButton? = null


    var progressBar: AlertDialog? = null
    var coin: String = "BTC"
    var tradelist = ArrayList<Trade>()
    var adapter: TableSellerAdapater? = null
    var total: Double? = null
    /*Dialog item */
    var btnCancel: Button? = null
    var ed_currency: TextView? = null
    var u_limit: EditText? = null

    var l_limit: EditText? = null
    var btnSave: Button? = null
    var spinner_time: Spinner? = null
    var time: String? = null

    /*scroll recycler */
    var isScrolling: Boolean? = false
    private var currentItems: Int? = 0
    private var totalItems: Int? = 0
    private var scrollOutItems: Int? = 0

    var toolbar: Toolbar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sell)
        toolbar = findViewById(R.id.toolbar_top)
        initView()
    }

    @SuppressLint("NewApi")
    private fun initView() {

        seller_coin_filter = findViewById(R.id.seller_coin_filter)
        seller_filter_group = findViewById(R.id.seller_filter_group)
        seller_price_filter = findViewById(R.id.seller_price_filter)
        seller_limit_filter = findViewById(R.id.seller_limit_filter)
        set_message = findViewById(R.id.tv_no_data)


        val coin_type_spinner = findViewById(R.id.curreny_type_spinner) as Spinner

        val builder = AlertDialog.Builder(this@SellActivity!!)
        builder.setView(R.layout.layout_dialog_progress)
        builder.setCancelable(false)
        progressBar = builder.create()

        val recyclerView: RecyclerView = findViewById(R.id.table_recycler)
        Collections.sort(tradelist, Trade.Order.ByPrice.descending());

        adapter = TableSellerAdapater(this@SellActivity, tradelist) { position ->
            var obj = Gson().toJson(tradelist[position])
            val intent = Intent(this@SellActivity, PlaceOrderActivity::class.java)
            intent.putExtra("tradeObject", obj)
            intent.putExtra("orderType", "Sell")
            startActivityForResult(intent,44)
        }

        val layoutManager = LinearLayoutManager(this@SellActivity, LinearLayout.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        recyclerView.itemAnimator = DefaultItemAnimator()

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                currentItems = layoutManager?.childCount

                totalItems = layoutManager?.itemCount

                scrollOutItems = layoutManager?.findFirstVisibleItemPosition()

                if (isScrolling!! && ((currentItems!! + scrollOutItems!!) >= totalItems!!) && dy > 0) {
                    isScrolling = false
                    loadDataFromArrayList()
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true
                }
            }
        })

        val spinnerAdapter = ArrayAdapter.createFromResource(this@SellActivity,
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
                    if (seller_limit_filter!!.isChecked ) {
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

        btn_back.setOnClickListener {
            finish()
        }

    }


    private fun getAllTrade() {

        if (!Apputils.isNetworkAvailable(this@SellActivity)) {
            Toast.makeText(baseContext, " Network error ", Toast.LENGTH_SHORT).show()
            return
        }
        tradelist.clear()

        progressBar!!.show()
        var fuacid = SharedPref.getInstance()!!.getProfilePref(this@SellActivity).UAC_Id

        ApiClint.getInstance()?.getService()?.getTrade("sell", coin,fuacid = fuacid!!)?.enqueue(object : Callback<ArrayList<Trade>> {
            override fun onFailure(call: Call<ArrayList<Trade>>?, t: Throwable?) {
                println("failed "+t)
            }
            override fun onResponse(call: Call<ArrayList<Trade>>?, response: Response<ArrayList<Trade>>?) {




                if (response?.body() != null) {
                    response?.body()?.forEach { trade ->
                        tradelist.add(trade)

                    }
                    tv_no_data.visibility = View.GONE
                    progressBar!!.dismiss()
                    adapter!!.notifyDataSetChanged()
                }
                if(tradelist.size == null || tradelist.size == 0 || response?.body() == null) {
                    Log.d("$$$" , "Working")
                    tv_no_data!!.setText("Currently no Trade Available!")
                    tv_no_data.visibility = View.VISIBLE

                    //Toast.makeText(this, "Currently no Trade Available!", Toast.LENGTH_LONG).show()

                }






            }

        })


    }

    private fun loadDataFromArrayList() {
        //progressLoadData?.visibility = View.VISIBLE
        val progressdialog = ProgressDialog(this@SellActivity)
        progressdialog.setMessage("Please Wait....")
        progressdialog.show()

        AppExecutors.instance.diskIO().execute {
            runOnUiThread {
                Handler().postDelayed(
                        {
                            if ((adapter!!.num) * 20 < tradelist!!.size) {
                                adapter!!.num = adapter!!.num + 1
                            }
                            progressdialog.dismiss()
                        },
                        1200L
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            getAllTrade()
        }
    }
}
