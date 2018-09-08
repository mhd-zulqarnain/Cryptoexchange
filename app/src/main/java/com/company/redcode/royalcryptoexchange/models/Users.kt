package com.company.redcode.royalcryptoexchange.models

data class Users(var uid: String? = null,
                 var banks: List<Bank>? = null,
                 var password: String? = null,
                 var email: String? = null
)