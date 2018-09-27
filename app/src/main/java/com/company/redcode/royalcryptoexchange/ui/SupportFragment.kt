package com.company.redcode.royalcryptoexchange.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
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
import com.company.redcode.royalcryptoexchange.models.ImageObject
import com.company.redcode.royalcryptoexchange.models.Response
import com.company.redcode.royalcryptoexchange.retrofit.ApiClint
import com.company.redcode.royalcryptoexchange.utils.Constants
import com.company.redcode.royalcryptoexchange.utils.ServiceError
import com.company.redcode.royalcryptoexchange.utils.ServiceListener
import com.example.admin.camerawork.CameraActivity
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import java.io.ByteArrayOutputStream
import java.util.HashMap


class SupportFragment : Fragment() {
    var URL = Constants.ImageURL;
    private val CAMERA_INTENT = 555
    private val REQUSET_GALLERY_CODE: Int = 44
    var progressBar: android.app.AlertDialog? = null
    private var attach_img_1: ImageView? = null
    private var attach_img_2: ImageView? = null
    private var attach_img_3: ImageView? = null
    private var attach_img_4: ImageView? = null
    private var myImgJson: String? = null

    var btn_add: Button? = null
    var coin: String = " "
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.fragment_support, container, false)
        initView(view)

        val builder = android.app.AlertDialog.Builder(activity!!)
        builder.setView(R.layout.layout_dialog_progress)
        builder.setCancelable(false)
        progressBar = builder.create()


        return view


    }

    private fun initView(view: View) {
        btn_add = view!!.findViewById(R.id.btn_add)
        val coin_type_spinner = view.findViewById(R.id.spinner) as Spinner
        val spinnerAdapter = ArrayAdapter.createFromResource(activity!!,R.array.array_support_title, android.R.layout.simple_spinner_item)

        spinnerAdapter.setDropDownViewResource(R.layout.spinner_textview)
        coin_type_spinner.adapter = spinnerAdapter
        coin_type_spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                val item = parent!!.getItemAtPosition(pos);
                coin = item.toString()
                Log.d("Selected Item ", " "+coin)
            }
        })
        btn_add!!.setOnClickListener {
            supportImageDialoge()
        }
        attach_img_1 = view!!.findViewById(R.id.attach_img_1)
        attach_img_2 = view!!.findViewById(R.id.attach_img_2)
        attach_img_3 = view!!.findViewById(R.id.attach_img_3)
        attach_img_4 = view!!.findViewById(R.id.attach_img_4)

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
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            startActivityForResult(intent, REQUSET_GALLERY_CODE)
            dialog.dismiss()
        }
        camera_dialog.setOnClickListener {
            var intent = Intent(activity!!, CameraActivity::class.java)
            startActivityForResult(intent, CAMERA_INTENT)
            dialog.dismiss()
        }

        dialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        progressBar!!.show()
        if (requestCode == REQUSET_GALLERY_CODE && resultCode == Activity.RESULT_OK && data != null) {

            if (data.clipData != null) {
                var count: Int = data.clipData.itemCount
                if (count > 5) {
                    count = 4
                }
                for (i in 0 until count) {
                    val imageUri = data.clipData.getItemAt(i).uri
                    val bitmap = MediaStore.Images.Media.getBitmap(activity!!.getContentResolver(), imageUri)
                    if (i ==0)
                        attach_img_1!!.setImageBitmap(bitmap)
                    if (i ==1)
                        attach_img_2!!.setImageBitmap(bitmap)
                    if (i ==2)
                        attach_img_3!!.setImageBitmap(bitmap)
                    if (i ==4)
                        attach_img_4!!.setImageBitmap(bitmap)

                    uploadtoserver(bitmap, i, (count - 1))
                }

            } else if (data.data != null) {
                val imagePath = data.data

                val bitmap = MediaStore.Images.Media.getBitmap(activity!!.getContentResolver(), imagePath)
                attach_img_1!!.setImageBitmap(bitmap)
                uploadtoserver(bitmap, 2, 2)

            }




        } else if (requestCode == CAMERA_INTENT && resultCode == Activity.RESULT_OK && data != null) {
            var result = data!!.getStringExtra("camera intent")
            myImgJson = result
            var obj = Gson().fromJson(myImgJson, ImageObject::class.java)
            var list = obj.camList

            for(i in 0 until list!!.size){

                if (i ==0)
                    attach_img_1!!.setImageBitmap(list[i])
                if (i ==1)
                    attach_img_2!!.setImageBitmap(list[i])
                if (i ==2)
                    attach_img_3!!.setImageBitmap(list[i])
                if (i ==3)
                    attach_img_4!!.setImageBitmap(list[i])

                var c: Int = list!!.size - 1;
                uploadtoserver(list[i], i, (c))
            }

        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun userdoc(fuac: String, doc: String, serviceListener: ServiceListener<String>) {
        ApiClint.getInstance()?.getService()?.add_userdoc(fuac, doc)?.enqueue(object : Callback<com.company.redcode.royalcryptoexchange.models.Response> {
            override fun onFailure(call: Call<com.company.redcode.royalcryptoexchange.models.Response>?, t: Throwable?) {}

            override fun onResponse(call: Call<com.company.redcode.royalcryptoexchange.models.Response>?, response: retrofit2.Response<com.company.redcode.royalcryptoexchange.models.Response>?) {
                serviceListener.success("success")
            }
        })
    }


    fun uploadtoserver(bitmap: Bitmap, i: Int, size: Int) {

        val StrRequest = object : StringRequest(Request.Method.POST, URL,
                com.android.volley.Response.Listener { response ->
                    //Toast.makeText(activity!!, response, Toast.LENGTH_SHORT).show()
          //          imagename!!.add(response)

                    userdoc("1027", response!!, object : ServiceListener<String> {
                        override fun success(obj: String) {
                            if (i == size) {
                                Toast.makeText(activity!!, "Success", Toast.LENGTH_SHORT).show()
                                progressBar!!.dismiss()
                            }
                        }

                        override fun fail(error: ServiceError) {
                            Toast.makeText(activity!!, "Service error!! ", Toast.LENGTH_SHORT).show()
                        }
                    })



                    ApiClint.getInstance()?.getService()?.add_userdoc("1027", response!!)?.enqueue(object : Callback<Response> {
                        override fun onFailure(call: Call<Response>?, t: Throwable?) {
                            //   println("error")
                        }

                        override fun onResponse(call: Call<Response>?, response: retrofit2.Response<com.company.redcode.royalcryptoexchange.models.Response>?) {
                            if (response != null) {
                                var apiResponse = response.body()
                                if (apiResponse!!.status == Constants.STATUS_SUCCESS) {
                                    var status = response.body()!!.message
                                    // Toast.makeText(activity!!, "Image Uploaded!!", Toast.LENGTH_SHORT).show()
                                    //finish();
                                } else {
                                    //         Toast.makeText(activity!!, "Error in Image uploading!! ", Toast.LENGTH_SHORT).show()

                                }
                            }
                        }

                    });


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



