package com.company.redcode.royalcryptoexchange.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.company.redcode.royalcryptoexchange.R
import com.company.redcode.royalcryptoexchange.models.Order
import com.company.redcode.royalcryptoexchange.models.Trade

class AdsDashboardAdapater(var ctx: Context, var model: ArrayList<Trade>, private val onItemClick: (Int) -> Unit) : RecyclerView.Adapter<AdsDashboardAdapater.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view: MyViewHolder = MyViewHolder(LayoutInflater.from(ctx).inflate(R.layout.single_row_ads, parent, false))
        return view
    }

    override fun getItemCount(): Int {
        return model.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindView(model[position])
        holder.btn_action!!.setOnClickListener{
            onItemClick(position)

        }
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tv_time:TextView? = null
        var tv_category:TextView? = null
        var tv_executed_amount:TextView? = null
        var tv_executed_fee:TextView? = null
        var tv_status:TextView? = null
        var btn_action:Button? = null


        fun bindView(trade: Trade) {

            tv_time = itemView.findViewById(R.id.tv_time)
            tv_category = itemView.findViewById(R.id.tv_category)
            tv_executed_amount = itemView.findViewById(R.id.tv_executed_amount)
            tv_executed_fee = itemView.findViewById(R.id.tv_executed_fee)
            tv_status = itemView.findViewById(R.id.tv_status)
            btn_action = itemView.findViewById(R.id.btn_action)

            tv_time!!.setText(trade.Date)
            tv_category!!.setText(trade.OrderType)
            tv_executed_amount!!.setText(trade.ExecutedAmount)
            tv_executed_fee!!.setText(trade.ExecutedFees)
            tv_status!!.setText(trade.Status)



        }

    }
}