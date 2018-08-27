package com.example.redcode.sellandbye.auth.auth.auth

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast

import com.example.redcode.sellandbye.R
import com.example.redcode.sellandbye.auth.auth.MainActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
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
        sign_up_progress = findViewById(R.id.sign_up_progress)
        auth = FirebaseAuth.getInstance()
    }

    fun signUp(v: View) {

        if (ed_password!!.text.toString().trim { it <= ' ' }.length < 8) {
            ed_password!!.error = "password is short must be greater then 8 digits"
            ed_password!!.requestFocus()
            return
        }
        if (user_email!!.text.toString() == "") {
            user_email!!.error = "password is short must be greater then 8 digits"
            user_email!!.requestFocus()
            return
        }
        sign_up_progress!!.visibility = View.VISIBLE
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
