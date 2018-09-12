package com.company.redcode.royalcryptoexchange

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.company.redcode.royalcryptoexchange.auth.SignInActivity
import com.company.redcode.royalcryptoexchange.ui.DashboardFragment
import com.company.redcode.royalcryptoexchange.ui.HomeFragment
import com.company.redcode.royalcryptoexchange.ui.ProfileFragment
import kotlinx.android.synthetic.main.activity_drawer.*
import kotlinx.android.synthetic.main.app_bar_drawer.*

class DrawerActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

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
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_logout -> {
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
}