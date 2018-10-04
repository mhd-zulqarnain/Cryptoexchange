package com.company.redcode.royalcryptoexchange.retrofit

import com.company.redcode.royalcryptoexchange.models.*
import retrofit2.Call
import retrofit2.http.*

interface RetrofiltService {


    @POST("API/Service1.svc/Add_UserAccount/{fname}/{lastName}/{email}/{mobile}/{password}/{cnic}/{dob}")
    @Headers("Content-Type:application/json")
    fun signUpUser(@Path("fname") fname: String, @Path("lastName") lastName: String,
                   @Path("email") email: String, @Path("mobile") mobile: String,
                   @Path("password") password: String, @Path("cnic") cnic: String,
                   @Path("dob") dob: String,@Body response: Response): Call<Response>

    @POST("API/Service1.svc/VerifyEmail/{userId}/{code}")
    @Headers("Content-Type:application/json")
    fun verifyEmail(@Path("userId") userId: String, @Path("code") code: String): Call<Response>

    @POST("API/Service1.svc/Login/{userEmail}/{password}")
    @Headers("Content-Type:application/json")
    fun signIn(@Path("userEmail") userEmail: String, @Path("password") password: String, @Body response: Response): Call<Response>

    @POST("API/Service1.svc/forgot_pass/{userEmail}")
    @Headers("Content-Type:application/json")
    fun sendCode(@Path("userEmail") userEmail: String): Call<Response>

    @POST("API/Service1.svc/Add_UserTrades/{fuac_id}/{ordertype}/{fup_id}/{amount}/{exeamount}/{exefee}/{price}/{fees}/{ulimit}/{llimit}/{ctype}")
    @Headers("Content-Type:application/json")
    fun addTrade(@Path("fuac_id") fuac_id: String, @Path("ordertype") ordertype: String, @Path("fup_id") fup_id: String,
                 @Path("amount") amount: String, @Path("exeamount") exeamount: String, @Path("exefee") exefee: String, @Path("price") price: String, @Path("fees") fees: String,
                 @Path("ulimit") ulimit: String, @Path("llimit") llimit: String, @Path("ctype") ctype: String): Call<Response>

    //string fuacid, string ordertype, string fupid, string amnt, string exec_amount, string exec_fees, string pric, string fes, string uplimit,
// \ string lowlimit, string currencytyp

    @GET("API/Service1.svc/Select_UserAccount/{userid}")
    @Headers("Content-Type:application/json")
    fun getUserById(@Path("userid") userid: String): Call<Users>

    /*get trade by coin type and ordertype*/
    @GET("API/Service1.svc/Select_UserTrades/{orderType}/{coinType}/{fuacid}")
    @Headers("Content-Type:application/json")
    fun getTrade(@Path("orderType") orderType: String, @Path("coinType") coinType: String,@Path("fuacid") fuacid: String): Call<ArrayList<Trade>>

    @POST("API/Service1.svc/MobileFactor/{mbl}")
    @Headers("Content-Type:application/json")
    fun verifyMobile(@Path("mbl") mbl: String): Call<Response>

    @GET("API/Service1.svc/Select_Dashboard/{fuac_id}")
    @Headers("Content-Type:application/json")
    fun getdashboardorder(@Path("fuac_id") fuac_id: String): Call<ArrayList<Trade>>

    @GET("API/Service1.svc/Select_UserOrder/{fuac_id}")
    @Headers("Content-Type:application/json")
    fun getOrderById(@Path("fuac_id") fuac_id: String): Call<ArrayList<Order>>

    /*get orders by trade id*/
    @GET("API/Service1.svc/Select_TradeOrder/{FUT_Id}")
    @Headers("Content-Type:application/json")
    fun getOrderByTradeId(@Path("FUT_Id") FUT_Id: String): Call<ArrayList<Order>>

    @POST("API/Service1.svc/Add_UserOrder/{userid}/{ord_userid}/{fuac_id}/{fut_id}/{price}/{amount}/{payment_method}/{upperlimit}/{lowerlimit}/{bitamount}" +
            "/{bitprice}/{status}/{description}/{notify_status}")
    @Headers("Content-Type:application/json")
    fun addNewOrder(@Path("userid") userid: String, @Path("ord_userid") ord_userid: String, @Path("fuac_id") fuac_id: String, @Path("fut_id") fut_id: String,
                    @Path("price") price: String, @Path("amount") amount: String, @Path("payment_method") payment_method: String, @Path("upperlimit") upperlimit: String,
                    @Path("lowerlimit") lowerlimit: String, @Path("bitamount") bitamount: String, @Path("bitprice") bitprice: String, @Path("status") status: String,
                    @Path("description") description: String, @Path("notify_status") notify_status: String): Call<Response>

