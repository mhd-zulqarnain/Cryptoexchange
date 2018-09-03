package com.company.redcode.royalcryptoexchange.utils

import android.app.Activity
import android.content.Context
import android.widget.Toast

class Apputils{
    companion object {
        fun showMsg(ctx:Activity, msg:String){
            Toast.makeText(ctx , msg ,Toast.LENGTH_LONG).show()
        }


    }
}