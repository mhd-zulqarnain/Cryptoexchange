package com.company.redcode.royalcryptoexchange.models

data class Deals( var deal_id:String? =null,
                  var trade_amount:String? =null,
                  var trade_price:String? =null,
                  var currency_type:String? =null,
                  var expired:String? =null,
                  var status:String? =null,
                  var timespam:String? =null,
                  var comments:String? =null,
                  var transaction_type:String? =null
)