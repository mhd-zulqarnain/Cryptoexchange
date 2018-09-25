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
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.company.redcode.royalcryptoexchange.R
import com.company.redcode.royalcryptoexchange.adapter.UserBankAdapater
import com.company.redcode.royalcryptoexchange.models.*
import com.company.redcode.royalcryptoexchange.retrofit.ApiClint
import com.company.redcode.royalcryptoexchange.utils.*
import com.example.admin.camerawork.CameraActivity
import com.google.gson.Gson
import com.rengwuxian.materialedittext.MaterialEditText
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import java.io.ByteArrayOutputStream
import java.util.HashMap


class ProfileFragment : Fragment() {
    var URL = "http://wpassignment123.000webhostapp.com/upload.php"
    var imagename: ArrayList<String>? = null;
    var btn_add_bank: Button? = null
    var btn_add_img: Button? = null
    var spinner_payment_method: Spinner? = null
    var bank_recycler_view: RecyclerView? = null
    val SELECT_PICTURE: Int = 4
    private val CAMERA_INTENT = 555
    private val REQUSET_GALLERY_CODE: Int = 44
    private var myImgJson: String? = null
    private var bank_view: LinearLayout? = null
    private var mobile_method_view: LinearLayout? = null
    var adapter: UserBankAdapater? = null
    var progressBar: android.app.AlertDialog? = null
    private var attach_img_1: ImageView? = null
    private var attach_img_2: ImageView? = null
    private var attach_img_3: ImageView? = null
    var fuac_id: String? = null;
    var fname: EditText? = null
    var lname: EditText? = null
    var cnic_: EditText? = null
    var phno: EditText? = null
    var pass_: EditText? = null
    var repass_: EditText? = null
    var email_: EditText? = null;
    var btnupdate: Button? = null
    var docimage: LinearLayout? = null;
    var profile_terms: MaterialEditText? = null;
    var image: String = "http://wpassignment123.000webhostapp.com/uploads/";
    var list = ArrayList<PaymentMethod>()
    var docver: String? = null;
    var sharedpref: SharedPref = SharedPref.getInstance()!!
    var spinnervalue = "Jazz Cash"
    var etbankname: EditText? = null
    var etbankcode: EditText? = null
    var etcnic: EditText? = null
    var etaccountttile: EditText? = null
    var etmobilenumber: EditText? = null
    var etaccountnumber: EditText? = null
    var btnaddpayment: Button? = null


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_profile, container, false)
        image = "uploads/";
        val builder = android.app.AlertDialog.Builder(activity!!)
        builder.setView(R.layout.layout_dialog_progress)
        builder.setCancelable(false)
        progressBar = builder.create()


        fname = view!!.findViewById(R.id.profile_fname);
        lname = view!!.findViewById(R.id.profile_lastname)
        phno = view!!.findViewById(R.id.profile_mob)
        cnic_ = view!!.findViewById(R.id.profile_cnic)
        email_ = view!!.findViewById(R.id.profile_email)
        pass_ = view!!.findViewById(R.id.profile_pass)
        repass_ = view!!.findViewById(R.id.profile_repass)
        btnupdate = view!!.findViewById(R.id.btnprofileupdate);
        profile_terms = view!!.findViewById(R.id.profile_terms);
        docimage = view!!.findViewById(R.id.docimage);




        imagename = ArrayList();
        initView(view)
        return view
    }

    private fun initView(view: View?) {

        btn_add_bank = view!!.findViewById(R.id.addaccount)
        attach_img_1 = view!!.findViewById(R.id.attach_img_1)
        attach_img_2 = view!!.findViewById(R.id.attach_img_2)
        attach_img_3 = view!!.findViewById(R.id.attach_img_3)


        //api call image document


        var obj: Users = sharedpref.getProfilePref(activity!!)
        //      var obj: Users =sharedpref.getProfilePref(activity!!)

        fname!!.setText(obj.FirstName)
        lname!!.setText(obj.LastName);
        cnic_!!.setText(obj.CNIC);
        pass_!!.setText(obj.Password);
        repass_!!.setText(obj.Password);
        email_!!.setText(obj.Email);
        phno!!.setText(obj.PhoneNum);
        docver = obj.DocumentVerification;
        if (obj.Terms != "Your Terms")
            profile_terms!!.setText(obj.Terms)

        fuac_id = obj.UAC_Id;
        val im1: ImageView = view.findViewById(R.id.attach_img_1)
        val im3: ImageView = view.findViewById(R.id.attach_img_3)
        val im2: ImageView = view.findViewById(R.id.attach_img_2)
        if (docver != "Verified") {
            ApiClint.getInstance()?.getService()?.user_document(fuac_id!!)?.enqueue(object : Callback<ArrayList<Document>> {
                override fun onFailure(call: Call<ArrayList<Document>>?, t: Throwable?) {
                    print("Error in Showing Image " + t)
                }

                override fun onResponse(call: Call<ArrayList<Document>>?, response: retrofit2.Response<ArrayList<Document>>?) {
                    if (response != null) {
                        var doclist = response!!.body();
                        var size = doclist!!.size;
                        if (size > 0) {
                            if (doclist[0] != null)
                                Picasso.with(activity!!).load(image + doclist!![0].User_Document).into(im1);
                            if (size > 1 && doclist[1] != null)
                                Picasso.with(activity!!).load(image + doclist!![1].User_Document).into(im2);
                            if (size > 2 && doclist[2] != null)
                                Picasso.with(activity!!).load(image + doclist!![2].User_Document).into(im3);
                        }
                    }
                }

            })
        } else {
            docimage!!.visibility = (View.GONE);
            var ll: LinearLayout = view.findViewById(R.id.adddoc);
            ll!!.visibility = View.GONE
        }
        btn_add_img = view!!.findViewById(R.id.btn_add_img)

        btn_add_bank!!.setOnClickListener {
            showTradeDialog()
        }
        btn_add_img!!.setOnClickListener {

            showImageAddDialog()
        }

        btnupdate!!.setOnClickListener(View.OnClickListener {
            profileValidiation();
        })

    }


    fun addbank(acc_cnic: String, acctitle: String, banknumber: String, bankcode: String) {
        ApiClint?.getInstance()?.getService()?.add_paymentdetail(fuac_id!!, spinnervalue!!, acc_cnic!!, acctitle!!, banknumber!!, bankcode!!)?.enqueue(object : Callback<PaymentMethod> {
            override fun onFailure(call: Call<PaymentMethod>?, t: Throwable?) {
                print("Error While Adding Bank Details" + t)
            }

            override fun onResponse(call: Call<PaymentMethod>?, response: retrofit2.Response<PaymentMethod>?) {
                if (response != null) {
                    var apiResponse = response.body()
                    //   if ( apiResponse!!.status == Constants.STATUS_SUCCESS) {
                    //     var status = response.body()!!.message
                    Toast.makeText(activity!!, "Payment Method Added!!", Toast.LENGTH_SHORT).show()
                    list.add(PaymentMethod(apiResponse!!.UP_Id, apiResponse!!.FUAC_Id, apiResponse!!.Type, apiResponse!!.Account, apiResponse!!.AccountTitle, apiResponse!!.BankName, apiResponse!!.BankCode))
                    adapter!!.notifyDataSetChanged();
                    //finish();
                } else {
                    Toast.makeText(activity!!, "Error!! ", Toast.LENGTH_SHORT).show()

                }
            }


        })
    }

    fun profileValidiation() {
        var firstname: String = fname!!.text.toString();
        var lastname: String = lname!!.text.toString();
        var password: String = pass_!!.text.toString();
        var repass: String = repass_!!.text.toString();
        var terms: String = profile_terms!!.text.toString()
//                    && password!="" && firstname!="" && lastname!=""


        if (fname!!.text.toString() == "") {
            fname!!.error = Html.fromHtml("<font color='black'>Enter user first name</font>")
            fname!!.requestFocus()
            return
        }
        if (lname!!.text.toString() == "") {
            lname!!.error = Html.fromHtml("<font color='black'>Enter user last name</font>")
            lname!!.requestFocus()
            return
        }
        if (pass_!!.text.toString() == "") {
            pass_!!.error = Html.fromHtml("<font color='black'>This field could not be empty</font>")
            pass_!!.requestFocus()
            return
        }

        if (pass_!!.text.toString().trim { it <= ' ' }.length < 8) {
            pass_!!.error = Html.fromHtml("<font color='black'>password is short must be greater then 8 digits</font>")
            pass_!!.requestFocus()
            return
        }
        if (fname!!.text.toString() == lname!!.text.toString()) {
            fname!!.error = Html.fromHtml("<font color='black'>Could not be same</font>")
            lname!!.error = Html.fromHtml("<font color='black'>Could not be same</font>")
            fname!!.requestFocus()
            lname!!.requestFocus()
            return
        }
        if (pass_!!.text.toString() != repass_!!.text.toString()) {
            pass_!!.error = Html.fromHtml("<font color='black'>Should be same</font>")
            repass_!!.error = Html.fromHtml("<font color='black'>Should be same</font>")
            pass_!!.requestFocus()
            repass_!!.requestFocus()
            return
        }
        if (profile_terms!!.text.toString() == "") {
            terms = "Your Terms";
            //   profile_terms!!.setText(terms);
        }
        ApiClint.getInstance()?.getService()?.update_profile(fuac_id!!, firstname!!, lastname!!, password!!, terms!!)?.enqueue(object : Callback<com.company.redcode.royalcryptoexchange.models.Response> {
            override fun onFailure(call: Call<com.company.redcode.royalcryptoexchange.models.Response>?, t: Throwable?) {
                println("error")
            }

            override fun onResponse(call: Call<com.company.redcode.royalcryptoexchange.models.Response>?, response: retrofit2.Response<com.company.redcode.royalcryptoexchange.models.Response>?) {
                if (response != null) {
                    var apiResponse = response.body()
                    if (apiResponse!!.status == Constants.STATUS_SUCCESS) {
                        var status = response.body()!!.message
                        Toast.makeText(activity!!, "Profile Updated Successfully", Toast.LENGTH_SHORT).show()
                        //finish();
                    } else {
                        Toast.makeText(activity!!, "Error!! ", Toast.LENGTH_SHORT).show()

                    }
                }
            }

        });


    }

    private fun showImageAddDialog() {
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

    private fun showTradeDialog() {

        val view: View = LayoutInflater.from(activity!!).inflate(R.layout.dialog_new_bank, null)
        val alertBox = android.support.v7.app.AlertDialog.Builder(activity!!)
        alertBox.setView(view)
        val dialog = alertBox.create()



        etaccountnumber = view!!.findViewById(R.id.et_accountnumber)
        etbankcode = view!!.findViewById(R.id.et_bankcode)
        etbankname = view!!.findViewById(R.id.et_bankname)
        etaccountttile = view!!.findViewById(R.id.et_accounttitle)
        etcnic = view!!.findViewById(R.id.et_cnic)
        etmobilenumber = view!!.findViewById(R.id.et_mobilenumber)
        btnaddpayment = view!!.findViewById(R.id.btn_addpayment)







        btnaddpayment!!.setOnClickListener { view ->


            val account: String = etaccountnumber!!.text.toString();
            val title: String = etaccountttile!!.text.toString()
            val name: String = etbankname!!.text.toString()
            val cnic: String = etcnic!!.text.toString()
            val mob: String = etmobilenumber!!.text.toString()
            val code: String = etbankcode!!.text.toString()

            if (spinnervalue == "Bank Transfer")
                addbank(title.toString(), account.toString(), name.toString(), code.toString());
            else
                addbank(cnic.toString(), "null", mob.toString(), "null");

        }


        //-------------------------------------------------------------------------
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        val btnClose: ImageView = view.findViewById(R.id.btn_close)
        bank_recycler_view = view!!.findViewById(R.id.bank_recycler_view)
        bank_view = view!!.findViewById(R.id.bank_view)
        mobile_method_view = view!!.findViewById(R.id.mobile_method_view)
        spinner_payment_method = view!!.findViewById(R.id.spinner_payment_method)

        spinner_payment_method!!.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {

                val item = parent!!.getItemAtPosition(pos);
                spinnervalue = item.toString();
                if (item.equals("Bank Transfer")) {
                    mobile_method_view!!.visibility = View.GONE
                    bank_view!!.visibility = View.VISIBLE
                } else {
                    mobile_method_view!!.visibility = View.VISIBLE
                    bank_view!!.visibility = View.GONE
                }
            }
        })


        //bank

