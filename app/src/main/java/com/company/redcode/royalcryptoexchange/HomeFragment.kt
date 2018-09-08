package com.company.redcode.royalcryptoexchange


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

class HomeFragment : Fragment() {

    var btn_sell:Button? = null
    var btn_buy:Button? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view  =inflater.inflate(R.layout.fragment_home, container, false)
        initView(view)
        return view

    }

    private fun initView(view: View?) {
        btn_sell = view!!.findViewById(R.id.btn_sell)
        btn_buy = view!!.findViewById(R.id.btn_sell)
        btn_sell!!.setOnClickListener{
            startActivity(Intent(activity!! ,SellActivity::class.java))
        }
        btn_buy!!.setOnClickListener{
            startActivity(Intent(activity!! ,BuyActivity::class.java))
        }
    }


}
