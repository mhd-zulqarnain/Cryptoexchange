package com.company.redcode.royalcryptoexchange.utils

import android.content.Context
import android.content.SharedPreferences
import com.company.redcode.royalcryptoexchange.models.Users
import com.google.gson.Gson

private const val USER_PROFILE = "userProfile"

class SharedPref private constructor() {

    var mPref: SharedPreferences? = null

    companion object {
        private var sharedPref: SharedPref? = null

        fun getInstance(): SharedPref? {
            if (sharedPref == null)
                sharedPref = SharedPref()

            return sharedPref
        }
    }

    fun setProfilePref(context: Context, users: Users) {
        mPref = context.getSharedPreferences(USER_PROFILE, Context.MODE_PRIVATE)
        val editor = mPref!!.edit()
        var obj = Gson().toJson(users)
        editor.putString("userObject", obj)
        editor.apply()
    }

    fun getProfilePref(context: Context): Users {
        mPref = context.getSharedPreferences(USER_PROFILE, Context.MODE_PRIVATE)
        val editor = mPref!!.edit()
        var obj = mPref!!.getString("userObject", null)
        if (obj == null)
            return Users()
        var user = Gson().fromJson<Users>(obj, Users::class.java)
        return user
    }
    fun clearProfilePref(context: Context){
        mPref = context.getSharedPreferences(USER_PROFILE, Context.MODE_PRIVATE)
        val  editor = mPref!!.edit()
        editor.clear()
        editor.apply()
    }


}