///         list.add(Bank("ahmed", "Bank Transfer"))
        //      list.add(Bank("ahmed", "Bank Transfer"))
        //    list.add(Bank("ahmed", "Bank Transfer"))
        //  list.add(Bank("ahmed", "Bank Transfer"))

        ApiClint?.getInstance()?.getService()?.getPaymentDetailListByUid(fuac_id!!)?.enqueue(object : Callback<ArrayList<PaymentMethod>> {
            override fun onResponse(call: Call<ArrayList<PaymentMethod>>?, response: retrofit2.Response<ArrayList<PaymentMethod>>?) {
                if (response != null) {
                    var lis = response!!.body()
                    var count = lis!!.size;
                    for (i: Int in 0 until count)
                        list.add(PaymentMethod(lis.get(i).UP_Id, lis.get(i).FUAC_Id, lis.get(i).Type, lis.get(i).Account, lis.get(i).AccountTitle, lis.get(i).BankName, lis.get(i).BankCode))
                    adapter!!.notifyDataSetChanged();
                }
            }

            override fun onFailure(call: Call<ArrayList<PaymentMethod>>?, t: Throwable?) {
                print("Error!!")
            }

        })

//        list.add(PaymentMethod())
        adapter = UserBankAdapater(activity!!, list) { position ->

            //             showTradeDialog()
            progressBar!!.show()
            deletepayment(list.get(position).UP_Id, object : ServiceListener<String> {
                override fun success(obj: String) {
                    Toast.makeText(activity!!, obj, Toast.LENGTH_SHORT).show()
                    if(obj.equals("Method Deleted!!"))
                    {
                        list.removeAt(position)
                        adapter!!.notifyDataSetChanged()
                    }
                    progressBar!!.dismiss()

                }

                override fun fail(error: ServiceError) {
                    Toast.makeText(activity!!, "Trade Exists on this Payment!!", Toast.LENGTH_SHORT).show()
                    progressBar!!.dismiss()
                }

            });


        }
        var layout = LinearLayoutManager(activity!!, LinearLayout.VERTICAL, false)
        bank_recycler_view!!.layoutManager = layout
        bank_recycler_view!!.adapter = adapter


        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }


    fun deletepayment(uP_Id: String?, param: ServiceListener<String>) {

        ApiClint.getInstance()?.getService()?.delete_bank(uP_Id!!)?.enqueue(object : Callback<com.company.redcode.royalcryptoexchange.models.Response> {
            override fun onFailure(call: Call<com.company.redcode.royalcryptoexchange.models.Response>?, t: Throwable?) {
                print("Error")
            }

            override fun onResponse(call: Call<com.company.redcode.royalcryptoexchange.models.Response>?, response: retrofit2.Response<com.company.redcode.royalcryptoexchange.models.Response>?) {

                if (response != null) {
                    var api = response!!.body()
                    if (api!!.status == Constants.STATUS_SUCCESS) {
                        param.success("Method Deleted!!")

                    } else if (api!!.status == "failed") {
                        param.success("Trade Exists on this Payment!!")
                    }
                }
            }


        })
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        progressBar!!.show()
        if (requestCode == REQUSET_GALLERY_CODE && resultCode == Activity.RESULT_OK && data != null) {


            if (data.clipData != null) {
                var count: Int = data.clipData.itemCount
                if (count > 4) {
                    count = 3
                }
                for (i in 0 until count) {
                    val imageUri = data.clipData.getItemAt(i).uri
                    val bitmap = MediaStore.Images.Media.getBitmap(activity!!.getContentResolver(), imageUri)
                    if (i == 0)
                        attach_img_1!!.setImageBitmap(bitmap)
                    if (i == 1)
                        attach_img_2!!.setImageBitmap(bitmap)
                    if (i == 2)
                        attach_img_3!!.setImageBitmap(bitmap)

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

            for (i in 0 until list!!.size) {

                if (i == 0)
                    attach_img_1!!.setImageBitmap(list[i])
                if (i == 1)
                    attach_img_2!!.setImageBitmap(list[i])
                if (i == 2)
                    attach_img_3!!.setImageBitmap(list[i])

                var c: Int = list!!.size - 1;
                uploadtoserver(list[i], i, (c))

            }

        }

        // progressBar!!.dismiss()
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
                Response.Listener { response ->
                    //Toast.makeText(activity!!, response, Toast.LENGTH_SHORT).show()
                    imagename!!.add(response)

                    userdoc(fuac_id!!, response!!, object : ServiceListener<String> {
                        override fun success(obj: String) {
                            if (i == size) {
                                Toast.makeText(activity!!, "Image Uploaded!!", Toast.LENGTH_SHORT).show()
                                progressBar!!.dismiss()
                            }
                        }

                        override fun fail(error: ServiceError) {
                            Toast.makeText(activity!!, "Service error!! ", Toast.LENGTH_SHORT).show()
                        }
                    })



                    ApiClint.getInstance()?.getService()?.add_userdoc(fuac_id!!, response!!)?.enqueue(object : Callback<com.company.redcode.royalcryptoexchange.models.Response> {
                        override fun onFailure(call: Call<com.company.redcode.royalcryptoexchange.models.Response>?, t: Throwable?) {
                            //   println("error")
                        }

                        override fun onResponse(call: Call<com.company.redcode.royalcryptoexchange.models.Response>?, response: retrofit2.Response<com.company.redcode.royalcryptoexchange.models.Response>?) {
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


                }, Response.ErrorListener {
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
                params["Saving"] = image;

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
