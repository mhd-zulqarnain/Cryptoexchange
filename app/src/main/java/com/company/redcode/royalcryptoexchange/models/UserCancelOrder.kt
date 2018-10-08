package com.company.redcode.royalcryptoexchange.models

class UserCancelOrder(
                      var FORD_Id: String? = null,  //order id
                      var FUserId: String? = null,//id of user who cancel order
                      var FUT_Id: String? = null,
                      var FTrade_UserId: String? = null, //id of user  own trade
                      var Message: String? = null)