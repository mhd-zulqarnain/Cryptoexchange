package com.company.redcode.royalcryptoexchange.models

data class OrderTerms(var PaymentMethod: PaymentMethod? = null,
                      var Terms: String?=null
                      )