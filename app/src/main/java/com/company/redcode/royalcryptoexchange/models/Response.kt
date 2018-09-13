package com.company.redcode.royalcryptoexchange.models


class Response() {

    var Add_SupportTicketResult: String? =""
    var message: String? = ""

    constructor( message: String) : this() {
//        this.status = status
        this.Add_SupportTicketResult = message
    }

    override fun toString(): String {
        return "Response(message=$Add_SupportTicketResult)"
    }

}