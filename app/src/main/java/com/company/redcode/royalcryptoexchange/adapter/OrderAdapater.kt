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
import com.company.redcode.royalcryptoexchange.utils.Constants

class OrderAdapater(var ctx: Context, var model: ArrayList<Order>, private val onItemClick: (Int) -> Unit) : RecyclerView.Adapter<OrderAdapater.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view: MyViewHolder = MyViewHolder(LayoutInflater.from(ctx).inflate(R.layout.single_row_order, parent, false))
        return view
    }

    override fun getItemCount(): Int {
        return model.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindView(model[position])
        holder.tv_action!!.setOnClickListener {
            onItemClick(position)
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tv_description: TextView? = null
        var tv_price: TextView? = null
        var tv_date: TextView? = null
        var tv_status: TextView? = null
        var tv_action: Button? = null

        fun bindView(order: Order) {

            tv_description = itemView.findViewById(R.id.tv_description)
            tv_price = itemView.findViewById(R.id.tv_price)
            tv_status = itemView.findViewById(R.id.tv_status)
            tv_action = itemView.findViewById(R.id.tv_action)
            tv_date = itemView.findViewById(R.id.tv_date)
            tv_date!!.text = order.Order_Date
            tv_price!!.text = order.BitPrice
            if(order.Status== Constants.STATUS_CANCEL){
                tv_status!!.text = "cancelled"
            }else
                tv_status!!.text = order.Status
            tv_description!!.text = order.Description


            //  tv_price!!.text = order.BitPrice

        }

    }
}