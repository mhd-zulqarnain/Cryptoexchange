package com.company.redcode.royalcryptoexchange.auth

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast

import com.company.redcode.royalcryptoexchange.R
import com.company.redcode.royalcryptoexchange.MainActivity
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {

    private var user_email: EditText? = null
    private var ed_password: EditText? = null
    private var sign_up_progress: ProgressBar? = null
    private var auth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        user_email = findViewById(R.id.user_email)
        ed_password = findViewById(R.id.ed_password)
       // sign_up_progress = findViewById(R.id.sign_up_progress)
        auth = FirebaseAuth.getInstance()
    }

    fun signUp(v: View) {
        showVerifyDialog()
        Toast.makeText(baseContext, "Verify your account", Toast.LENGTH_SHORT).show()

        /*if (ed_password!!.text.toString().trim { it <= ' ' }.length < 8) {
            ed_password!!.error = "password is short must be greater then 8 digits"
            ed_password!!.requestFocus()
            return
        }
        if (user_email!!.text.toString() == "") {
            user_email!!.error = "password is short must be greater then 8 digits"
            user_email!!.requestFocus()
            return
        }
       /// sign_up_progress!!.visibility = View.VISIBLE
        auth!!.createUserWithEmailAndPassword(user_email!!.text.toString(), ed_password!!.text.toString()).addOnCompleteListener(this) { task ->
            if (!task.isSuccessful) {
                Toast.makeText(baseContext, "Failed", Toast.LENGTH_SHORT).show()
                sign_up_progress!!.visibility = View.GONE

            } else {
                Toast.makeText(baseContext, "Account created successfully", Toast.LENGTH_SHORT).show()
                startActivity(Intent(baseContext, MainActivity::class.java))
                sign_up_progress!!.visibility = View.GONE
                finish()

            }
        }*/
    }


    fun showVerifyDialog(){
        val view:View = LayoutInflater.from(this@SignUpActivity).inflate(R.layout.dilalog_email_verify,null)
        val alert = AlertDialog.Builder(this@SignUpActivity)
        alert.setView(view)
        alert.setCancelable(false)
        var dialog = alert.create()
        dialog.show()
        val btnVerify: Button = view.findViewById(R.id.btn_verify)
        btnVerify.setOnClickListener{

            Toast.makeText(baseContext, "Account created successfully", Toast.LENGTH_SHORT).show()
            startActivity(Intent(baseContext, MainActivity::class.java))
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

    override fun onStart() {
        if (auth!!.currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        super.onStart()
    }
}
