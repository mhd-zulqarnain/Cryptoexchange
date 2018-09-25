package com.company.redcode.royalcryptoexchange.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.company.redcode.royalcryptoexchange.models.PaymentMethod
import android.widget.TextView
import com.company.redcode.royalcryptoexchange.R


class CustomSpinnerAdapter( context: Context?, var resource: Int, var list: ArrayList<PaymentMethod>?) :
        ArrayAdapter<PaymentMethod>(context, resource, list) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return createItemView(position, convertView!!, parent!!)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return createItemView(position, convertView!!, parent!!)
    }

    private fun createItemView(position: Int, convertView: View, parent: ViewGroup): View {
        val view = LayoutInflater.from(context).inflate(resource, parent, false)

        val tv_bnk_name = view.findViewById(R.id.tv_bnk_name) as TextView
        val tv_bnk_title = view.findViewById(R.id.tv_bnk_title) as TextView
        tv_bnk_title.setText(list!![position].Account)
        tv_bnk_name.setText(list!![position].BankName)


        return view
    }
}