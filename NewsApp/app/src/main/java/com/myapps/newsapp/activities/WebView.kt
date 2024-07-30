package com.myapps.newsapp.activities

import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.myapps.newsapp.databinding.ActivityWebViewBinding

class WebView : AppCompatActivity() {
    lateinit var  binding: ActivityWebViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var url = intent.getStringExtra("url").toString()
        val webView: WebView = binding.webView
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(url)

    }
}