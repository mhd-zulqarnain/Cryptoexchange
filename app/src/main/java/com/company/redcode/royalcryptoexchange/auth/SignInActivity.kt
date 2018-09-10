package com.company.redcode.royalcryptoexchange.auth

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import com.company.redcode.royalcryptoexchange.DrawerActivity
import com.company.redcode.royalcryptoexchange.R

class SignInActivity : AppCompatActivity() {

    private var user_email: EditText? = null
    private var ed_password: EditText? = null
    private var login_progress: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        user_email = findViewById(R.id.user_email)
        ed_password = findViewById(R.id.ed_password)
        login_progress = findViewById(R.id.login_progress)
    }

    fun signIn(v: View) {

      /*  if (ed_password!!.text.toString().trim { it <= ' ' }.length < 8) {
            ed_password!!.error = "password is short must be greater then 8 digits"
            ed_password!!.requestFocus()
            return
        }
        if (user_email!!.text.toString() == "") {
            user_email!!.error = "password is short must be greater then 8 digits"
            user_email!!.requestFocus()
            return
        }
        login_progress!!.visibility = View.VISIBLE*/
        val intent = Intent(this, DrawerActivity::class.java)
        startActivity(intent)

        finish()

    }

    fun signUp(v: View) {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
        finish()
    }
/*
    override fun onStart() {
        if (auth!!.currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        super.onStart()
    }*/



}
