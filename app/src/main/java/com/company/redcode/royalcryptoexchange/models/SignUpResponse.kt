package com.company.redcode.royalcryptoexchange.models

class SignUpResponse(){
    var Add_UserAccountResult: MyResponse? =null


    constructor( message: MyResponse) : this() {
//        this.status = status
        this.Add_UserAccountResult = message
    }

    override fun toString(): String {

        return "Response(message=${Add_UserAccountResult!!.IsEmailActive})"
    }

    class MyResponse(var IsEmailActive: String? = null,
                     var UAC_Id: String? = null)
}