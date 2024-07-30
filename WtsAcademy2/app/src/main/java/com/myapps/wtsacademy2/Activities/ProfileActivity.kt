package com.myapps.wtsacademy2.Activities

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.ScrollView
import com.google.android.material.textview.MaterialTextView
import com.google.gson.Gson
import com.myapps.wtsacademy2.ModelClasses.SignInMD
import com.myapps.wtsacademy2.R
import com.squareup.picasso.Picasso

class ProfileActivity : BaseActivity() {
    private lateinit var nameTV : MaterialTextView
    private lateinit var emailTV : MaterialTextView
    private lateinit var roleTV : MaterialTextView
    private lateinit var createdAt : MaterialTextView
    private lateinit var updatedAt : MaterialTextView
    private lateinit var imageView: ImageView
    private lateinit var signInMD :SignInMD
    private lateinit var scrollView : ScrollView
    private lateinit var progressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_profile, findViewById(R.id.content_frame))
        initializeViews()
        fillData()
    }
        private fun initializeViews() {
            nameTV = findViewById(R.id.nameTV)
            emailTV = findViewById(R.id.emailTV)
            roleTV = findViewById(R.id.roleTV)
            createdAt = findViewById(R.id.createAtTV)
            updatedAt = findViewById(R.id.updateAtTV)
            imageView = findViewById(R.id.profileImage)
            scrollView = findViewById(R.id.scrollView2)
            progressBar = findViewById(R.id.progressBar)
        }
    private fun loadDataFromPreference(){
        val sharedPreferences = getSharedPreferences("signInResponse", MODE_PRIVATE)
        val json = sharedPreferences.getString("user_data", "")
        signInMD = Gson().fromJson(json,SignInMD::class.java)
    }
    private fun fillData(){
        loadDataFromPreference()
        val data = signInMD.data
        if (data!=null){
            nameTV.text = "${data.first_name} ${signInMD.data?.last_name}"
            emailTV.text = data.email.toString()
            roleTV.text = data.role_data?.role.toString()
            createdAt.text = data.createdAt.toString()
            updatedAt.text = data.updatedAt.toString()
            if (data.profile_pic != null && data.profile_pic.isNotEmpty()){
                Picasso.get().load(data.profile_pic).into(imageView)
            }
            scrollView.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        }
    }
}