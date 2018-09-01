package com.company.redcode.royalcryptoexchange.models

data class Order(var uid: String? = null,
                 var method: String? = null,
                 var price: String? = null,
                 var currency: String? = null,
                 var limit: String? = null,
                 var timespam: String? = null,
                 var terms: String? = null,
                 var bank: Bank? = null
                 )