package com.company.redcode.royalcryptoexchange.retrofit

import com.company.redcode.royalcryptoexchange.models.Users
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface RetrofiltService {

    @GET("users.php")
    @Headers("Content-Type:application/json")
    fun getUser(): Call<ArrayList<Users>>

    @POST("trade_post.php")
    @Headers("Content-Type:application/json")
    

}