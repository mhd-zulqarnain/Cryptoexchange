package com.company.redcode.royalcryptoexchange.models


class Response() {

    var status: String? =""
    var message: String? = ""

    constructor( status: String, message: String) : this() {
        this.status = status
        this.message = message
    }

    override fun toString(): String {
        return "Response(message=$message status=$status)"
    }

}