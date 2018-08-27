package com.example.redcode.sellandbye.auth.auth.models

import java.util.*

data class Payments(var seller: String? = null,
                    var method: String? = null,
                    var price: String? = null,
                    var currency: String? = null,
                    var limit: String? = null)