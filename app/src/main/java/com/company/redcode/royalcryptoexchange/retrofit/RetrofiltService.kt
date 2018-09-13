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

    @POST("Service1.svc/Add_SupportTicket/{title}/{desc}/{image}/{fucid}")
    @Headers("Content-Type:application/json")
    fun testApi( @Path("title") title: String,@Path("desc") desc: String,@Path("image") image: String,@Path("fucid") fucid: String): Call<Response>

    @POST("Service1.svc/Add_SupportTicket/{title}/{desc}/{image}/{fucid}")
    @Headers("Content-Type:application/json")
    fun postNewUser( @Path("title") title: String,@Path("desc") desc: String,@Path("image") image: String,@Path("fucid") fucid: String): Call<Response>

}