package com.example.redcode.sellandbye.models

data class Users(var uid: String? = null,
                 var user_email: String? = null,
                 var banks: ArrayList<Bank>? = null,
                 var password: String? = null,
                 var email: String? = null
)