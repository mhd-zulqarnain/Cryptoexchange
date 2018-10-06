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
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.company.redcode.royalcryptoexchange.DrawerActivity
import com.company.redcode.royalcryptoexchange.R
import com.company.redcode.royalcryptoexchange.adapter.UserBankAdapater
import com.company.redcode.royalcryptoexchange.models.*
import com.company.redcode.royalcryptoexchange.retrofit.ApiClint
import com.company.redcode.royalcryptoexchange.retrofit.MyApiClint
import com.company.redcode.royalcryptoexchange.utils.*
import com.example.admin.camerawork.CameraActivity
import com.google.gson.Gson
import com.rengwuxian.materialedittext.MaterialEditText
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.io.ByteArrayOutputStream
import java.util.HashMap


class ProfileFragment : Fragment() {
    var URL = Constants.IMAGE_URLold;

    var btn_add_bank: Button? = null
    var user_id: TextView? = null
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
    private var attach_img_4: ImageView? = null
    var fuac_id: String? = null;
    var fname: EditText? = null
    var lname: EditText? = null
    var cnic_: EditText? = null
    var phno: EditText? = null
    var pass_: EditText? = null
    var email_: EditText? = null;
    var btnupdate: Button? = null
    var docimage: LinearLayout? = null;
    var profile_terms: EditText? = null;
    var image: String = Constants.IMAGE_URL+"/"+Constants.ProfilePath;
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
    var btn_pass_change: Button? = null

    var password: String = ""

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_profile, container, false)

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
        btnupdate = view!!.findViewById(R.id.btnprofileupdate);
        profile_terms = view!!.findViewById(R.id.profile_terms);
        docimage = view!!.findViewById(R.id.docimage);
        btn_pass_change = view!!.findViewById(R.id.btn_pass_change);
        user_id = view!!.findViewById(R.id.user_id);


