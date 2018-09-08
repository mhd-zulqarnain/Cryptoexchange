package com.company.redcode.royalcryptoexchange.models

class ApiResponse{
    var status :String?=null
    constructor(status:String){
        this.status = status
    }
    override fun toString(): String {
        return "Api status is " +
                "$status)"
    }

}