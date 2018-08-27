package com.example.redcode.sellandbye.auth.auth.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.redcode.sellandbye.R
import com.example.redcode.sellandbye.auth.auth.models.Payments

class TableBuyerAdapater(var ctx: Context, var model: ArrayList<Payments>) : RecyclerView.Adapter<TableBuyerAdapater.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view: MyViewHolder = MyViewHolder(LayoutInflater.from(ctx).inflate(R.layout.single_table_row_buyer,parent,false))
        return view
    }

    override fun getItemCount(): Int {
        return model.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindView(model[position])
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tv_price:TextView? = null
        var tv_limit:TextView? = null
        var tv_method:TextView? = null
        var tv_seller:TextView? = null
        var btn_buy:Button? = null

        fun bindView(payments: Payments) {
            tv_price = itemView.findViewById(R.id.tv_price)
            tv_limit = itemView.findViewById(R.id.tv_limit)
            tv_seller = itemView.findViewById(R.id.tv_seller)
            tv_method = itemView.findViewById(R.id.tv_method)
            btn_buy = itemView.findViewById(R.id.btn_buy)

            tv_limit!!.setText(payments.limit+" "+payments.currency)
            tv_method!!.setText(payments.method)
            tv_seller!!.setText(payments.seller)
            tv_price!!.setText(payments.price+" \n "+payments.currency)
        }


    }
}