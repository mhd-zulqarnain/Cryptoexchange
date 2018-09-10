package com.company.redcode.royalcryptoexchange

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.company.redcode.royalcryptoexchange.auth.SignInActivity
import gr.net.maroulis.library.EasySplashScreen

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val config = EasySplashScreen(this@SplashActivity)
                .withFullScreen()
                .withTargetActivity(SignInActivity::class.java)
                .withSplashTimeOut(3000)
                .withLogo(R.mipmap.ic_launcher_logo)

        val view = config.create()
        setContentView(view)
    }
}