//        profile_terms.performContextClick()
        /*    profile_terms!!.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(editable: Editable?) {

                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (profile_terms!!.text.toString() == "") {
                        var clean = Apputils.stringClean(profile_terms!!.text.toString())
                        profile_terms!!.setText(clean)
                    }
                }
            })*/


        initView(view)
        return view
    }

    private fun initView(view: View?) {

        btn_add_bank = view!!.findViewById(R.id.addaccount)
        attach_img_1 = view!!.findViewById(R.id.attach_img_1)
        attach_img_2 = view!!.findViewById(R.id.attach_img_2)
        attach_img_3 = view!!.findViewById(R.id.attach_img_3)
        attach_img_4 = view!!.findViewById(R.id.attach_img_4)

        //api call image document


        var obj: Users = sharedpref.getProfilePref(activity!!)
        //      var obj: Users =sharedpref.getProfilePref(activity!!)

        fname!!.setText(obj.FirstName)
        lname!!.setText(obj.LastName);
        user_id!!.setText("U-"+obj.UAC_Id)
        cnic_!!.setText(obj.CNIC);
        pass_!!.setText(obj.Password);
        password = obj.Password!!

        email_!!.setText(obj.Email);
        phno!!.setText(obj.PhoneNum);
        docver = obj.DocumentVerification;
        if (obj.Terms != "Your Terms")
            profile_terms!!.setText(obj.Terms)

        fuac_id = obj.UAC_Id;
        val im1: ImageView = view.findViewById(R.id.attach_img_1)
        val im3: ImageView = view.findViewById(R.id.attach_img_3)
        val im2: ImageView = view.findViewById(R.id.attach_img_2)
        val im4: ImageView = view.findViewById(R.id.attach_img_4)
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
                            if (size > 3 && doclist[3] != null)
                                Picasso.with(activity!!).load(image + doclist!![3].User_Document).into(im4);
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
        btn_pass_change!!.setOnClickListener(View.OnClickListener {
            showChangePasswordDialog();
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
        var password: String = password
//        var repass: String = repass_!!.text.toString();
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
        /* if (pass_!!.text.toString() == "") {
             pass_!!.error = Html.fromHtml("<font color='black'>This field could not be empty</font>")
             pass_!!.requestFocus()
             return
         }*/

        /* if (pass_!!.text.toString().trim { it <= ' ' }.length < 8) {
             pass_!!.error = Html.fromHtml("<font color='black'>password is short must be greater then 8 digits</font>")
             pass_!!.requestFocus()
             return
         }*/
        if (fname!!.text.toString() == lname!!.text.toString()) {
            fname!!.error = Html.fromHtml("<font color='black'>Could not be same</font>")
            lname!!.error = Html.fromHtml("<font color='black'>Could not be same</font>")
            fname!!.requestFocus()
            lname!!.requestFocus()
            return
        }
        /*if (pass_!!.text.toString() != repass_!!.text.toString()) {
            pass_!!.error = Html.fromHtml("<font color='black'>Should be same</font>")
            repass_!!.error = Html.fromHtml("<font color='black'>Should be same</font>")
            pass_!!.requestFocus()
            repass_!!.requestFocus()
            return
        }*/
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
                     //   (activity as DrawerActivity).updateUserProfile()
                        //finish();
                        activity!!.supportFragmentManager.popBackStack()
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
            activity!!.startActivityForResult(intent, REQUSET_GALLERY_CODE)
            dialog.dismiss()
        }
        camera_dialog.setOnClickListener {
            var intent = Intent(activity!!, CameraActivity::class.java)
            activity!!.startActivityForResult(intent, CAMERA_INTENT)
            dialog.dismiss()
        }


        dialog.show()
    }

    private fun showChangePasswordDialog() {
        val view: View = LayoutInflater.from(activity!!).inflate(R.layout.dilalog_new_pass, null)
        val alertBox = AlertDialog.Builder(activity!!)
        alertBox.setView(view)
        alertBox.setCancelable(true)
        val dialog = alertBox.create()

        val ed_old_pass: EditText = view.findViewById(R.id.ed_old_pass)
        val ed_new_pass: EditText = view.findViewById(R.id.ed_new_pass)
        val ed_confirm_pass: EditText = view.findViewById(R.id.ed_confirm_pass)
        val btn_verify: Button = view.findViewById(R.id.btn_verify)

        btn_verify.setOnClickListener {
            if (ed_old_pass.text.toString() == password && ed_old_pass.text.toString().trim() != "") {
                if (ed_new_pass.text.toString().trim() != "" && ed_confirm_pass.text.toString().trim() != "") {
                    if (ed_new_pass.text.toString().trim() == ed_confirm_pass.text.toString().trim()) {
                        if (ed_new_pass!!.text.toString().trim { it <= ' ' }.length < 8) {
                            Apputils.showMsg(activity!!, "Password should be greater than 8")
                        } else {
                            password = ed_confirm_pass.text.toString()
                            Apputils.showMsg(activity!!, "New password added please update")
                            dialog.dismiss()
                        }
                    } else {
                        Apputils.showMsg(activity!!, "Password not matched")

                    }

                } else {
                    Apputils.showMsg(activity!!, "Fill all fields")
                }

            } else {
                Apputils.showMsg(activity!!, "Old Password is wrong")
            }
        }
        dialog.show()
    }

    private fun showTradeDialog() {

        list = ArrayList<PaymentMethod>()

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

            if (spinnervalue == "Bank Transfer") {
                var check: Boolean = validate(1);
                if (check) {
                    addbank(title.toString(), account.toString(), name.toString(), code.toString());
                    etaccountnumber!!.setText("")
                    etaccountttile!!.setText("")
                    etbankname!!.setText("")
                    etbankcode!!.setText("")
                }
            } else {
                var check: Boolean = validate(0);
                if (check) {
                    addbank(cnic.toString(), "null", mob.toString(), "null");
                    etcnic!!.setText("")
                    etmobilenumber!!.setText("")
                }
            }

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
                    if (obj.equals("Method Deleted!!")) {
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

    private fun validate(i: Int): Boolean {
        if (i == 0) {
            if (etcnic!!.text.toString() == "") {
                etcnic!!.error = Html.fromHtml("<font color='black'>This field could not be empty</font>")
                etcnic!!.requestFocus()
                return false
            }
            if (etmobilenumber!!.text.toString() == "") {
                etmobilenumber!!.error = Html.fromHtml("<font color='black'>This field could not be empty</font>")
                etmobilenumber!!.requestFocus()
                return false
            }

        } else if (i == 1) {
            if (etaccountnumber!!.text.toString() == "") {
                etaccountnumber!!.error = Html.fromHtml("<font color='black'>This field could not be empty</font>")
                etaccountnumber!!.requestFocus()
                return false
            }
            if (etaccountttile!!.text.toString() == "") {
                etaccountttile!!.error = Html.fromHtml("<font color='black'>This field could not be empty</font>")
                etaccountttile!!.requestFocus()
                return false
            }
            if (etbankcode!!.text.toString() == "") {
                etbankcode!!.error = Html.fromHtml("<font color='black'>This field could not be empty</font>")
                etbankcode!!.requestFocus()
                return false
            }
            if (etbankname!!.text.toString() == "") {
                etbankname!!.error = Html.fromHtml("<font color='black'>This field could not be empty</font>")
                etbankname!!.requestFocus()
                return false
            }


        }
        return true;
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
                if (count > 5) {
                    count = 4
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
                    if (i == 3)
                        attach_img_4!!.setImageBitmap(bitmap)

                    uploadtoserver(bitmap, i, (count - 1))
                }

            } else if (data.data != null) {
                val imagePath = data.data

                val bitmap = MediaStore.Images.Media.getBitmap(activity!!.getContentResolver(), imagePath)
                attach_img_1!!.setImageBitmap(bitmap)
                uploadtoserver(bitmap, 3, 3)
            }


        } else if (requestCode == CAMERA_INTENT && resultCode == Activity.RESULT_OK && data != null) {
            var result = data!!.getStringExtra("camera intent")
            myImgJson = result


            val obj = Gson().fromJson(myImgJson, ImageObject::class.java)
            val list = obj.camList

            for (i in 0 until list!!.size) {

                if (i == 0)
                    attach_img_1!!.setImageBitmap(list[i])
                if (i == 1)
                    attach_img_2!!.setImageBitmap(list[i])
                if (i == 2)
                    attach_img_3!!.setImageBitmap(list[i])
                if (i == 3)
                    attach_img_4!!.setImageBitmap(list[i])

                var c: Int = list!!.size - 1;
                uploadtoserver(list[i], i, (c))

            }

        } else
            progressBar!!.dismiss()
        super.onActivityResult(requestCode, resultCode, data)
    }


    fun userdoc(fuac: String, doc: String, serviceListener: ServiceListener<String>) {
        ApiClint.getInstance()?.getService()?.add_userdoc(fuac, doc)?.enqueue(object : Callback<com.company.redcode.royalcryptoexchange.models.Response> {
            override fun onFailure(call: Call<com.company.redcode.royalcryptoexchange.models.Response>?, t: Throwable?) {

            }

            override fun onResponse(call: Call<com.company.redcode.royalcryptoexchange.models.Response>?, response: retrofit2.Response<com.company.redcode.royalcryptoexchange.models.Response>?) {
                serviceListener.success("success")
            }
        })
    }


    fun uploadtoserver(bitmap: Bitmap, i: Int, size: Int) {
        val imageData = imageTostring(bitmap!!)
        var obj = ImageObj(imageData, Constants.ProfilePath)


        MyApiClint.getInstance()?.getService()?.uploadImage(obj)?.enqueue(object : Callback<com.company.redcode.royalcryptoexchange.models.Response> {
            override fun onFailure(call: Call<com.company.redcode.royalcryptoexchange.models.Response>?, t: Throwable?) {
                progressBar!!.dismiss()
                Toast.makeText(activity!!,"Failed",Toast.LENGTH_SHORT).show()
                println("failed")
            }

            override fun onResponse(call: Call<com.company.redcode.royalcryptoexchange.models.Response>?, response: retrofit2.Response<com.company.redcode.royalcryptoexchange.models.Response>?) {
                println("passed")
                if(response!=null){
                    var imagename: String? = null;
                    try {
                        var ob = response!!.body()
                        if (ob!!.status == "OK") {
                            imagename = ob.message!!;

                            userdoc(fuac_id!!, imagename!!, object : ServiceListener<String> {
                                override fun success(obj: String) {
                                    if (i == size) {
                                        Toast.makeText(activity!!, "Image Uploaded!!", Toast.LENGTH_SHORT).show()
                                        progressBar!!.dismiss()
                                    }
                                }

                                override fun fail(error: ServiceError) {
                                    Toast.makeText(activity!!, "Server error!! ", Toast.LENGTH_SHORT).show()
                                }
                            })

//                        ApiClint.getInstance()?.getService()?.add_userdoc(fuac_id!!, imagename!!)?.enqueue(object : Callback<com.company.redcode.royalcryptoexchange.models.Response> {
//                            override fun onFailure(call: Call<com.company.redcode.royalcryptoexchange.models.Response>?, t: Throwable?) {
//                                //   println("error")
//                            }
//
//                            override fun onResponse(call: Call<com.company.redcode.royalcryptoexchange.models.Response>?, response: retrofit2.Response<com.company.redcode.royalcryptoexchange.models.Response>?) {
//                                if (response != null) {
//                                    var apiResponse = response.body()
//                                    if (apiResponse!!.status == Constants.STATUS_SUCCESS) {
//                                        var status = response.body()!!.message
//                                        // Toast.makeText(activity!!, "Image Uploaded!!", Toast.LENGTH_SHORT).show()
//                                        //finish();
//                                    } else {
//                                        //         Toast.makeText(activity!!, "Error in Image uploading!! ", Toast.LENGTH_SHORT).show()
//
//                                    }
//                                }
//                            }
//
//                        });


                        } else if (ob!!.status == "false")
                            image = "test";
                    } catch (e:Exception){
                        Toast.makeText(activity!!,e.message,Toast.LENGTH_LONG).show()
                    }
                }


              //  Toast.makeText(activity!!,"Passed",Toast.LENGTH_SHORT).show()
                progressBar!!.dismiss()
            }
        })




    }

//    fun uploadtoserver(bitmap: Bitmap, i: Int, size: Int) {
//
//        val StrRequest = object : StringRequest(Request.Method.POST, URL,
//                Response.Listener { response ->
//                    //Toast.makeText(activity!!, response, Toast.LENGTH_SHORT).show()
//                    var imagename: String? = null;
//                    var status: String? = null;
//                    try {
//                        var json: JSONObject = JSONObject(response);
//                        status = json.getString("Status");
//                        if (status == "OK") {
//                            imagename = json.getString("Message");
//
//
//                            userdoc(fuac_id!!, imagename!!, object : ServiceListener<String> {
//                                override fun success(obj: String) {
//                                    if (i == size) {
//                                        Toast.makeText(activity!!, "Image Uploaded!!", Toast.LENGTH_SHORT).show()
//                                        progressBar!!.dismiss()
//                                    }
//                                }
//
//                                override fun fail(error: ServiceError) {
//                                    Toast.makeText(activity!!, "Server error!! ", Toast.LENGTH_SHORT).show()
//                                }
//                            })
//
//
//                            ApiClint.getInstance()?.getService()?.add_userdoc(fuac_id!!, imagename!!)?.enqueue(object : Callback<com.company.redcode.royalcryptoexchange.models.Response> {
//                                override fun onFailure(call: Call<com.company.redcode.royalcryptoexchange.models.Response>?, t: Throwable?) {
//                                    //   println("error")
//                                }
//
//                                override fun onResponse(call: Call<com.company.redcode.royalcryptoexchange.models.Response>?, response: retrofit2.Response<com.company.redcode.royalcryptoexchange.models.Response>?) {
//                                    if (response != null) {
//                                        var apiResponse = response.body()
//                                        if (apiResponse!!.status == Constants.STATUS_SUCCESS) {
//                                            var status = response.body()!!.message
//                                            // Toast.makeText(activity!!, "Image Uploaded!!", Toast.LENGTH_SHORT).show()
//                                            //finish();
//                                        } else {
//                                            //         Toast.makeText(activity!!, "Error in Image uploading!! ", Toast.LENGTH_SHORT).show()
//
//                                        }
//                                    }
//                                }
//
//                            });
//                        } else if (status == "false")
//                            imagename = "test";
//                    } catch (e: Exception) {
//
//                    }
//
//                }, Response.ErrorListener {
//            Toast.makeText(activity!!, "Error", Toast.LENGTH_SHORT).show()
//            progressBar!!.dismiss()
//        }) {
//            //@Throws(AuthFailureError::class)
//            override fun getParams(): Map<String, String> {
//                val imageData = imageTostring(bitmap!!)
//                val params = HashMap<String, String>()
//                //   params.put("image",imageData);
//                // params.put("string1","ali")
//                params["image"] = imageData
//                params["Saving"] = image;
//
//                return params
//            }
//        }
//
//        val requestQueue = Volley.newRequestQueue(activity!!)
//        requestQueue.add(StrRequest)
//
//    }

    private fun imageTostring(bitmap: Bitmap): String {
        val outStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, outStream)
        val imageBytes = outStream.toByteArray()
        return Base64.encodeToString(imageBytes, Base64.DEFAULT)
    }


}
