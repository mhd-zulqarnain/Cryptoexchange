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
import android.widget.ProgressBar
import android.widget.Toast
import com.company.redcode.royalcryptoexchange.DrawerActivity
import com.company.redcode.royalcryptoexchange.R
import com.company.redcode.royalcryptoexchange.models.Response
import com.company.redcode.royalcryptoexchange.retrofit.ApiClint
import com.company.redcode.royalcryptoexchange.utils.Apputils
import com.company.redcode.royalcryptoexchange.utils.Constants
import com.company.redcode.royalcryptoexchange.utils.ServiceError
import com.company.redcode.royalcryptoexchange.utils.ServiceListener
import kotlinx.android.synthetic.main.activity_sign_in.*
import retrofit2.Call
import retrofit2.Callback


class SignInActivity : AppCompatActivity() {

    private var login_progress: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        // login_progress = findViewById(R.id.login_progress)
    }

    fun signIn(v: View) {

        if (ed_password!!.text.toString().trim { it <= ' ' }.length < 8) {
            ed_password!!.error = "password is short must be greater then 8 digits"
            ed_password!!.requestFocus()
            return
        }
        if (ed_email.text.toString() == "") {
            ed_email!!.error = "This field could not be empty"
            ed_email!!.requestFocus()
            return
        }
        if (!Apputils.isValidEmail(ed_email!!.text.toString()) || ed_email!!.text.toString() == "") {
            ed_email!!.error = Html.fromHtml("<font color='black'>Invalid email</font>")
            ed_email!!.requestFocus()
            return
        }

        ApiClint.getInstance()?.getService()?.signIn( ed_email!!.text.toString(),ed_password!!.text.toString())
                ?.enqueue(object : Callback<Response> {
                    override fun onFailure(call: Call<Response>?, t: Throwable?) {
                        Apputils.showMsg(this@SignInActivity, "failed")
                        println("response " + t)
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

                            } else if (apiResponse!!.status == Constants.STATUS_SUCCESS) {
                                Toast.makeText(baseContext, "Login successfully ", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@SignInActivity, DrawerActivity::class.java)
                                startActivity(intent)
                                finish()

                            } else if (apiResponse!!.status == Constants.STATUS_ERROR) {
                                Toast.makeText(baseContext, "Wrong password or email", Toast.LENGTH_SHORT).show()
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

}
