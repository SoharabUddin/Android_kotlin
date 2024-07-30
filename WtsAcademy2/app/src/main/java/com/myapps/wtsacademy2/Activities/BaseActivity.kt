package com.myapps.wtsacademy2.Activities

import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.myapps.wtsacademy2.Utils.Constant
import com.myapps.wtsacademy2.R
import com.squareup.picasso.Picasso

open class BaseActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)

        setupNavigationMenu()
        updateNavigationMenuVisibility()

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer, R.string.close_drawer)
        drawerLayout.addDrawerListener(toggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle.syncState()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (toggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }
    private fun setupNavigationMenu() {
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_profile -> { Constant.goTo(this,ProfileActivity::class.java)}
                R.id.nav_product_list->{ Constant.goTo(this,ProductListActivity::class.java)}

                R.id.nav_sign_out->{
                    signOut()
                    Constant.goTo(this,SignInActivity::class.java)
                }
                R.id.nav_sign_in->{ Constant.goTo(this,SignInActivity::class.java)}
            }
            drawerLayout.closeDrawers()
            true
        }
    }
    protected fun showGroup(id:Int,show: Boolean) {
        val menu = navigationView.menu
        menu.setGroupVisible(id, show)
    }
    private fun signOut() {
        val sharedPreferences = getSharedPreferences("signInResponse", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", false)
        editor.remove("user_data")
        editor.apply()
        finish()
    }
    fun updateHeader(name: String, email: String, profileImageUrl: String) {
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        val headerView = navigationView.getHeaderView(0)
        val headerImageView = headerView.findViewById<ImageView>(R.id.headerImageView)
        val headerNameTextView = headerView.findViewById<TextView>(R.id.headerNameTextView)
        val headerEmailTextView = headerView.findViewById<TextView>(R.id.headerEmailTextView)

        headerNameTextView.text = name
        headerEmailTextView.text = email
        if (profileImageUrl.isNotEmpty()&&profileImageUrl!=null){
            Picasso.get().load(profileImageUrl).into(headerImageView)
        }
    }
    protected fun updateNavigationMenuVisibility() {
        val sharedPreferences = getSharedPreferences("signInResponse", MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        if (isLoggedIn) {
            showGroup(R.id.group_sign_in, false)
            showGroup(R.id.group_products_profile, true)
        } else {
            showGroup(R.id.group_sign_in, true)
            showGroup(R.id.group_products_profile, false)
        }
    }
}
