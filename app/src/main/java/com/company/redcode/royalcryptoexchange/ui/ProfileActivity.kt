package com.company.redcode.royalcryptoexchange.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.company.redcode.royalcryptoexchange.R

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        var  fragentTrasaction =supportFragmentManager.beginTransaction()
        fragentTrasaction!!.add(R.id.fragment_container, ProfileFragment()).commit()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (fragment != null)
            fragment.onActivityResult(requestCode, resultCode, data)
    }
}
