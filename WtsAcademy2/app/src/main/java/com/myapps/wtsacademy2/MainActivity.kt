package com.myapps.wtsacademy2

import android.os.Bundle
import com.google.gson.Gson
import com.myapps.wtsacademy2.Utils.Constant
import com.myapps.wtsacademy2.Activities.BaseActivity
import com.myapps.wtsacademy2.Activities.ProductListActivity
import com.myapps.wtsacademy2.Activities.SignInActivity
import com.myapps.wtsacademy2.ModelClasses.SignInMD

class MainActivity : BaseActivity() {
    private lateinit var signInMD: SignInMD

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkSignIn()
    }

    private fun checkSignIn() {
        if (isUserSignedIn()) {
            Constant.goTo(this, ProductListActivity::class.java)
            finish()
        } else {
            Constant.goTo(this, SignInActivity::class.java)
            finish()
        }
    }

    private fun isUserSignedIn(): Boolean {
        val sharedPreferences = getSharedPreferences("signInResponse", MODE_PRIVATE)
        val json = sharedPreferences.getString("user_data", null)

        return if (json != null) {
            signInMD = Gson().fromJson(json, SignInMD::class.java)
            sharedPreferences.getBoolean("isLoggedIn", false)
        } else {
            false
        }
    }
}
