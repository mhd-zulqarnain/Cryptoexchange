package com.company.redcode.royalcryptoexchange.auth

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.company.redcode.royalcryptoexchange.DrawerActivity
import com.company.redcode.royalcryptoexchange.R
import com.company.redcode.royalcryptoexchange.models.Response
import com.company.redcode.royalcryptoexchange.retrofit.ApiClint
import com.company.redcode.royalcryptoexchange.utils.*
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_sign_in.*
import retrofit2.Call
import retrofit2.Callback


class SignInActivity : AppCompatActivity() {

    //    private var login_progress: ProgressBar? = null
    private var USER_KEY: String = "user id"
    private var progressDialog: AlertDialog? = null
    private var pref: SharedPref = SharedPref.getInstance()!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        // login_progress = findViewById(R.id.login_progress)

        val builder = AlertDialog.Builder(this@SignInActivity)
        builder.setCancelable(false) // if you want user to wait for some process to finish,
        builder.setView(R.layout.progress_bar)
        progressDialog = builder.create()
        initView()
    }

    private fun initView() {
        tv_forget_password.setOnClickListener {
            showforgetPasswordDialog()

        }
    }

    fun signIn(v: View) {

        if (ed_password.text.toString().trim { it <= ' ' }.length < 8) {
            ed_password.error = Html.fromHtml("<font color='black'>password is short must be greater then 8 digits</font>")
            ed_password.requestFocus()
            return
        }
        if (ed_email.text.toString() == "") {
            ed_email.error = Html.fromHtml("<font color='black'>This field could not be empty</font>")
            ed_email.requestFocus()
            return
        }
        if (!Apputils.isValidEmail(ed_email!!.text.toString()) || ed_email!!.text.toString() == "") {
            ed_email!!.error = Html.fromHtml("<font color='black'>Invalid email</font>")
            ed_email!!.requestFocus()
            return
        }
        progressDialog?.show()
        var token = FirebaseInstanceId.getInstance().getToken()
       var fcm = Response("",token.toString())
        ApiClint.getInstance()?.getService()?.signIn(ed_email!!.text.toString(), ed_password!!.text.toString(),fcm)
                ?.enqueue(object : Callback<Response> {
                    override fun onFailure(call: Call<Response>?, t: Throwable?) {
                        Apputils.showMsg(this@SignInActivity, "failed")
                        println("response " + t)
                        progressDialog?.dismiss()
                    }

                    override fun onResponse(call: Call<Response>?, response: retrofit2.Response<Response>?) {
                        println("response " + response!!.body())

                        if (response != null) {
                            var apiResponse = response.body()

                            if (apiResponse!!.status == Constants.STATUS_INACTIVE) {
                                var status = response.body()!!.message
                                val mStatus = status!!.split(" ")
                                Toast.makeText(baseContext, "Verify your email code sent to your email", Toast.LENGTH_SHORT).show()
                                showVerifyDialog(code = mStatus[1], userId = mStatus[0])
                                progressDialog?.dismiss()

                            } else if (apiResponse!!.status == Constants.STATUS_SUCCESS) {

                                val str = apiResponse.message!!.split(" ")
                                var uid = str[0]
                                var numberStatus = str[1]
                                var mbl = str[2]

                                if(numberStatus=="true"){
                                    showMblVerifyDialog(mbl)
                                }
                                Toast.makeText(baseContext, "Login successfully ", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@SignInActivity, DrawerActivity::class.java)
                                intent.putExtra(USER_KEY, uid)
                                startActivity(intent)
                                finish()
                                progressDialog?.dismiss()

                            } else if (apiResponse!!.status == Constants.STATUS_ERROR) {
                                Toast.makeText(baseContext, "Wrong password or email", Toast.LENGTH_SHORT).show()
                                progressDialog?.dismiss()
                            }
                        }

                    }
                })

    }

    fun showVerifyDialog(code: String, userId: String) {

        val view: View = LayoutInflater.from(this@SignInActivity).inflate(R.layout.dilalog_email_verify, null)
        val alert = AlertDialog.Builder(this@SignInActivity)
        alert.setView(view)
        alert.setCancelable(true)
        var dialog = alert.create()
        dialog.show()
        val btnVerify: Button = view.findViewById(R.id.btn_verify)
        val ed_code: EditText = view.findViewById(R.id.ed_code)

        btnVerify.setOnClickListener {
            if (ed_code.text.toString() != "") {
                if (ed_code.text.toString() == code) {
                    verifyEmail(code, userId, object : ServiceListener<String> {
                        override fun success(obj: String) {
                            Toast.makeText(baseContext, "Email Verified successfully please login", Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                        }

                        override fun fail(error: ServiceError) {}
                    })
                } else {
                    ed_code!!.requestFocus()
                    ed_code!!.error = Html.fromHtml("<font color='black'>Wrong code</font>")
                }

            }
        }

    }

    fun verifyEmail(code: String, userId: String, serviceListener: ServiceListener<String>) {
        ApiClint.getInstance()?.getService()?.verifyEmail(userId, code)?.enqueue(object : Callback<Response> {
            override fun onFailure(call: Call<Response>?, t: Throwable?) {}
            override fun onResponse(call: Call<Response>?, response: retrofit2.Response<Response>?) {
                serviceListener.success("success")
            }
        })
    }

    fun signUp(v: View) {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun showforgetPasswordDialog() {

        val view: View = LayoutInflater.from(this@SignInActivity).inflate(R.layout.dialog_forget_password, null)
        val alert = AlertDialog.Builder(this@SignInActivity)
        alert.setView(view)
        alert.setCancelable(true)
        var dialog = alert.create()
        //-------------------------------------------------------------------------
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        dialog.show()
        val btnSend: Button = view.findViewById(R.id.btn_recover)
        val ed_dialog_email: EditText = view.findViewById(R.id.ed_email)

        btnSend.setOnClickListener {
            progressDialog!!.show()
            recoverPassword(ed_dialog_email, object : ServiceListener<String> {
                override fun success(obj: String) {

                    if (obj == Constants.STATUS_FAILED) {
                        Toast.makeText(baseContext, "Email not exist", Toast.LENGTH_SHORT).show()
                        progressDialog!!.dismiss()

                    } else if (obj == Constants.STATUS_SUCCESS) {
                        Toast.makeText(baseContext, "Password has been send to your email", Toast.LENGTH_SHORT).show()
                        progressDialog!!.dismiss()
                        dialog.dismiss()
                    }
                }

                override fun fail(error: ServiceError) {
                }
            })
        }


    }

    fun showMblVerifyDialog(mbl: String) {

        val view: View = LayoutInflater.from(this@SignInActivity).inflate(R.layout.dialog_mobile_verify, null)
        val alert = AlertDialog.Builder(this@SignInActivity)
        alert.setView(view)
        alert.setCancelable(true)
        var dialog = alert.create()
        dialog.show()
        val btnSend: Button = view.findViewById(R.id.btnmobileverify)
        val ed_mobilecode: EditText = view.findViewById(R.id.ed_mobilecode)

        btnSend.setOnClickListener {
            progressDialog!!.show()
            verifyMobile(mbl, object : ServiceListener<String> {
                override fun success(obj: String) {

                    if (obj == Constants.STATUS_FAILED) {
                        Toast.makeText(baseContext, "Email not exist", Toast.LENGTH_SHORT).show()
                        progressDialog!!.dismiss()

                    } else if (obj == Constants.STATUS_SUCCESS) {
                        Toast.makeText(baseContext, "Password has been send to your email", Toast.LENGTH_SHORT).show()
                        progressDialog!!.dismiss()
                        dialog.dismiss()
                    }
                }

                override fun fail(error: ServiceError) {
                }
            })
        }


    }

    fun verifyMobile(code: String, serviceListener: ServiceListener<String>) {
        ApiClint.getInstance()?.getService()?.verifyMobile(code)?.enqueue(object : Callback<Response> {
            override fun onFailure(call: Call<Response>?, t: Throwable?) {}
            override fun onResponse(call: Call<Response>?, response: retrofit2.Response<Response>?) {
                if (response!!.body()!! != null) {
                    var apiResponse: Response = response.body()!!

                    if (apiResponse.status == Constants.STATUS_SUCCESS)
                        serviceListener.success(response.body()!!.message!!)
                }
            }
        })
    }

    private fun recoverPassword(ed_dialog_email: EditText, serviceListener: ServiceListener<String>) {
        progressDialog?.dismiss()

        if (!Apputils.isValidEmail(ed_dialog_email!!.text.toString()) || ed_dialog_email!!.text.toString() == "") {
            ed_dialog_email!!.error = Html.fromHtml("<font color='black'>Invalid email</font>")
            ed_dialog_email!!.requestFocus()
            return
        }

        ApiClint.getInstance()?.getService()?.sendCode(ed_dialog_email!!.text.toString())?.enqueue(object : Callback<Response> {
            override fun onFailure(call: Call<Response>?, t: Throwable?) {}
            override fun onResponse(call: Call<Response>?, response: retrofit2.Response<Response>?) {

                if (response!!.body() != null) {
                    var apiResponse: Response = response!!.body()!!

                    serviceListener.success(apiResponse.status!!)

                }
            }
        })


    }

    override fun onStart() {
        super.onStart()
//        if()
        var userId = pref.getProfilePref(this@SignInActivity).UAC_Id
        if (userId != null) {
            val intent = Intent(this@SignInActivity, DrawerActivity::class.java)
            intent.putExtra("user id", userId)
            startActivity(intent)
            finish()
        }
    }

}
