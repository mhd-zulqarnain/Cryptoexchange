package com.company.redcode.royalcryptoexchange.auth

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import com.company.redcode.royalcryptoexchange.DrawerActivity
import com.company.redcode.royalcryptoexchange.MainActivity
import com.company.redcode.royalcryptoexchange.R
import com.company.redcode.royalcryptoexchange.models.Response
import com.company.redcode.royalcryptoexchange.models.Users
import com.company.redcode.royalcryptoexchange.retrofit.ApiClint
import com.company.redcode.royalcryptoexchange.utils.Apputils
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*
import retrofit2.Call
import retrofit2.Callback
import java.text.DateFormat
import java.util.*


class SignUpActivity : AppCompatActivity() {

    private var sign_up_progress: ProgressBar? = null
    private var auth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        // sign_up_progress = findViewById(R.id.sign_up_progress)
        auth = FirebaseAuth.getInstance()
        ed_dob.setOnClickListener {
            DateDialog()
        }
    }

    fun signUp(v: View) {

           if (ed_pasword!!.text.toString().trim { it <= ' ' }.length < 8) {
               ed_pasword!!.error = "password is short must be greater then 8 digits"
               ed_pasword!!.requestFocus()
               return
           }
           if (ed_email!!.text.toString() == "") {
               ed_email!!.error = ""
               ed_email!!.requestFocus()
               return
           }
           if (ed_first_name!!.text.toString() == "") {
               ed_first_name!!.error = "Enter user first name"
               ed_first_name!!.requestFocus()
               return
           }
           if (ed_last_name!!.text.toString() == "") {
               ed_last_name!!.error = "Enter user last name"
               ed_last_name!!.requestFocus()
               return
           }
           if (ed_mobile_number!!.text.toString() == "") {
               ed_mobile_number!!.error = "This field could not be empty"
               ed_mobile_number!!.requestFocus()
               return
           }

           if (ed_cnic!!.text.toString() == "") {
               ed_cnic!!.error = "This field could not be empty"
               ed_cnic!!.requestFocus()
               return
           }
           if (ed_dob!!.text.toString() == "") {
               Toast.makeText(baseContext, "Missing date of birth ", Toast.LENGTH_SHORT).show()

               return
           }
        showVerifyDialog()

//        var dateCreate = Cal

        val date = Date()
        var dateCreated = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(date)


        Toast.makeText(baseContext, "Verify your account", Toast.LENGTH_SHORT).show()

        var user = Users(ed_first_name.text.toString(), ed_last_name.text.toString(), "0", createdDate = dateCreated, loginDate = dateCreated,
                logoutDate = dateCreated,IsActive = "0",dateOfBirth = ed_dob.text.toString() , terms = "null",documentVerification = "0",
                userId = "null",cnic = ed_cnic.text.toString(), IsPhoneNumActive = "0",Password =ed_pasword!!.text.toString() )
        println(user)

        /// sign_up_progress!!.visibility = View.VISIBLE
        /*auth!!.createUserWithEmailAndPassword(user_email!!.text.toString(), ed_password!!.text.toString()).addOnCompleteListener(this) { task ->
            if (!task.isSuccessful) {
                Toast.makeText(baseContext, "Failed", Toast.LENGTH_SHORT).show()
                sign_up_progress!!.visibility = View.GONE

            } else {
                Toast.makeText(baseContext, "Account created successfully", Toast.LENGTH_SHORT).show()
                startActivity(Intent(baseContext, MainActivity::class.java))
                sign_up_progress!!.visibility = View.GONE
                finish()

            }
        }
*/

    }

    fun postNewUser() {
        ApiClint.getInstance()?.getService()?.postNewUser("myname", ",asdsad", "Myimage .png  * ) )& ^% $%@~!@ n", "9")?.enqueue(object : Callback<Response> {
            override fun onFailure(call: Call<Response>?, t: Throwable?) {
                Apputils.showMsg(this@SignUpActivity, "failed")
                println("response " + t)
            }

            override fun onResponse(call: Call<Response>?, response: retrofit2.Response<Response>?) {
                Apputils.showMsg(this@SignUpActivity, "successfully added")
                println("response " + response!!.body())
            }
        })
        /*   override fun onFailure(call: Call<String>?, t: Throwable?) {
               Apputils.showMsg(this@SignInActivity , "failed")
               println("response "+t)


           }

           override fun onResponse(call: Call<String>?, response: Response<String>?) {
               Apputils.showMsg(this@SignInActivity , "successfully added")
               println("response "+response!!.body())


           }
       })*/

    }

    fun showVerifyDialog() {
        val view: View = LayoutInflater.from(this@SignUpActivity).inflate(R.layout.dilalog_email_verify, null)
        val alert = AlertDialog.Builder(this@SignUpActivity)
        alert.setView(view)
        alert.setCancelable(true)
        var dialog = alert.create()
        dialog.show()
        val btnVerify: Button = view.findViewById(R.id.btn_verify)
        btnVerify.setOnClickListener {

            Toast.makeText(baseContext, "Account created successfully", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, DrawerActivity::class.java)
            startActivity(intent)
            finish()
            /*   sign_up_progress!!.visibility = View.GONE*/
            dialog.dismiss()
            finish()
        }

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
            ed_dob.setText(monthOfYear.toString() + "/" + dayOfMonth.toString() + "/" + year)
        }
        val dpDialog = DatePickerDialog(this@SignUpActivity, listener, year, month, day)
        dpDialog.show()
    }

    override fun onStart() {
        if (auth!!.currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        super.onStart()
    }
}
