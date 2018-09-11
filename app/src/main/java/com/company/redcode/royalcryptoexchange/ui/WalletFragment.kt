package com.company.redcode.royalcryptoexchange.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.company.redcode.royalcryptoexchange.R
import com.company.redcode.royalcryptoexchange.adapter.WalletAdopter
import com.company.redcode.royalcryptoexchange.models.Wallet

class WalletFragment : Fragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.wallet_fragmnt, container, false)
        initView(view)
        return view

    }

    private fun initView(view: View) {
        var wollet = arrayListOf<Wallet>()
        wollet.add(Wallet("BTC Wallet","BTC","10000",12000))
        wollet.add(Wallet("BCH Wallet","BCH","420000",4))
        wollet.add(Wallet("BTH Wallet","BTC","20000",24000))


        var recyclerView = view.findViewById(R.id.wollet_recycler) as RecyclerView
        recyclerView.layoutManager= LinearLayoutManager(context,LinearLayout.VERTICAL,false)
        var adopter = WalletAdopter(wollet)
        recyclerView.adapter=adopter


    }

}