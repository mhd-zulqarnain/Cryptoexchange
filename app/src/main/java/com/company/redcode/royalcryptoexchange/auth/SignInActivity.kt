package com.company.redcode.royalcryptoexchange.auth

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import com.company.redcode.royalcryptoexchange.R
import com.company.redcode.royalcryptoexchange.models.Response
import com.company.redcode.royalcryptoexchange.retrofit.ApiClint
import com.company.redcode.royalcryptoexchange.utils.Apputils
import retrofit2.Call
import retrofit2.Callback


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
        login_progress!!.visibility = View.VISIBLE

        /*val intent = Intent(this, DrawerActivity::class.java)
        startActivity(intent)*/

        finish()

       // postNewUser()
    }

    fun signUp(v: View) {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
        finish()
    }


    fun postNewUser(){
        ApiClint.getInstance()?.getService()?.postNewUser("myname",",asdsad","Myimage .png  * ) )& ^% $%@~!@ n","9")?.enqueue(object :Callback<Response>{
            override fun onFailure(call: Call<Response>?, t: Throwable?) {
                Apputils.showMsg(this@SignInActivity , "failed")
                println("response "+t)
            }
            override fun onResponse(call: Call<Response>?, response: retrofit2.Response<Response>?) {
                Apputils.showMsg(this@SignInActivity , "successfully added")
                println("response "+response!!.body())
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
