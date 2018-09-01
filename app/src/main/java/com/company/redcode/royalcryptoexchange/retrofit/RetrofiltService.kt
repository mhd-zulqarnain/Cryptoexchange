package com.company.redcode.royalcryptoexchange.retrofit

import com.company.redcode.royalcryptoexchange.models.ApiResponse
import com.company.redcode.royalcryptoexchange.models.Users
import retrofit2.Call
import retrofit2.http.*
import com.company.redcode.royalcryptoexchange.models.Trade

interface RetrofiltService {

    @GET("users.php")
    @Headers("Content-Type:application/json")
    fun getUser(): Call<ArrayList<Users>>

    @POST("trade_post.php")
    @Headers("Content-Type:application/json")
    fun addTrade(@Body trade: Trade): Call<ApiResponse>




}