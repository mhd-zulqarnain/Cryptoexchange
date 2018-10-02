package com.company.redcode.royalcryptoexchange

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.company.redcode.royalcryptoexchange.auth.SignInActivity
import com.company.redcode.royalcryptoexchange.models.Users
import com.company.redcode.royalcryptoexchange.retrofit.ApiClint
import com.company.redcode.royalcryptoexchange.ui.*
import com.company.redcode.royalcryptoexchange.utils.ServiceError
import com.company.redcode.royalcryptoexchange.utils.ServiceListener
import com.company.redcode.royalcryptoexchange.utils.SharedPref
import kotlinx.android.synthetic.main.activity_drawer.*
import kotlinx.android.synthetic.main.app_bar_drawer.*
import retrofit2.Call
import retrofit2.Callback
import android.support.v4.widget.DrawerLayout


class DrawerActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var userId: String? = null
    private var USER_KEY: String = "user id"
    private var mPref = SharedPref.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer)
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout,
                toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()


        supportFragmentManager.beginTransaction().add(R.id.relativeLayout, HomeFragment()).commit();
        nav_view.setNavigationItemSelectedListener(this)
        userId = intent.getStringExtra(USER_KEY)
        //updateUserProfile()

    }

//    fun updateUserProfile(){
//        getuserData(userId, object : ServiceListener<Users> {
//            override fun success(obj: Users) {
//                mPref!!.setProfilePref(this@DrawerActivity, obj)
//            }
//
//            override fun fail(error: ServiceError) {}
//        })
//    }
    private fun getuserData(userId: String?, serviceListener: ServiceListener<Users>) {
        ApiClint.getInstance()?.getService()?.getUserById(userId!!)!!.enqueue(object : Callback<Users> {
            override fun onFailure(call: Call<Users>?, t: Throwable?) {
                Toast.makeText(this@DrawerActivity, "Shared prefrence error", Toast.LENGTH_SHORT).show()

            }

            override fun onResponse(call: Call<Users>?, response: retrofit2.Response<Users>?) {
                print("object success ")
                if (response!!.body() != null) {
                    serviceListener.success(response.body()!!)
                }
            }
        })

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_logout -> {
                mPref!!.clearProfilePref(this@DrawerActivity)
                startActivity(Intent(this@DrawerActivity, SignInActivity::class.java))
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    fun displayScreen(id: Int) {
        val fragment: Fragment = when (id) {
            R.id.nav_home -> {
                HomeFragment()
            }
            R.id.nav_profile -> {
                ProfileFragment()
            }
            R.id.nav_dashboard -> {
                DashboardFragment()
            }
            R.id.nav_wallet -> {
                WalletFragment()
            }
            R.id.nav_support -> {
                SupportFragment()
            }

            else -> {
                HomeFragment()
            }
        }
        supportFragmentManager.beginTransaction().replace(R.id.relativeLayout, fragment as Fragment).commit();
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        displayScreen(item.itemId)
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val fragment = supportFragmentManager.findFragmentById(R.id.relativeLayout)
        fragment!!.onActivityResult(requestCode, resultCode, data)
    }


    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val fragmentCurrent = supportFragmentManager.findFragmentById(R.id.relativeLayout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            if (fragmentCurrent !is HomeFragment) {
                supportFragmentManager.beginTransaction().replace(R.id.relativeLayout, HomeFragment()).addToBackStack(null).commit();
            } else {
                super.onBackPressed()
            }

        }
    }

}