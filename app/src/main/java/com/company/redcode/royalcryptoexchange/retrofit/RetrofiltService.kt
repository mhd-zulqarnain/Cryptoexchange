package com.company.redcode.royalcryptoexchange.retrofit

import com.company.redcode.royalcryptoexchange.models.ApiResponse
import com.company.redcode.royalcryptoexchange.models.Response
import com.company.redcode.royalcryptoexchange.models.Trade
import com.company.redcode.royalcryptoexchange.models.Users
import retrofit2.Call
import retrofit2.http.*

interface RetrofiltService {

    @GET("users.php")
    @Headers("Content-Type:application/json")
    fun getUser(): Call<ArrayList<Users>>

    @POST("trade_post.php")
    @Headers("Content-Type:application/json")
    fun addTrade(@Body trade: Trade): Call<ApiResponse>

    /*@GET("get_trade.php")
    @Headers("Content-Type:application/json")
    fun getTrade(): Call<ArrayList<Trade>>*/

    @GET("get_trade.php")
    @Headers("Content-Type:application/json")
    fun getTradeByType(@Query("currency_type") currency_type:String, @Query("deal_type") deal_type:String ): Call<ArrayList<Trade>>

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

    @GET("Select_UserAccount/{userid}")
    @Headers("Content-Type:application/json")
    fun getUserById(@Path("userid") userid:String): Call<Users>


}