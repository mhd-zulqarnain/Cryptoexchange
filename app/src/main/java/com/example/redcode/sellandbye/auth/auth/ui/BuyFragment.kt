package com.example.redcode.sellandbye.auth.auth.ui


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import com.example.redcode.sellandbye.R



class BuyFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
      var view = inflater.inflate(R.layout.fragment_buy, container, false)
        var tv_terms = view.findViewById<TextView>(R.id.tv_terms)
        tv_terms.setOnClickListener {
            showTradeDialog()

        }
        return view
    }


    private fun showTradeDialog() {
        val view: View = LayoutInflater.from(activity!!).inflate(R.layout.dilalog_terms_trade, null)
        val alertBox = android.support.v7.app.AlertDialog.Builder(activity!!)
        alertBox.setView(view)
        alertBox.setCancelable(false)
        val dialog = alertBox.create()

        val btnSave: Button = view.findViewById(R.id.btn_save)


        btnSave.setOnClickListener {
            dialog.dismiss()
        }


        dialog.show()
    }

}
