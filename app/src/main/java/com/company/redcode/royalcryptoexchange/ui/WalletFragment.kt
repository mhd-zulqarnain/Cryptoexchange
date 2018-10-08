package com.company.redcode.royalcryptoexchange.ui


import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.company.redcode.royalcryptoexchange.R
import com.company.redcode.royalcryptoexchange.adapter.WalletAdapter
import com.company.redcode.royalcryptoexchange.models.Wallet

class WalletFragment : Fragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.fragment_wallet, container, false)
        initView(view)
        return view

    }

    private fun initView(view: View) {
        var wollet = arrayListOf<Wallet>()
        wollet.add(Wallet(BitmapFactory.decodeResource(resources, R.mipmap.openbitcoin),"BTC","BTC","10000",475))
        wollet.add(Wallet(BitmapFactory.decodeResource(resources, R.mipmap.bchcoin),"BCH","BCH","420000",9864))
        wollet.add(Wallet(BitmapFactory.decodeResource(resources, R.mipmap.btggold),"BTG","BTG","10000",1243))
        wollet.add(Wallet(BitmapFactory.decodeResource(resources, R.mipmap.ethhcoin),"ETH","ETH","20000",9803))


        var recyclerView = view.findViewById(R.id.wollet_recycler) as RecyclerView
        recyclerView.layoutManager= LinearLayoutManager(context,LinearLayout.VERTICAL,false)
        var adopter = WalletAdapter(wollet)
        recyclerView.adapter=adopter


    }

}