    /*get payment and terms details*/
    @GET("API/Service1.svc/Select_tradeDetail/{userId}/{pid}")
    @Headers("Content-Type:application/json")
    fun gettermAndPayment(@Path("userId") userId: String, @Path("pid") pid: String): Call<OrderTerms>

    @GET("API/Service1.svc/Update_UserAccount/{fuac_id}/{fname}/{lname}/{pass}/{terms}")
    @Headers("Content-Type:application/json")
    fun update_profile(@Path("fuac_id") fuac_id: String, @Path("fname") fname: String, @Path("lname") lname: String, @Path("pass") pass: String, @Path("terms") terms: String): Call<Response>


    /*update order status*/
    @POST("API/Service1.svc/Update_UserOrder/{order_id}/{status}")
    @Headers("Content-Type:application/json")
    fun update_order_status(@Path("order_id") order_id: String, @Path("status") status: String): Call<Response>

    @POST("API/Service1.svc/Add_UserDocument/{fuac_id}/{userdoc}")
    @Headers("Content-Type:application/json")
    fun add_userdoc(@Path("fuac_id") fuac_id: String, @Path("userdoc") userdoc: String): Call<Response>

    // add bank details
    @POST("API/Service1.svc/Add_UserPaymentDetail/{fuac_id}/{type}/{account}/{title}/{bankname}/{bankcode}")
    @Headers("Content-Type:application/json")
    fun add_paymentdetail(@Path("fuac_id") fuac_id: String, @Path("type") type: String, @Path("account") account: String, @Path("title") title: String, @Path("bankname") bankname: String, @Path("bankcode") bankcode: String): Call<PaymentMethod>

    // select images
    @GET("API/Service1.svc/Select_UserDocument/{fuac_id}")
    @Headers("Content-Type:application/json")
    fun user_document(@Path("fuac_id") fuac_id: String): Call<ArrayList<Document>>

    /*update order status*/
    @GET("API/Service1.svc/Select_Userpaymentdetail/{fuac_id}")
    @Headers("Content-Type:application/json")
    fun getPaymentDetailListByUid(@Path("fuac_id") fuac_id: String): Call<ArrayList<PaymentMethod>>

    /*get user payment id status*/
    @GET("API/Service1.svc/getupid/{tradeId}")
    @Headers("Content-Type:application/json")
    fun getUserPaymentId(@Path("tradeId") tradeId: String): Call<String>

    /*update order status*/
    @POST("API/Service1.svc/irelease/{ord_id}/{utfee}/{utamount}/{uobitamount}/{uoamount}/{ut_id}")
    @Headers("Content-Type:application/json")
    fun orderIRelease(@Path("ord_id") ord_id: String, @Path("utfee") utfee: String, @Path("utamount") utamount: String,
                      @Path("uobitamount") uobitamount: String, @Path("uoamount") uoamount: String, @Path("ut_id") ut_id: String): Call<Response>

    //delete bank detail
    @POST("API/Service1.svc/Delete_UserPaymentDetail/{UP_Id}")
    @Headers("Content-Type:application/json")
    fun delete_bank(@Path("UP_Id") UP_Id: String): Call<Response>


    //delete bank detail
    @POST("API/Service1.svc/tradestatuschange/{UT_Id}")
    @Headers("Content-Type:application/json")
    fun delete_trade(@Path("UT_Id") UT_Id: String): Call<String>

    //delete bank detail
    @POST("API/Service1.svc/UserOrder_Dispute")
    @Headers("Content-Type:application/json")
    fun addDispute(@Body userOrderDispute: UserOrderDispute): Call<String>

    //I have paid
    @POST("API/Service1.svc/UserOrder_Pay")
    @Headers("Content-Type:application/json")
    fun orderPaid(@Body userOrderPay: UserOrderPay): Call<String>

    //I have paid
    @POST("API/Service1.svc/UserCancel_Order/{bitamount}")
    @Headers("Content-Type:application/json")
    fun cancelOrder(@Path("bitamount") bitamount: String,@Body userCancelOrder: UserCancelOrder): Call<String>

  @POST("API/Service1.svc/Add_SupportTicket")
    @Headers("Content-Type:application/json")
    fun add_support(@Body supportTicket: SupportTicket): Call<String>

    //Get order by order id
    @GET("API/Service1.svc/Select_UserOrderSingle/{orderId}")
    @Headers("Content-Type:application/json")
    fun getSingleOrderById(@Path("orderId") orderId:String): Call<Order>

    //Get order by order id
    @POST("uploadFile.php")
    @Headers("Content-Type:application/json")
    fun uploadImage(@Body imageObj: ImageObj): Call<Response>

}

//"irelease/{ord_id}/{utfee}/{utamount}/{uobitamount}/{uoamount}/{ut_id}"