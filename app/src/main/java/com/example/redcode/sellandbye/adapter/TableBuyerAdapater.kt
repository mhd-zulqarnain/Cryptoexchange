package com.example.redcode.sellandbye.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.redcode.sellandbye.R
import com.example.redcode.sellandbye.models.Payments
import com.example.redcode.sellandbye.models.Trade

class TableBuyerAdapater(var ctx: Context, var model: ArrayList<Trade>,private val onItemClick: (Int) -> Unit) : RecyclerView.Adapter<TableBuyerAdapater.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view: MyViewHolder = MyViewHolder(LayoutInflater.from(ctx).inflate(R.layout.single_table_row_buyer, parent, false))
        return view
    }

    override fun getItemCount(): Int {
        return model.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindView(model[position])
        holder.btn_buy!!.setOnClickListener{
            onItemClick(position)
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tv_price:TextView? = null
        var tv_limit:TextView? = null

        var tv_amount:TextView? = null
        var tv_seller:TextView? = null
        var btn_buy:Button? = null

        fun bindView(trade: Trade) {

            tv_price = itemView.findViewById(R.id.tv_price)
            tv_limit = itemView.findViewById(R.id.tv_limit)

            tv_seller = itemView.findViewById(R.id.tv_User)
            tv_amount = itemView.findViewById(R.id.tv_amount)
            btn_buy = itemView.findViewById(R.id.btn_buy)

            tv_limit!!.setText(trade.d_limit.toString()+"-"+trade.u_limit.toString())
            tv_seller!!.setText(trade.user!!.uid)

            tv_amount!!.setText(trade.amount+trade.currency_type)
            tv_price!!.setText(trade.price)
        }


    }
}