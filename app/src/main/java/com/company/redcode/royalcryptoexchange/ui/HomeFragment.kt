package com.company.redcode.royalcryptoexchange.ui


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.company.redcode.royalcryptoexchange.AdvertisementActivity
import com.company.redcode.royalcryptoexchange.R

class HomeFragment : Fragment() {

    var btn_sell:Button? = null
    var btn_buy:Button? = null
    var btn_ads:Button? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view  =inflater.inflate(R.layout.fragment_home, container, false)
        initView(view)
        return view

    }

    private fun initView(view: View?) {
        btn_sell = view!!.findViewById(R.id.btn_sell)
        btn_buy = view!!.findViewById(R.id.btn_buy)
        btn_ads = view!!.findViewById(R.id.btn_ads)
        btn_sell!!.setOnClickListener{
            startActivity(Intent(activity!! , SellActivity::class.java))
        }
        btn_buy!!.setOnClickListener{
            startActivity(Intent(activity!! , BuyActivity::class.java))
        }
        btn_ads!!.setOnClickListener{
            startActivity(Intent(activity!! , AdvertisementActivity::class.java))
        }
    }


}
