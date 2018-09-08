package com.company.redcode.royalcryptoexchange

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import com.company.redcode.royalcryptoexchange.auth.SignInActivity
import com.company.redcode.royalcryptoexchange.ui.ProfileActivity
import com.company.redcode.royalcryptoexchange.ui.TradeFragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var btnToogle: ImageButton? = null
    private var close_btn: ImageButton? = null
    private var btn_logout: TextView? = null
    private var tv_profile: TextView? = null
    private var menuList: LinearLayout? = null
    private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnToogle = findViewById(R.id.add_option)
        close_btn = findViewById(R.id.close_btn)
        menuList = findViewById(R.id.menu_layout)
        btn_logout = findViewById(R.id.btn_logout)
        tv_profile = findViewById(R.id.tv_profile)
        auth = FirebaseAuth.getInstance()


        initView()
    }

    private fun initView() {

        btn_logout!!.setOnClickListener {
            logout()
        }
        var fragentTrasaction = supportFragmentManager.beginTransaction()
        fragentTrasaction!!.add(R.id.fragment_container, TradeFragment()).commit()
        fragentTrasaction!!.addToBackStack(null)

        btnToogle!!.setOnClickListener {
            val animate = TranslateAnimation(Animation.RELATIVE_TO_PARENT, -1.0f,
                    Animation.RELATIVE_TO_PARENT, 0.0f,
                    Animation.RELATIVE_TO_PARENT, 0.0f,
                    Animation.RELATIVE_TO_PARENT, 0.0f)

            animate.duration = 500
            animate.fillAfter = true
            menuList!!.startAnimation(animate)
            btnToogle!!.visibility = View.GONE

            viewOption(View.VISIBLE)

        }

        close_btn!!.setOnClickListener {

            hideView()

        }

        tv_trade.setOnClickListener {
            //supportFragmentManager.beginTransaction().replace(R.id.fragment_container, TradeFragment()).commit()
            // hideView()

        }
        tv_home.setOnClickListener {

            supportFragmentManager.beginTransaction()!!.replace(R.id.fragment_container, TradeFragment()).commit()
            hideView()
        }

        tv_profile!!.setOnClickListener {
            startActivity(Intent(baseContext, ProfileActivity::class.java))
            hideView()

        }

    }

    private fun hideView() {

        val animate = TranslateAnimation(0f, -menuList!!.width.toFloat(), 0f, 0f)
        animate.duration = 500
        animate.fillAfter = true
        menuList!!.startAnimation(animate)
        btnToogle!!.visibility = View.VISIBLE

        viewOption(View.GONE)
    }

    private fun viewOption(visiblity: Int) {

        menuList!!.visibility = visiblity
        tv_home!!.visibility = visiblity
        tv_trade!!.visibility = visiblity
        btn_logout!!.visibility = visiblity
        close_btn!!.visibility = visiblity
        tv_profile!!.visibility = visiblity
        tv_my_transaction!!.visibility = visiblity

    }


    private fun logout() {
        startActivity(Intent(baseContext, SignInActivity::class.java))
        finish()
        /*if (auth!!.currentUser != null) {
            auth!!.signOut()
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }*/
    }
}
