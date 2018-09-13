package com.company.redcode.royalcryptoexchange.models

data class Users(var firstName: String? = null,
                 var email: String? = null,
                 var IsEmailActive: String? = null,
                 var createdDate: String? = null,
                 var loginDate: String? = null,
                 var logoutDate: String? = null,
                 var IsActive: String? = null,
                 var dateOfBirth: String? = null,
                 var terms: String? = null,
                 var documentVerification: String? = null,
                 var userId: String? = null,
                 var cnic: String? = null,
                 var IsPhoneNumActive: String? = null,
                 var Password: String? = null
)