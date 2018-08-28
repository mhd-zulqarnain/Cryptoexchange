package com.example.redcode.sellandbye.auth.auth

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView

import com.example.redcode.sellandbye.R
import com.example.redcode.sellandbye.auth.auth.auth.SignInActivity
import com.example.redcode.sellandbye.auth.auth.ui.HomeFragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var btnToogle: ImageButton? = null
    private var close_btn: ImageButton? = null
    private var btn_logout: TextView? = null
    private var menuList: LinearLayout? = null
    private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnToogle = findViewById(R.id.add_option)
        close_btn = findViewById(R.id.close_btn)
        menuList = findViewById(R.id.menu_layout)
        btn_logout = findViewById(R.id.btn_logout)
        auth = FirebaseAuth.getInstance()

        initView()
        add_option.bringToFront()
        menu_layout.bringToFront()
    }

    private fun initView() {

        btn_logout!!.setOnClickListener { logout() }
            supportFragmentManager.beginTransaction().add(R.id.fragment_container, HomeFragment()).commit()

        btnToogle!!.setOnClickListener {
            val animate = TranslateAnimation(Animation.RELATIVE_TO_PARENT, -1.0f,
                    Animation.RELATIVE_TO_PARENT, 0.0f,
                    Animation.RELATIVE_TO_PARENT, 0.0f,
                    Animation.RELATIVE_TO_PARENT, 0.0f)

            animate.duration = 500
            animate.fillAfter = true
            menuList!!.startAnimation(animate)
            menuList!!.visibility = View.VISIBLE
            btnToogle!!.visibility = View.GONE
            close_btn!!.visibility = View.VISIBLE

        }

        close_btn!!.setOnClickListener {

            hideView()

        }

        tv_trade.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, TradeFragment()).commit()
            hideView()

        }
        tv_home.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, HomeFragment()).commit()
            hideView()

        }

    }

    private fun hideView() {

        val animate = TranslateAnimation(0f, -menuList!!.width.toFloat(), 0f, 0f)
        animate.duration = 500
        animate.fillAfter = true
        menuList!!.startAnimation(animate)
        menuList!!.visibility = View.GONE
        btnToogle!!.visibility = View.VISIBLE
        close_btn!!.visibility = View.GONE
    }

    private fun logout() {
        if (auth!!.currentUser != null) {
            auth!!.signOut()
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
