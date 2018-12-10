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
import com.company.redcode.royalcryptoexchange.models.PaymentMethod
import com.company.redcode.royalcryptoexchange.models.Response
import com.company.redcode.royalcryptoexchange.retrofit.ApiClint
import com.company.redcode.royalcryptoexchange.utils.Constants
import retrofit2.Call
import retrofit2.Callback

class UserBankAdapater(var ctx: Context, var model: ArrayList<PaymentMethod>, private val onItemClick: (Int) -> Unit) : RecyclerView.Adapter<UserBankAdapater.MyViewHolder>() {

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
        var tv_account_title:TextView? = null

        var btn_edit:Button? = null

        fun bindView(bank: PaymentMethod) {

            tv_account_name = itemView.findViewById(R.id.tv_account_name)
            tv_account_title = itemView.findViewById(R.id.tv_account_number)

            tv_account_name!!.text = bank.Type;
            tv_account_title!!.text = bank.BankName;

            btn_edit = itemView.findViewById(R.id.btn_edit)
            btn_edit!!.setOnClickListener {
//api delete

            }

        }

    }
}