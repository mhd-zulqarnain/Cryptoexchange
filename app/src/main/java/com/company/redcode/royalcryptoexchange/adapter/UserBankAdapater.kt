package com.company.redcode.royalcryptoexchange.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.company.redcode.royalcryptoexchange.R
import com.company.redcode.royalcryptoexchange.models.Bank
import com.company.redcode.royalcryptoexchange.models.Payments

class UserBankAdapater(var ctx: Context, var model: ArrayList<Bank>, private val onItemClick: (Int) -> Unit) : RecyclerView.Adapter<UserBankAdapater.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view: MyViewHolder = MyViewHolder(LayoutInflater.from(ctx).inflate(R.layout.single_bank_row, parent, false))
        return view
    }

    override fun getItemCount(): Int {
        return model.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindView(model[position])
        holder.btn_edit!!.setOnClickListener{
            onItemClick(position)
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tv_account_name:TextView? = null
        var tv_account_number:TextView? = null

        var btn_edit:Button? = null

        fun bindView(bank: Bank) {

            tv_account_name = itemView.findViewById(R.id.tv_account_name)
            tv_account_number = itemView.findViewById(R.id.tv_account_number)

            btn_edit = itemView.findViewById(R.id.btn_edit)


        }


    }
}