package com.company.redcode.royalcryptoexchange.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.company.redcode.royalcryptoexchange.R
import com.company.redcode.royalcryptoexchange.models.Trade
import com.company.redcode.royalcryptoexchange.utils.Apputils
import com.company.redcode.royalcryptoexchange.utils.OnLoadMoreListener




class TableBuyerAdapater(var ctx: Context, var model: ArrayList<Trade>, private val onItemClick: (Int) -> Unit) : RecyclerView.Adapter<TableBuyerAdapater.MyViewHolder>() {
    var num = 1
    var data = model
    var onLoadMoreListener: OnLoadMoreListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view: MyViewHolder = MyViewHolder(LayoutInflater.from(ctx).inflate(R.layout.single_table_row_buyer, parent, false))
        return view
    }


    override fun getItemCount(): Int {
        return if (num * 15 > data.size) {
            data.size
        } else {
            num * 15
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindView(model[position])
        holder.btn_buy!!.setOnClickListener {
            onItemClick(position)
        }
    }

    fun setOnAddMoreListener(onLoadMoreListener: OnLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener

    }

    /* init {
         nLayoutManager =
     }*/
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tv_price: TextView? = null
        var tv_limit: TextView? = null

        var tv_amount: TextView? = null
        var tv_seller: TextView? = null
        var btn_buy: Button? = null

        fun bindView(trade: Trade) {

            tv_price = itemView.findViewById(R.id.tv_price)
            tv_limit = itemView.findViewById(R.id.tv_limit)

            tv_seller = itemView.findViewById(R.id.tv_User)
            tv_amount = itemView.findViewById(R.id.tv_amount)
            btn_buy = itemView.findViewById(R.id.btn_buy)


            tv_limit!!.setText(/*Apputils.formatCurrency(*/trade.UpperLimit.toString() + "-" +
                    /*Apputils.formatCurrency(*/trade.LowerLimit.toString())
            tv_seller!!.setText("U-" + trade.FUAC_Id)

            tv_amount!!.setText(trade.Amount )
            tv_price!!.setText(/*Apputils.formatCurrency(*/trade.Price!!)/*)*/
        }


    }


}