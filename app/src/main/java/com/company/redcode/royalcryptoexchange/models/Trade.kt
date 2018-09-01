package com.company.redcode.royalcryptoexchange.models

import java.util.*

class Trade(var  tid: String? = null,
            var uid: String? = null,
            var user: Users? = null,
            var bid: String? = null,
            var u_limit: Int? = null,
            var d_limit: Int? = null,
            var banks: ArrayList<Bank>? = null,
            var dead_line: String? = null,
            var currency_type: String? = null,
            var amount: String? = null,
            var price: String? = null
){

    enum class Order : Comparator<Trade>{
        ByPrice{
            override fun compare(left: Trade?, right: Trade?): Int {
                return left!!.price!!.compareTo(right!!.price!!)
            }
        },
        ByLimit{
            override fun compare(left: Trade?, right: Trade?): Int {
                return left!!.u_limit!!.compareTo(right!!.u_limit!!)
            }
        },
        ByAmount{

            override fun compare(left: Trade?, right: Trade?): Int {
                return left!!.amount!!.compareTo(right!!.amount!!)
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