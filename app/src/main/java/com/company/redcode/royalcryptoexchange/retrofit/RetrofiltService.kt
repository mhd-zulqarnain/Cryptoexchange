package com.company.redcode.royalcryptoexchange.retrofit

import com.company.redcode.royalcryptoexchange.models.*
import retrofit2.Call
import retrofit2.http.*

interface RetrofiltService {


    @POST("Add_UserAccount/{fname}/{lastName}/{email}/{mobile}/{password}/{cnic}/{dob}")
    @Headers("Content-Type:application/json")
    fun signUpUser(@Path("fname") fname: String, @Path("lastName") lastName: String, @Path("email") email: String, @Path("mobile") mobile: String, @Path("password") password: String, @Path("cnic") cnic: String, @Path("dob") dob: String): Call<Response>

    @POST("VerifyEmail/{userId}/{code}")
    @Headers("Content-Type:application/json")
    fun verifyEmail(@Path("userId") userId: String, @Path("code") code: String): Call<Response>

    @POST("Login/{userEmail}/{password}")
    @Headers("Content-Type:application/json")
    fun signIn(@Path("userEmail") userEmail: String, @Path("password") password: String): Call<Response>

    @POST("forgot_pass/{userEmail}")
    @Headers("Content-Type:application/json")
    fun sendCode(@Path("userEmail") userEmail: String): Call<Response>

    @POST("Add_UserTrades/{fuac_id}/{ordertype}/{fup_id}/{amount}/{exeamount}/{exefee}/{price}/{fees}/{ulimit}/{llimit}/{ctype}")
    @Headers("Content-Type:application/json")
    fun addTrade(@Path("fuac_id") fuac_id: String, @Path("ordertype") ordertype: String, @Path("fup_id") fup_id: String,
                 @Path("amount") amount: String, @Path("exeamount") exeamount: String, @Path("exefee") exefee: String, @Path("price") price: String, @Path("fees") fees: String,
                 @Path("ulimit") ulimit: String, @Path("llimit") llimit: String, @Path("ctype") ctype: String): Call<Response>

    //string fuacid, string ordertype, string fupid, string amnt, string exec_amount, string exec_fees, string pric, string fes, string uplimit,
// \ string lowlimit, string currencytyp

    @GET("Select_UserAccount/{userid}")
    @Headers("Content-Type:application/json")
    fun getUserById(@Path("userid") userid: String): Call<Users>

    /*get trade by coin type and ordertype*/
    @GET("Select_UserTrades/{orderType}/{coinType}")
    @Headers("Content-Type:application/json")
    fun getTrade(@Path("orderType") orderType: String, @Path("coinType") coinType: String): Call<ArrayList<Trade>>

    @POST("MobileFactor/{mbl}")
    @Headers("Content-Type:application/json")
    fun verifyMobile(@Path("mbl") mbl: String): Call<Response>

    @GET("Select_Dashboard/{fuac_id}")
    @Headers("Content-Type:application/json")
    fun getdashboardorder(@Path("fuac_id") fuac_id: String): Call<ArrayList<Trade>>

    @GET("Select_UserOrder/{fuac_id}")
    @Headers("Content-Type:application/json")
    fun getOrderById(@Path("fuac_id") fuac_id: String): Call<ArrayList<Order>>

    /*get orders by trade id*/
    @GET("Select_TradeOrder/{FUT_Id}")
    @Headers("Content-Type:application/json")
    fun getOrderByTradeId(@Path("FUT_Id") FUT_Id: String): Call<ArrayList<Order>>

    @POST("Add_UserOrder/{userid}/{ord_userid}/{fuac_id}/{fut_id}/{price}/{amount}/{payment_method}/{upperlimit}/{lowerlimit}/{bitamount}" +
            "/{bitprice}/{status}/{description}/{notify_status}")
    @Headers("Content-Type:application/json")
    fun addNewOrder(@Path("userid") userid: String, @Path("ord_userid") ord_userid: String, @Path("fuac_id") fuac_id: String, @Path("fut_id") fut_id: String,
                    @Path("price") price: String, @Path("amount") amount: String, @Path("payment_method") payment_method: String, @Path("upperlimit") upperlimit: String,
                    @Path("lowerlimit") lowerlimit: String, @Path("bitamount") bitamount: String, @Path("bitprice") bitprice: String, @Path("status") status: String,
                    @Path("description") description: String, @Path("notify_status") notify_status: String): Call<Response>

    /*get payment and terms details*/
    @GET("Select_tradeDetail/{userId}/{pid}")
    @Headers("Content-Type:application/json")
    fun gettermAndPayment(@Path("userId") userId: String, @Path("pid") pid: String): Call<OrderTerms>

    @PUT("Update_UserAccount/{fuac_id}/{fname}/{lname}/{pass}/{terms}")
    @Headers("Content-Type:application/json")
    fun update_profile(@Path("fuac_id") fuac_id: String, @Path("fname") fname: String, @Path("lname") lname: String, @Path("pass") pass: String, @Path("terms") terms: String): Call<Response>


    /*update order status*/
    @PUT("Update_UserOrder/{order_id}/{status}")
    @Headers("Content-Type:application/json")
    fun update_order_status(@Path("order_id") order_id: String, @Path("status") status: String): Call<Response>

    @POST("Add_UserDocument/{fuac_id}/{userdoc}")
    @Headers("Content-Type:application/json")
    fun add_userdoc(@Path("fuac_id") fuac_id: String, @Path("userdoc") userdoc: String): Call<Response>

    // add bank details
    @POST("Add_UserPaymentDetail/{fuac_id}/{type}/{account}/{title}/{bankname}/{bankcode}")
    @Headers("Content-Type:application/json")
    fun add_paymentdetail(@Path("fuac_id") fuac_id: String, @Path("type") type: String,@Path("account") account: String,@Path("title") title: String,@Path("bankname") bankname: String,@Path("bankcode") bankcode: String): Call<PaymentMethod>
// select images
    @GET("Select_UserDocument/{fuac_id}")
    @Headers("Content-Type:application/json")
    fun user_document(@Path("fuac_id") fuac_id: String): Call<ArrayList<Document>>

   /*update order status*/
    @GET("Select_Userpaymentdetail/{fuac_id}")
    @Headers("Content-Type:application/json")
    fun getPaymentDetailListByUid(@Path("fuac_id") fuac_id: String): Call<ArrayList<PaymentMethod>>

}