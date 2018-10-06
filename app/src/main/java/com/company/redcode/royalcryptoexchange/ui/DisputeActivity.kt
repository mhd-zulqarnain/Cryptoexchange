package com.company.redcode.royalcryptoexchange.ui

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.company.redcode.royalcryptoexchange.R
import com.company.redcode.royalcryptoexchange.models.*
import com.company.redcode.royalcryptoexchange.retrofit.ApiClint
import com.company.redcode.royalcryptoexchange.retrofit.MyApiClint
import com.company.redcode.royalcryptoexchange.utils.*
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_dispute.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.io.ByteArrayOutputStream
import java.util.HashMap

class DisputeActivity : AppCompatActivity() {
    var order: Order = Order()
    var activityType: String = ""
    var URL = Constants.IMAGE_URLold;
    private val CAMERA_INTENT = 555
    var image="";
    var path = "";
    private val REQUSET_GALLERY_CODE: Int = 44
    var progressBar: android.app.AlertDialog? = null
    private val MY_PERMISSIONS_REQUEST_CAMERA = 999
    var toolbar: Toolbar? = null

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dispute)

        var obj = intent.getStringExtra("order");
        activityType = intent.getStringExtra("activity");
        order = Gson().fromJson(obj, Order::class.java)
        toolbar = findViewById(R.id.toolbar_top)

        val builder = AlertDialog.Builder(this@DisputeActivity)
        builder.setView(R.layout.layout_dialog_progress)
        builder.setCancelable(false)
        progressBar = builder.create()

        initView()
        btn_back.setOnClickListener {
            finish()
        }

    }

    private fun initView() {

        ed_seller_id.setText(order.User_Id)
        ed_Buyer_id.setText("U-" + order.FUAC_Id)
        ed_price_id.setText(order.BitPrice)

        getPayementId(object : ServiceListener<String> {
            override fun success(obj: String) {
                getTerm(obj)
            }

            override fun fail(error: ServiceError) {}
        })
        btn_submit_dispute.setOnClickListener {
            //            orderRelease(order.ORD_Id, trade.Fees, trade.Amount, order.BitAmount, order.Amount, trade.UT_Id)

            if (activityType == "dispute") {
                addispute()
                updateStatus(Constants.STATUS_DISPUTE, order.ORD_Id!!)
            }
            if (activityType == "paid") {
                paymentPaid()
                updateStatus(Constants.STATUS_IN_PROGRESS, order.ORD_Id!!)
            }
            if (activityType == "cancel") {
                cancelOrder()
                updateStatus(Constants.STATUS_CANCEL, order.ORD_Id!!)
            }
        }
        if (activityType == "dispute") {
            image_view.visibility = View.VISIBLE
            tv_title.text = "Create Dispute"
            path = Constants.DisputePath;
            // image work

            ed_dispute_msg.hint = "Enter the dispute messege"
            add_image.text = "Upload prove"
        }
        if (activityType == "paid") {
            image_view.visibility = View.VISIBLE
            tv_title.text = "Upload the Recipt"
            ed_dispute_msg.visibility = View.GONE
            path = Constants.OrderReceiptPath;
            // image work
            add_image.text = "Upload script"
            btn_submit_dispute.text = "Done"
        }
        if (activityType == "cancel") {
            image_view.visibility = View.GONE
            tv_title.text = "Cancel Order"
            ed_dispute_msg.hint = "Enter a Reason for order cancelling"
            add_image.visibility = View.GONE
            image_view.visibility = View.GONE
            btn_submit_dispute.text = "Done"
        }
        add_image!!.setOnClickListener{
            DisputeImage();


        }

    }
    private fun DisputeImage() {
        val view: View = LayoutInflater.from(this@DisputeActivity).inflate(R.layout.select_image_dialog, null)
        val alertBox = android.support.v7.app.AlertDialog.Builder(this@DisputeActivity)
        alertBox.setView(view)
        alertBox.setCancelable(true)
        val dialog = alertBox.create()

        val gallery_dialog: ImageView = view.findViewById(R.id.gallery_dialog)
        val camera_dialog: ImageView = view.findViewById(R.id.camera_dialog)

        gallery_dialog.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            // intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            startActivityForResult(intent, REQUSET_GALLERY_CODE)
            dialog.dismiss()
        }
        camera_dialog.setOnClickListener {


            if (ContextCompat.checkSelfPermission(this@DisputeActivity,
                            Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, CAMERA_INTENT)

            } else {
                askForCameraPermission()
                Toast.makeText(this@DisputeActivity, "Please Allow app to Use Camera of your Device. Thanks", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()


        }

        dialog.show()
    }


    private fun askForCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this@DisputeActivity, Manifest.permission.CAMERA)) {
            Snackbar.make(findViewById<View>(android.R.id.content), "Need permission for loading data", Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                    View.OnClickListener {
                        //asking all required permissions
                        ActivityCompat.requestPermissions(this@DisputeActivity, arrayOf(Manifest.permission.CAMERA), MY_PERMISSIONS_REQUEST_CAMERA)
                    }).show()
        } else {
            ActivityCompat.requestPermissions(this@DisputeActivity, arrayOf(Manifest.permission.CAMERA),
                    MY_PERMISSIONS_REQUEST_CAMERA)
        }
    }



    fun uploadtoserver(bitmap: Bitmap, i: Int, size: Int) {
        val imageData = imageTostring(bitmap!!)
        var obj = ImageObj(imageData, path)


        MyApiClint.getInstance()?.getService()?.uploadImage(obj)?.enqueue(object : Callback<com.company.redcode.royalcryptoexchange.models.Response> {
            override fun onFailure(call: Call<com.company.redcode.royalcryptoexchange.models.Response>?, t: Throwable?) {
                progressBar!!.dismiss()
                Toast.makeText(this@DisputeActivity,"Failed",Toast.LENGTH_SHORT).show()
                println("failed")
            }

            override fun onResponse(call: Call<com.company.redcode.royalcryptoexchange.models.Response>?, response: retrofit2.Response<Response>?) {
                println("passed")
                if(response!=null){
                    var ob = response!!.body()
                    if(ob!!.status== "OK")
                        image = ob.message!!;
                    else if(ob!!.status == "false")
                        image = "test";
                }
                progressBar!!.dismiss()
            }
        })




    }

    private fun imageTostring(bitmap: Bitmap): String {
        val outStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, outStream)
        val imageBytes = outStream.toByteArray()
        return Base64.encodeToString(imageBytes, Base64.DEFAULT)
    }


    fun getPayementId(serviceListener: ServiceListener<String>) {
        progressBar!!.show()
        ApiClint.getInstance()?.getService()?.getUserPaymentId(order.FUT_Id!!)!!.enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>?, t: Throwable?) {
                println("ERROR")
                progressBar!!.dismiss()
                Apputils.showMsg(this@DisputeActivity, "Network error")
            }

            override fun onResponse(call: Call<String>?, response: retrofit2.Response<String>?) {
                progressBar!!.dismiss()
                if (response != null) {
                    if (response.body() != "0") {
                        serviceListener.success(response.body()!!)
                    }
                }
            }
        })
    }

    fun getTerm(pid: String) {
        progressBar!!.show()
        var ownerId = order.User_Id.toString().substring(2)
        ApiClint.getInstance()?.getService()?.gettermAndPayment(ownerId, pid)?.enqueue(object : Callback<OrderTerms> {
            override fun onFailure(call: Call<OrderTerms>?, t: Throwable?) {
                progressBar!!.dismiss()
            }

            override fun onResponse(call: Call<OrderTerms>?, response: retrofit2.Response<OrderTerms>?) {
                progressBar!!.dismiss()
                if (response != null) {
                    var orderTerms = response.body()!!

                    if (orderTerms.PaymentMethod!!.Type == "Bank")
                        tv_payment.text = "Type: " + orderTerms.PaymentMethod!!.Type + "\nCode:" + orderTerms.PaymentMethod!!.BankCode
                    else {
                        tv_payment.setText("Type: " + orderTerms.PaymentMethod!!.Type + "\n Number:" + orderTerms.PaymentMethod!!.BankName)
                    }

                }
            }
        })

    }

    fun updateStatus(status: String, order_id: String) {
        ApiClint.getInstance()?.getService()?.update_order_status(order_id, status)!!.enqueue(object : Callback<com.company.redcode.royalcryptoexchange.models.Response> {
            override fun onFailure(call: Call<com.company.redcode.royalcryptoexchange.models.Response>?, t: Throwable?) {
                print("error " + t)
            }

            override fun onResponse(call: Call<com.company.redcode.royalcryptoexchange.models.Response>?, response: retrofit2.Response<Response>?) {
                if (response?.body() != null) {
                }
            }

        })
    }

    fun addispute() {

        if (ed_dispute_msg.text.toString() == "") {
            Apputils.showMsg(this@DisputeActivity, "Please set a messege")
            ed_dispute_msg!!.requestFocus()
            return
        }
        var userOrderDispute = UserOrderDispute()


        //image


        userOrderDispute.FUAC_Id = order.FUAC_Id
        userOrderDispute.FUT_Id = order.FUT_Id
        userOrderDispute.FORD_Id = order.ORD_Id
        userOrderDispute.Image = image
        userOrderDispute.Message = ed_dispute_msg.text.toString()
        userOrderDispute.UOD_Id = "0"
        userOrderDispute.UserId = order.User_Id

        if(image=="test"){
            Toast.makeText(this@DisputeActivity,"Error in Image uploading",Toast.LENGTH_SHORT).show()
            return;
        }
        if(image=="") {
            Toast.makeText(this@DisputeActivity, "Please Upload Image", Toast.LENGTH_SHORT).show()
            return;
        }

        progressBar!!.show()
        ApiClint.getInstance()?.getService()?.addDispute(userOrderDispute)!!.enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>?, t: Throwable?) {
                print("error " + t)
                progressBar!!.dismiss()
            }

            override fun onResponse(call: Call<String>?, response: retrofit2.Response<String>?) {
                if (response?.body() != null) {
                    setResult(RESULT_OK);
                    finish()
                    progressBar!!.dismiss()
                }
            }

        })
    }

    fun paymentPaid() {


        var userOrderPay = UserOrderPay()

        userOrderPay.FUAC_Id = order.FUAC_Id
        userOrderPay.FUT_Id = order.FUT_Id
        userOrderPay.Image = image
        userOrderPay.UserId = order.User_Id
        userOrderPay.FORD_Id = order.ORD_Id

        progressBar!!.show()
        ApiClint.getInstance()?.getService()?.orderPaid(userOrderPay)!!.enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>?, t: Throwable?) {
                print("error " + t)
                progressBar!!.dismiss()
            }

            override fun onResponse(call: Call<String>?, response: retrofit2.Response<String>?) {
                if (response?.body() != null) {
                    setResult(RESULT_OK);
                    finish()
                    progressBar!!.dismiss()
                }
            }

        })
    }

    fun cancelOrder() {

        if (ed_dispute_msg.text.toString() == "") {
            Apputils.showMsg(this@DisputeActivity, "Please set a messege")
            ed_dispute_msg!!.requestFocus()
            return
        }
        var userCancelOrder = UserCancelOrder()

        userCancelOrder.FORD_Id = order.ORD_Id
        userCancelOrder.FUT_Id = order.FUT_Id
        userCancelOrder.FUserId = SharedPref.getInstance()!!.getProfilePref(this@DisputeActivity).UAC_Id
        userCancelOrder.FTrade_UserId = order.User_Id

        progressBar!!.show()
        ApiClint.getInstance()?.getService()?.cancelOrder(order.BitAmount!!,userCancelOrder)!!.enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>?, t: Throwable?) {
                print("error " + t)
                progressBar!!.dismiss()
            }

            override fun onResponse(call: Call<String>?, response: retrofit2.Response<String>?) {
                if (response?.body() != null) {
                    setResult(RESULT_OK);
                    finish()
                    progressBar!!.dismiss()
                }
            }

        })
    }
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        progressBar!!.show()
        if (requestCode == REQUSET_GALLERY_CODE && resultCode == Activity.RESULT_OK && data != null) {

            if (data.data != null) {
                val imagePath = data.data

                val bitmap = MediaStore.Images.Media.getBitmap(this@DisputeActivity.getContentResolver(), imagePath)
                image_view!!.setImageBitmap(bitmap)
                uploadtoserver(bitmap, 2, 2)

            }


        } else if (requestCode == CAMERA_INTENT && resultCode == Activity.RESULT_OK && data != null) {
//            Bitmap image = (Bitmap) data.getExtras().get("data");
            val imageBitmap = data.extras.get("data") as Bitmap
//            myImgJson = result

//            val bitmap = MediaStore.Images.Media.getBitmap(this@DisputeActivity.getContentResolver(), result)
            image_view!!.setImageBitmap(imageBitmap)
            uploadtoserver(imageBitmap, 2, 2)
            /* var obj = Gson().fromJson(myImgJson, ImageObject::class.java)
             var list = obj.camList
 */
//            for (i in 0 until list!!.size) {
//
//                if (i == 0)
//                    attach_img_1!!.setImageBitmap(list[i])
//                if (i == 1)
//                    attach_img_2!!.setImageBitmap(list[i])
//                if (i == 2)
//                    attach_img_3!!.setImageBitmap(list[i])
//                if (i == 3)
//                    attach_img_4!!.setImageBitmap(list[i])
//
//                var c: Int = list!!.size - 1;
//                uploadtoserver(list[i], i, (c))
//            }

        }
        else
            progressBar!!.dismiss()
        super.onActivityResult(requestCode, resultCode, data)
    }


}