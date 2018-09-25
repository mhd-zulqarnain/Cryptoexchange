package com.company.redcode.royalcryptoexchange.models

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.Image
import android.widget.ImageView


data class Wallet (
        var logo: Bitmap? = null,
        var cat_name: String? = null,
        var cat_abbr: String? = null,
        var pkr_currency: String? = null,
        var balance: Int? = null

)