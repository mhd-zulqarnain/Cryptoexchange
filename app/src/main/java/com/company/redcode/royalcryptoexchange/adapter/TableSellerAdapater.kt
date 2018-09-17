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


class TableSellerAdapater(var ctx: Context, var model: ArrayList<Trade>,private val onItemClick: (Int) -> Unit) : RecyclerView.Adapter<TableSellerAdapater.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view: MyViewHolder = MyViewHolder(LayoutInflater.from(ctx).inflate(R.layout.single_table_row_seller, parent, false))
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
            tv_price = itemView.findViewById(R.id.tv_price)
            tv_limit = itemView.findViewById(R.id.tv_limit)

            tv_seller = itemView.findViewById(R.id.tv_User)
            tv_amount = itemView.findViewById(R.id.tv_amount)
            btn_buy = itemView.findViewById(R.id.btn_buy)

            tv_limit!!.setText(/*Apputils.formatCurrency(*/trade.UpperLimit.toString() + "-" +
                    /*Apputils.formatCurrency(*/trade.LowerLimit.toString())
            tv_seller!!.setText("U-" + trade.FUAC_Id)

            tv_amount!!.setText(trade.Amount + trade.CurrencyType)
            tv_price!!.setText(/*Apputils.formatCurrency(*/trade.Price!!)/*)*/
        }



    }
}