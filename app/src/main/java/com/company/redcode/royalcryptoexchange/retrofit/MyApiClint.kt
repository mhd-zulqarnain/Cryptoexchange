package com.company.redcode.royalcryptoexchange.retrofit

import com.company.redcode.royalcryptoexchange.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

 class MyApiClint private constructor() {

     private var mRetrofit = Retrofit.Builder().baseUrl(Constants.IMAGE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    private var rService :RetrofiltService?  =null

    companion object {
        var apiClint: MyApiClint? = null

        fun getInstance(): MyApiClint? {
            if (apiClint == null)
                return MyApiClint()
            else
                return apiClint

        }
    }

    init {
        rService = mRetrofit.create(RetrofiltService::class.java)
    }

    fun getService():RetrofiltService?{
        return  rService
    }


}