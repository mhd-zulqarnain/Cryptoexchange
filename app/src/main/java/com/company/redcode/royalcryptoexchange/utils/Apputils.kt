package com.company.redcode.royalcryptoexchange.utils

import android.app.Activity
import android.text.TextUtils
import android.widget.Toast
import java.text.DecimalFormat
import java.text.SimpleDateFormat

class Apputils{
    companion object {
        fun showMsg(ctx:Activity, msg:String){
            Toast.makeText(ctx , msg ,Toast.LENGTH_LONG).show()
        }
        /*fun formatCurrency(amount: String): String {
            val formatter = DecimalFormat("###,###,##")
            return formatter.format(java.lang.Double.parseDouble(amount))
        }*/

        fun isValidEmail(target: CharSequence): Boolean {
            return if (TextUtils.isEmpty(target)) {
                false
            } else {
                android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches()
            }
        }

        fun getTimeStamp(time: String): String? {
            try {
                val dateFormat = SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa")
                val parsedDate = dateFormat.parse(time)
                val time = parsedDate.getTime()
                return time.toString()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return null
        }
    }
}