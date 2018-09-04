package com.company.redcode.royalcryptoexchange.utils

import android.app.Activity
import android.widget.Toast
import java.text.DecimalFormat

class Apputils{
    companion object {
        fun showMsg(ctx:Activity, msg:String){
            Toast.makeText(ctx , msg ,Toast.LENGTH_LONG).show()
        }
        fun formatCurrency(amount: String): String {
            val formatter = DecimalFormat("###,###,##")
            return formatter.format(java.lang.Double.parseDouble(amount))
        }


    }
}