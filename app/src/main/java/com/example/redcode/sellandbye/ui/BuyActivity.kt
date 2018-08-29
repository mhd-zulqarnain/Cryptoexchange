package com.example.redcode.sellandbye.ui


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import com.example.redcode.sellandbye.R
import kotlinx.android.synthetic.main.activity_buy.*


class BuyActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buy)
        initView()
    }

    private fun initView() {
        tv_terms.setOnClickListener {
            showTradeDialog()

        }
    }




    private fun showTradeDialog() {
        val view: View = LayoutInflater.from(this@BuyActivity).inflate(R.layout.dilalog_terms_trade, null)
        val alertBox = android.support.v7.app.AlertDialog.Builder(this@BuyActivity)
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
