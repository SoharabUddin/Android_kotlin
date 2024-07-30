package com.myapps.wtsacademy2.Activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import com.myapps.wtsacademy2.ApiWorks.ApiService
import com.myapps.wtsacademy2.ApiWorks.ServiceBuilder
import com.myapps.wtsacademy2.ModelClasses.SignUpResponse
import com.myapps.wtsacademy2.Utils.FileUploadHelper
import com.myapps.wtsacademy2.Utils.FileUploadHelper.createMultipartFile
import com.myapps.wtsacademy2.R
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class SignUpActivity : BaseActivity() {
    private lateinit var firstName: TextInputEditText
    private lateinit var lastName: TextInputEditText
    private lateinit var email: TextInputEditText
    private lateinit var password: TextInputEditText
    private lateinit var uploadPictureTV: MaterialTextView
    private lateinit var imageView: ImageView
    private lateinit var signUpButton: MaterialButton
    private lateinit var progressBar: ProgressBar
    private lateinit var signInTV: TextView
    private lateinit var file: File
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_sign_up, findViewById(R.id.content_frame))
        initializeViews()
        uploadPictureTV.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        signUpButton.setOnClickListener { signUp()}
        signInTV.setOnClickListener { goTo(SignInActivity()) }
    }

    private fun initializeViews() {
        firstName = findViewById(R.id.first_name)
        lastName = findViewById(R.id.last_name)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        uploadPictureTV = findViewById(R.id.uploadPictureTV)
        imageView = findViewById(R.id.imageView)
        signUpButton = findViewById(R.id.sign_up_button)
        progressBar = findViewById(R.id.progressBar)
        signInTV = findViewById(R.id.signinTV)
    }
    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            showToast("Image selected: $uri")
            file = FileUploadHelper.copyUriToInternalPath(this,uri) ?: return@registerForActivityResult
            Log.d("File Extension", "${file.extension}")
            displaySelectedFile(file)
        } else {
            showToast("Image not selected")
        }
    }
    private fun signUp() {
        val firstNameText = firstName.text.toString().trim()
        val lastNameText = lastName.text.toString().trim()
        val emailText = email.text.toString().trim()
        val passwordText = password.text.toString().trim()

        if (firstNameText.isEmpty() || lastNameText.isEmpty() || emailText.isEmpty() || passwordText.isEmpty() || !::file.isInitialized) {
            showToast("All fields are required and a profile picture must be selected.")
            return
        }
        progressBar.visibility=View.VISIBLE

        val first_name = FileUploadHelper.createPartFromString(firstNameText)
        val last_name = FileUploadHelper.createPartFromString(lastNameText)
        val email = FileUploadHelper.createPartFromString(emailText)
        val password = FileUploadHelper.createPartFromString(passwordText)
        val profile_pic = file.createMultipartFile("image")

        val retrofit = ServiceBuilder.createService(ApiService::class.java)
        retrofit.signup(first_name,last_name,email,password,profile_pic).enqueue(object: Callback<SignUpResponse> {
            override fun onResponse(p0: Call<SignUpResponse>, p1: Response<SignUpResponse>) {
                if (p1.isSuccessful){
                    val body = p1.body()
                    if (body!=null){
                        showToast("Registration Successful")
                        clearEditTexts()
                        progressBar.visibility=View.GONE
                        val intent = Intent(this@SignUpActivity,SignInActivity::class.java)
                        intent.putExtra("email",emailText)
                        intent.putExtra("password",passwordText)
                        startActivity(intent)
                    }
                    else{
                        showToast("Data is null")
                        progressBar.visibility=View.GONE
                    }
                }
                else{
                    showToast("Not success: ${p1.errorBody()?.string()}")
                    progressBar.visibility=View.GONE
                }
            }

            override fun onFailure(p0: Call<SignUpResponse>, p1: Throwable) {
                showToast("Failed: ${p1.message}")
                progressBar.visibility=View.GONE
            }
        })
    }
    private fun displaySelectedFile(file: File) {
        imageView.visibility = View.VISIBLE
        Picasso.get().load(file).into(imageView)
    }
    private fun showToast(msg:String){
        Toast.makeText(this,msg, Toast.LENGTH_SHORT).show()
    }
    private fun clearEditTexts(){
        firstName.text?.clear()
        lastName.text?.clear()
        email.text?.clear()
        password.text?.clear()
        imageView.visibility = View.GONE

    }
    private fun  goTo(activity: Activity){
        startActivity(Intent(this,activity::class.java))
    }
}