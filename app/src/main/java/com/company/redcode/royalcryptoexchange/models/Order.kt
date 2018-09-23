package com.company.redcode.royalcryptoexchange.models

data class Order(var ORD_Id: String? = null,
                 var User_Id: String? = null,
                 var ORD_UserId: String? = null,
                 var FUAC_Id: String? = null,
                 var FUT_Id: String? = null,
                 var Price: String? = null,
                 var Amount: String? = null,
                 var PaymentMethod: String? = null,
                 var UpperLimit: String? = null,
                 var LowerLimit: String? = null,
                 var BitAmount: String? = null,
                 var BitPrice: String? = null,
                 var Status: String? = null,
                 var Order_Date: String? = null,
                 var Expire: String? = null,
                 var Description: String? = null,
                 var Notify_Status: String? = null
)