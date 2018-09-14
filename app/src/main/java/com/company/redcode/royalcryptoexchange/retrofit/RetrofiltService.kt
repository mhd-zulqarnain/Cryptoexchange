package com.company.redcode.royalcryptoexchange.retrofit

import com.company.redcode.royalcryptoexchange.models.*
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

    @POST("Service1.svc/Add_SupportTicket/{title}/{desc}/{image}/{fucid}")
    @Headers("Content-Type:application/json")
    fun testApi( @Path("title") title: String,@Path("desc") desc: String,@Path("image") image: String,@Path("fucid") fucid: String): Call<Response>

    @POST("Service1.svc/Add_SupportTicket/{title}/{desc}/{image}/{fucid}")
    @Headers("Content-Type:application/json")
    fun postNewUser( @Path("title") title: String,@Path("desc") desc: String,@Path("image") image: String,@Path("fucid") fucid: String): Call<Response>

    @POST("Service1.svc/Add_UserAccount/{fname}/{lastName}/{email}/{mobile}/{password}/{cnic}/{dob}")
    @Headers("Content-Type:application/json")
    fun signUpUser( @Path("fname") fname: String,@Path("lastName") lastName: String,@Path("email") email: String,@Path("mobile") mobile: String,@Path("password") password: String,@Path("cnic") cnic: String,@Path("dob") dob: String): Call<SignUpResponse>

}