package com.company.redcode.royalcryptoexchange.models

import java.util.*

class Trade(
        var UT_Id:Int? = null,
        var FUAC_Id: Int? = null,
        var order_type: String? = null,
        var FUP_Id: Int? = null,
        var Amount: String? = null,
        var ExecutedAmount: String? = null,
        var ExecutedFees: String? = null,
        var Price: String? = null,
        var Fees: String? = null,
        var UpperLimit: Long? = null,
        var LowerLimit: Long? = null,
        var CurrencyType: String? = null,
        var Status: String? = null,
        var Date: String? = null
){

    enum class Order : Comparator<Trade>{
        ByPrice{
            override fun compare(left: Trade?, right: Trade?): Int {
                return left!!.Price!!.compareTo(right!!.Price!!)
            }
        },
        ByLimit{
            override fun compare(left: Trade?, right: Trade?): Int {
                return left!!.UpperLimit!!.compareTo(right!!.UpperLimit!!)
            }
        },
        ByAmount{

            override fun compare(left: Trade?, right: Trade?): Int {
                return left!!.Amount!!.compareTo(right!!.Amount!!)
            }
        };
        fun ascending(): Comparator<Trade> {
            return this
        }

        fun descending(): Comparator<Trade> {
            return Collections.reverseOrder(this)
        }
    }

}