package com.company.redcode.royalcryptoexchange.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.company.redcode.royalcryptoexchange.R
import com.company.redcode.royalcryptoexchange.models.Trade
import com.company.redcode.royalcryptoexchange.utils.OnLoadMoreListener
import com.company.redcode.royalcryptoexchange.utils.SharedPref


class TableBuyerAdapater(var ctx: Context, var model: ArrayList<Trade>, private val onItemClick: (Int) -> Unit) : RecyclerView.Adapter<TableBuyerAdapater.MyViewHolder>() {
    var num = 1
    var data = model
    var context:Context = ctx
    var onLoadMoreListener: OnLoadMoreListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view: MyViewHolder = MyViewHolder(LayoutInflater.from(ctx).inflate(R.layout.single_table_row_buyer, parent, false))
        return view
    }


    override fun getItemCount(): Int {
        return if (num * 5 > data.size) {
            data.size
        } else {
            num * 5
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindView(model[position],ctx)
        holder.btn_buy!!.setOnClickListener {
            if(model[position].FUAC_Id.toString() == SharedPref.getInstance()!!.getProfilePref(ctx).UAC_Id)
            {
                Toast.makeText(ctx,"You can not place order on your own trade",Toast.LENGTH_LONG).show()
            }else
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


        fun bindView(trade: Trade, ctx: Context) {

            tv_price = itemView.findViewById(R.id.tv_price)
            tv_limit = itemView.findViewById(R.id.tv_limit)

            tv_seller = itemView.findViewById(R.id.tv_User)
            tv_amount = itemView.findViewById(R.id.tv_amount)
            btn_buy = itemView.findViewById(R.id.btn_buy)
            var user_status: ImageView = itemView.findViewById(R.id.user_status)

            tv_limit!!.setText(/*Apputils.formatCurrency(*/trade.UpperLimit.toString() + "-" +
                    /*Apputils.formatCurrency(*/trade.LowerLimit.toString())
            tv_seller!!.setText("U-" + trade.FUAC_Id)


            tv_amount!!.setText(trade.Amount )
            tv_price!!.setText(/*Apputils.formatCurrency(*/trade.Price!!)/*)*/

            var resources = ctx.getResources()
            if(trade.ut_status=="0"){
                user_status.setImageDrawable(resources.getDrawable(R.drawable.ic_offline))
            }else{
                user_status.setImageDrawable(resources.getDrawable(R.drawable.ic_online))

            }
        }


    }


}