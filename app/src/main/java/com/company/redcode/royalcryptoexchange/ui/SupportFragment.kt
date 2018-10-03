package com.company.redcode.royalcryptoexchange.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.text.Html
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.company.redcode.royalcryptoexchange.R
import com.company.redcode.royalcryptoexchange.models.ApiResponse
import com.company.redcode.royalcryptoexchange.models.ImageObject
import com.company.redcode.royalcryptoexchange.models.Response
import com.company.redcode.royalcryptoexchange.models.SupportTicket
import com.company.redcode.royalcryptoexchange.retrofit.ApiClint
import com.company.redcode.royalcryptoexchange.utils.Constants
import com.company.redcode.royalcryptoexchange.utils.ServiceError
import com.company.redcode.royalcryptoexchange.utils.ServiceListener
import com.company.redcode.royalcryptoexchange.utils.SharedPref
import com.example.admin.camerawork.CameraActivity
import com.google.gson.Gson
import com.rengwuxian.materialedittext.MaterialEditText
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.io.ByteArrayOutputStream
import java.util.HashMap


class SupportFragment : Fragment() {
    var URL = Constants.IMAGE_URLold;
    private val CAMERA_INTENT = 555
    private val REQUSET_GALLERY_CODE: Int = 44
    var progressBar: android.app.AlertDialog? = null
    private val MY_PERMISSIONS_REQUEST_CAMERA = 999
    private var attach_img_1: ImageView? = null
    private var attach_img_2: ImageView? = null
    private var attach_img_3: ImageView? = null
    private var attach_img_4: ImageView? = null
    private var myImgJson: String? = null
    var sharedpref: SharedPref = SharedPref.getInstance()!!
    var et_supportmessage: MaterialEditText? = null
    var btn_supportsubmit: Button? = null
    var btn_add: Button? = null
    var coin: String = " "
    var image: String = "a"
    var fuac_id: String? = null
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.fragment_support, container, false)
        initView(view)

        val builder = android.app.AlertDialog.Builder(activity!!)
        builder.setView(R.layout.layout_dialog_progress)
        builder.setCancelable(false)
        progressBar = builder.create()

        fuac_id = sharedpref.getProfilePref(activity!!).UAC_Id!!

        return view


    }

    private fun initView(view: View) {
        btn_add = view!!.findViewById(R.id.btn_add)


        btn_add!!.setOnClickListener {
            supportImageDialoge()
        }
        attach_img_1 = view!!.findViewById(R.id.attach_img_1)
        attach_img_2 = view!!.findViewById(R.id.attach_img_2)
        attach_img_3 = view!!.findViewById(R.id.attach_img_3)
        attach_img_4 = view!!.findViewById(R.id.attach_img_4)
        btn_supportsubmit = view!!.findViewById(R.id.btn_supportsubmit)
        et_supportmessage = view!!.findViewById(R.id.et_supportmessage)


        btn_supportsubmit!!.setOnClickListener { v ->

            validate()

        }

    }

    fun validate() {
        if (et_supportmessage!!.text.toString() == "") {
            et_supportmessage!!.error = Html.fromHtml("<font color='black'>This field could not be empty</font>")
            et_supportmessage!!.requestFocus()
            return
        }

        if (image == "") {
            Toast.makeText(activity!!, "Please Upload Image", Toast.LENGTH_SHORT).show()
            return
        }
        if(image=="test"){
            Toast.makeText(activity!!,"Error in Image uploading",Toast.LENGTH_SHORT).show()
            return;
        }


        progressBar!!.show()
        var support = SupportTicket(null, coin, et_supportmessage!!.text.toString(), image, fuac_id!!)
        ApiClint.getInstance()?.getService()?.add_support(support)?.enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>?, t: Throwable?) {
                print("error")
            }

            override fun onResponse(call: Call<String>?, response: retrofit2.Response<String>?) {
                if (response != null) {
                    var api: String? = response!!.body();
                    if (api == "success")
                        Toast.makeText(activity!!, "Your Support has been Added", Toast.LENGTH_SHORT).show()
                    else if (api == "fail")
                        Toast.makeText(activity!!, "Unable to Add Support", Toast.LENGTH_SHORT).show()
                progressBar!!.dismiss()
                }
            }

        })

    }


    private fun supportImageDialoge() {
        val view: View = LayoutInflater.from(activity!!).inflate(R.layout.select_image_dialog, null)
        val alertBox = AlertDialog.Builder(activity!!)
        alertBox.setView(view)
        alertBox.setCancelable(true)
        val dialog = alertBox.create()

        val gallery_dialog: ImageView = view.findViewById(R.id.gallery_dialog)
        val camera_dialog: ImageView = view.findViewById(R.id.camera_dialog)

        gallery_dialog.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
           // intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            activity!!.startActivityForResult(intent, REQUSET_GALLERY_CODE)
            dialog.dismiss()
        }
        camera_dialog.setOnClickListener {


            if (ContextCompat.checkSelfPermission(activity!!,
                            Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                activity!!.startActivityForResult(intent, CAMERA_INTENT)

            } else {
                askForCameraPermission()
                Toast.makeText(activity!!, "Please Allow app to Use Camera of your Device. Thanks", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()


        }

        dialog.show()
    }


    private fun askForCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity!!, Manifest.permission.CAMERA)) {
            Snackbar.make(view!!.findViewById<View>(android.R.id.content), "Need permission for loading data", Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                    View.OnClickListener {
                        //asking all required permissions
                        ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.CAMERA), MY_PERMISSIONS_REQUEST_CAMERA)
                    }).show()
        } else {
            ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.CAMERA),
                    MY_PERMISSIONS_REQUEST_CAMERA)
        }
    }


    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        progressBar!!.show()
        if (requestCode == REQUSET_GALLERY_CODE && resultCode == Activity.RESULT_OK && data != null) {

            if (data.data != null) {
                val imagePath = data.data

                val bitmap = MediaStore.Images.Media.getBitmap(activity!!.getContentResolver(), imagePath)
                attach_img_1!!.setImageBitmap(bitmap)
                uploadtoserver(bitmap, 2, 2)

            }


        } else if (requestCode == CAMERA_INTENT && resultCode == Activity.RESULT_OK && data != null) {
//            Bitmap image = (Bitmap) data.getExtras().get("data");
            var result: Uri = data!!.data
//            myImgJson = result

            val bitmap = MediaStore.Images.Media.getBitmap(activity!!.getContentResolver(), result)
            attach_img_1!!.setImageBitmap(bitmap)
            uploadtoserver(bitmap, 2, 2)
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




    fun uploadtoserver(bitmap: Bitmap, i: Int, size: Int) {

        val StrRequest = object : StringRequest(Request.Method.POST, URL,
                com.android.volley.Response.Listener { response ->
                    //Toast.makeText(activity!!, response, Toast.LENGTH_SHORT).show()
                    //          imagename!!.add(response)
                    try {
                        var json : JSONObject = JSONObject(response);
                        var status = json.get("Status");
                        if(status == "OK")
                            image = json.getString("Message");
                        else if(status == "false")
                            image = "test";

                    }catch (e:Exception){

                    }
                Toast.makeText(activity!!, image, Toast.LENGTH_SHORT).show()
            progressBar!!.dismiss()

                }, com.android.volley.Response.ErrorListener {
            Toast.makeText(activity!!, "Error", Toast.LENGTH_SHORT).show()
            progressBar!!.dismiss()
        }) {
            //@Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val imageData = imageTostring(bitmap!!)
                val params = HashMap<String, String>()
                //   params.put("image",imageData);
                // params.put("string1","ali")
                params["image"] = imageData
                params["Saving"] = Constants.SupportPath;

                return params
            }
        }

        val requestQueue = Volley.newRequestQueue(activity!!)
        requestQueue.add(StrRequest)

    }

    private fun imageTostring(bitmap: Bitmap): String {
        val outStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
        val imageBytes = outStream.toByteArray()
        return Base64.encodeToString(imageBytes, Base64.DEFAULT)
    }

}



