package com.company.redcode.royalcryptoexchange.retrofit

import com.company.redcode.royalcryptoexchange.models.ApiResponse
import com.company.redcode.royalcryptoexchange.models.Response
import com.company.redcode.royalcryptoexchange.models.Trade
import com.company.redcode.royalcryptoexchange.models.Users
import retrofit2.Call
import retrofit2.http.*

interface RetrofiltService {


    @POST("Add_UserAccount/{fname}/{lastName}/{email}/{mobile}/{password}/{cnic}/{dob}")
    @Headers("Content-Type:application/json")
    fun signUpUser( @Path("fname") fname: String,@Path("lastName") lastName: String,@Path("email") email: String,@Path("mobile") mobile: String,@Path("password") password: String,@Path("cnic") cnic: String,@Path("dob") dob: String): Call<Response>

    @POST("VerifyEmail/{userId}/{code}")
    @Headers("Content-Type:application/json")
    fun verifyEmail( @Path("userId") userId: String,@Path("code") code: String): Call<Response>

    @POST("Login/{userEmail}/{password}")
    @Headers("Content-Type:application/json")
    fun signIn( @Path("userEmail") userEmail: String,@Path("password") password: String): Call<Response>

    @POST("forgot_pass/{userEmail}")
    @Headers("Content-Type:application/json")
    fun sendCode( @Path("userEmail") userEmail: String): Call<Response>

    @POST("Add_UserTrades/{fuac_id}/{ordertype}/{fup_id}/{amount}/{price}/{fees}/{ulimit}/{llimit}/{ctype}")
    @Headers("Content-Type:application/json")
    fun addTrade( @Path("fuac_id") fuac_id: String,@Path("ordertype") ordertype: String,@Path("fup_id") fup_id: String,@Path("amount") amount: String,@Path("price") price: String,@Path("fees") fees: String,@Path("ulimit") ulimit: String,@Path("llimit") llimit: String,@Path("ctype") ctype: String): Call<Response>

    @GET("Select_UserAccount/{userid}")
    @Headers("Content-Type:application/json")
    fun getUserById(@Path("userid") userid:String): Call<Users>


}