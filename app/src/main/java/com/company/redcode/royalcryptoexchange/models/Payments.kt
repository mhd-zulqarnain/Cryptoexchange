package com.company.redcode.royalcryptoexchange.models

import android.R.attr.name
import java.util.*


class Payments(var seller: String? = null,
               var method: String? = null,
               var price: String? = null,
               var currency: String? = null,
               var limit: Int? = null)  {

    enum class Order : Comparator<Payments> {
        ByPrice {
            override fun compare(lhs: Payments, rhs: Payments): Int {
                return lhs.price!!.compareTo(rhs.price!!)
            }
        },
        ByLimit {
            override fun compare(lhs: Payments, rhs: Payments): Int {
                return lhs.limit!!.compareTo(rhs.limit!!)
            }
        };
        fun ascending(): Comparator<Payments> {
            return this
        }

        fun descending(): Comparator<Payments> {
            return Collections.reverseOrder(this)
        }

    }
}

