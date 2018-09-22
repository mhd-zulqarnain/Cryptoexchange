package com.company.redcode.royalcryptoexchange.adapter

import android.content.Context
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.company.redcode.royalcryptoexchange.R
import com.company.redcode.royalcryptoexchange.models.Wallet

class WalletAdapter (var datalist: ArrayList<Wallet>):
        RecyclerView.Adapter<WalletAdapter.ViewHolder>() {

    var  ctx: Context? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.wallet_cardview,parent,false)
        ctx = parent.context
        return ViewHolder(v);
    }

    override fun getItemCount(): Int {

        return datalist.size;
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val wallet : Wallet = datalist[position]
        holder?.catagooryName?.text= wallet.cat_name
        holder?.balance?.text= wallet.balance.toString()+" "+wallet.cat_abbr+" ="+" PKR"+wallet.pkr_currency.toString()
        holder?.btn_send.setOnClickListener{
            showSendDialog()
        }


    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var catagooryName = itemView.findViewById(R.id.catagory) as TextView
        var balance= itemView.findViewById(R.id.balance) as TextView
        var btn_send =itemView.findViewById(R.id.send)as Button;
    }
    private fun showSendDialog() {
        val view: View = LayoutInflater.from(ctx!!).inflate(R.layout.dialogue_wallet_send, null)
        val alertBox = AlertDialog.Builder(ctx!!)
        alertBox.setView(view)
        alertBox.setCancelable(false)
        val dialog = alertBox.create()

        var wollet_amount: EditText = view.findViewById(R.id.amount)
        var wollet_address: EditText =view.findViewById(R.id.Ed_wallet_address)
        var button_ok: Button = view.findViewById(R.id.wallet_ok)
        var button_cancel:Button = view.findViewById(R.id.wallet_cancel)
        button_ok.setOnClickListener {
            var saveable =true
            if(wollet_address.length()!=0)
                dialog.cancel()
            else
                (Toast.makeText(ctx, "Fill Send Method",Toast.LENGTH_SHORT).show())
        }
        button_cancel.setOnClickListener{
            dialog.dismiss()
        }
        dialog.show()

    }

}

