package com.myapps.wtsacademy2.Activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.myapps.wtsacademy2.ApiWorks.ApiService
import com.myapps.wtsacademy2.ApiWorks.ServiceBuilder
import com.myapps.wtsacademy2.ModelClasses.SignInMD
import com.myapps.wtsacademy2.ModelClasses.SignInRequest
import com.myapps.wtsacademy2.R
import com.myapps.wtsacademy2.Utils.Constant
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInActivity : BaseActivity() {
    private lateinit var email: TextInputEditText
    private lateinit var password: TextInputEditText
    private lateinit var signUpButton: MaterialButton
    private lateinit var progressBar: ProgressBar
    private lateinit var emailText :String
    private lateinit var passwordText :String
    private lateinit var signInRequest: SignInRequest
    private lateinit var signupTV : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_sign_in, findViewById(R.id.content_frame))
        initializeViews()
        getDataFromIntent()
        email.setText(emailText)
        password.setText(passwordText)
        signUpButton.setOnClickListener { callApi() }
        signupTV.setOnClickListener {
            Constant.goTo(this,SignUpActivity::class.java)
            finish()
        }
    }
    private fun getDataFromIntent(){
        emailText = intent.getStringExtra("email")?:""
        passwordText = intent.getStringExtra("password")?:""
    }
    private fun initializeViews() {
        email = findViewById(R.id.emailSin)
        password = findViewById(R.id.passwordSin)
        signUpButton = findViewById(R.id.sign_in_button)
        progressBar = findViewById(R.id.progressBarSignin)
        signupTV = findViewById(R.id.signupTV)
    }
    private fun callApi(){
        progressBar.visibility = View.VISIBLE
        val retrofit = ServiceBuilder.createService(ApiService::class.java)
        signInRequest = SignInRequest(email.text.toString(),password.text.toString())
        retrofit.signin(signInRequest).enqueue(object: Callback<SignInMD>{
            override fun onResponse(p0: Call<SignInMD>, p1: Response<SignInMD>) {
                if (p1.isSuccessful){
                    val body = p1.body()
                    if (body!=null){
                        showToast("Successfully Signed In")
                        saveToSharedPreference(body)
                        val intent = Intent(this@SignInActivity,ProductListActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else{
                        showToast("Body is null")
                    }
                }
                else{
                    showToast("Not Success: ${p1.errorBody()?.string()}")
                }
            }
            override fun onFailure(p0: Call<SignInMD>, p1: Throwable) {
                showToast("Failed: ${p1.message}")
            }
        })
    }
    private fun showToast(msg:String){
        Toast.makeText(this,msg, Toast.LENGTH_SHORT).show()
    }
    private fun saveToSharedPreference(signInResponse: SignInMD){
        val json = Gson().toJson(signInResponse)
        val sharedPreferences =getSharedPreferences("signInResponse", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("user_data", json)
        editor.putBoolean("isLoggedIn",true)
        editor.apply()
    }
}