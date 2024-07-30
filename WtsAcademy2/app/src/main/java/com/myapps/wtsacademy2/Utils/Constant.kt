package com.myapps.wtsacademy2.Utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.myapps.wtsacademy2.ModelClasses.SignInMD
import com.myapps.wtsacademy2.ModelClasses.SignUpResponse

object Constant {
    lateinit var signInMD:SignInMD
    var token = ""
    var isSignIn = false
    lateinit var signUpResponse : SignUpResponse
    fun replaceFragment(fragment: Fragment,supportFragmentManager: FragmentManager,containerId: Int) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(containerId, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    fun viewVisible(view: View){
        view.visibility = View.VISIBLE
    }
    fun viewGone(view: View){
        view.visibility = View.GONE
    }
    fun showToast(context: Context,msg:String){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show()
    }
    fun goTo(context: Context, activityClass: Class<out Activity>) {
        context.startActivity(Intent(context, activityClass))
    }
}