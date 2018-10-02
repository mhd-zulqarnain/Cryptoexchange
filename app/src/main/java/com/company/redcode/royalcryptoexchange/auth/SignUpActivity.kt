package com.company.redcode.royalcryptoexchange.auth

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.company.redcode.royalcryptoexchange.R
import com.company.redcode.royalcryptoexchange.models.Response
import com.company.redcode.royalcryptoexchange.models.Users
import com.company.redcode.royalcryptoexchange.retrofit.ApiClint
import com.company.redcode.royalcryptoexchange.utils.Apputils
import com.company.redcode.royalcryptoexchange.utils.Constants
import com.company.redcode.royalcryptoexchange.utils.ServiceError
import com.company.redcode.royalcryptoexchange.utils.ServiceListener
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*
import retrofit2.Call
import retrofit2.Callback
import java.text.DateFormat
import java.util.*


class SignUpActivity : AppCompatActivity() {

    private var progressDialog: AlertDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        initView()
    }

    private fun initView() {
        val builder = AlertDialog.Builder(this@SignUpActivity)
        builder.setCancelable(false) // if you want user to wait for some process to finish,
        builder.setView(R.layout.progress_bar)
        progressDialog = builder.create()

        ed_dob.setOnClickListener {
            DateDialog()
        }
        ed_cnic.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {

                if (editable!!.length == 5 || editable.length == 13) {
                    editable.append('-');
                }


            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {


            }
        })






    }

    fun signUp(v: View) {

        if (ed_first_name!!.text.toString() == "") {
            ed_first_name!!.error = Html.fromHtml("<font color='black'>Enter user first name</font>")
            ed_first_name!!.requestFocus()
            return
        }
        if (ed_last_name!!.text.toString() == "") {
            ed_last_name!!.error = Html.fromHtml("<font color='black'>Enter user last name</font>")
            ed_last_name!!.requestFocus()
            return
        }
        if (!Apputils.isValidEmail(ed_email!!.text.toString()) || ed_email!!.text.toString() == "") {
            ed_email!!.error = Html.fromHtml("<font color='black'>Invalid email</font>")
            ed_email!!.requestFocus()
            return
        }


        if (ed_mobile_number!!.text.toString() == "") {
            ed_mobile_number!!.error = Html.fromHtml("<font color='black'>This field could not be empty</font>")
            ed_mobile_number!!.requestFocus()
            return
        }

        if (ed_pasword!!.text.toString() == "") {
            ed_pasword!!.error = Html.fromHtml("<font color='black'>This field could not be empty</font>")
            ed_pasword!!.requestFocus()
            return
        }

        if (ed_pasword!!.text.toString().trim { it <= ' ' }.length < 8) {
            ed_pasword!!.error = Html.fromHtml("<font color='black'>password is short must be greater then 8 digits</font>")
            ed_pasword!!.requestFocus()
            return
        }

        if (ed_cnic!!.text.toString() == "") {
            ed_cnic!!.error = Html.fromHtml("<font color='black'>This field could not be empty</font>")
            ed_cnic!!.requestFocus()
            return
        }
        if (ed_dob!!.text.toString() == "") {
            Toast.makeText(baseContext, "Missing date of birth ", Toast.LENGTH_SHORT).show()
            return
        }
        if (ed_first_name!!.text.toString() == ed_last_name.text.toString()) {
            ed_first_name!!.error = Html.fromHtml("<font color='black'>Could not be same</font>")
            ed_last_name!!.error = Html.fromHtml("<font color='black'>Could not be same</font>")
            ed_first_name!!.requestFocus()
            ed_last_name!!.requestFocus()
            return
        }

        val date = Date()
        var dateCreated = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(date)


        var user = Users(CNIC = ed_cnic.text.toString(), CreatedDate = "null",
                DateOfBirth = ed_dob.text.toString(), DocumentVerification = "Un-Verified",
                Email = ed_email.text.toString(),
                FirstName = ed_first_name.text.toString(), IsActive = "false",
                IsPhoneNumActive = "false", LastName = ed_last_name.text.toString(), LoginDate = "null", LogoutDate = "null", Password = ed_pasword.text.toString(),
                PhoneNum = "+92"+ed_mobile_number.text.toString(), Terms = "null", UAC_Id = "null", UserId = "null")
        progressDialog?.show()

        ApiClint.getInstance()?.getService()?.signUpUser(user.FirstName!!, user.LastName!!, email = user.Email!!,
                mobile = user.PhoneNum!!, password = ed_pasword.text.toString(), cnic = user.CNIC!!, dob = user.DateOfBirth!!)
                ?.enqueue(object : Callback<Response> {
                    override fun onFailure(call: Call<Response>?, t: Throwable?) {
                        Apputils.showMsg(this@SignUpActivity, "failed")
                        println("response " + t)
                        progressDialog?.dismiss()
                    }

                    override fun onResponse(call: Call<Response>?, response: retrofit2.Response<Response>?) {
                        println("response " + response!!.body())
                        progressDialog?.dismiss()

                        if (response != null) {
                            var apiResponse = response.body()
                            if (apiResponse!!.status == Constants.STATUS_INACTIVE || apiResponse!!.status == Constants.STATUS_SUCCESS) {
                                var status = response.body()!!.message
                                val mStatus = status!!.split(" ")

                                showVerifyDialog(code = mStatus[1], userId = mStatus[0])
                                Toast.makeText(baseContext, "Please verify your email", Toast.LENGTH_SHORT).show()

                            } else {
                                Toast.makeText(baseContext, "Email already verifed please login", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@SignUpActivity, SignInActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }

                    }
                })
    }

    fun showVerifyDialog(code: String, userId: String) {

        val view: View = LayoutInflater.from(this@SignUpActivity).inflate(R.layout.dilalog_email_verify, null)
        val alert = AlertDialog.Builder(this@SignUpActivity)
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
                            Toast.makeText(baseContext, "Account Verified successfully", Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                            val intent = Intent(this@SignUpActivity, SignInActivity::class.java)
                            startActivity(intent)
                            finish()
                        }

                        override fun fail(error: ServiceError) {
                            Toast.makeText(baseContext, "Unable to verify ", Toast.LENGTH_SHORT).show()
                        }
                    })
                } else {
                    ed_code!!.requestFocus()
                    ed_code!!.error = Html.fromHtml("<font color='black'>Wrong code</font>")

                }

            }
            /*val intent = Intent(this, DrawerActivity::class.java)
            startActivity(intent)
            finish()
            */
            /*   sign_up_progress!!.visibility = View.GONE*/
            //  dialog.dismiss()
            //finish()
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

    fun signIn(v: View) {
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
        finish()

    }

    fun DateDialog() {
        val cal = Calendar.getInstance()
        var day = cal.get(Calendar.DAY_OF_MONTH);
        var year = cal.get(Calendar.YEAR);
        var month = cal.get(Calendar.MONTH);

        val listener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            ed_dob.setText(dayOfMonth.toString() + "-" + monthOfYear.toString() + "-" + year)

        }
        val dpDialog = DatePickerDialog(this@SignUpActivity, listener, year, month, day)
        dpDialog.show()
    }

    override fun onStart() {
        /* if (auth!!.currentUser != null) {
             val intent = Intent(this, MainActivity::class.java)
             startActivity(intent)
             finish()
         }*/
        super.onStart()
    }
}
