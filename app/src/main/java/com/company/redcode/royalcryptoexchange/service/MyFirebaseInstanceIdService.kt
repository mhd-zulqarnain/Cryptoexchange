package com.company.redcode.royalcryptoexchange.service

import android.util.Log

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService


class MyFirebaseInstanceIdService : FirebaseInstanceIdService() {

    override fun onTokenRefresh() {
//        val auth = FirebaseAuth.getInstance()
        val refreshedToken = FirebaseInstanceId.getInstance().token

     /*   if (auth.currentUser != null) {
            val ref = FirebaseDatabase.getInstance().getReference(auth.currentUser!!.uid).child("token")
            ref.setValue(refreshedToken)
        }*/

        Log.d("token", "onTokenRefresh: " + refreshedToken!!)

    }
}
//redcodetechnologies12@34