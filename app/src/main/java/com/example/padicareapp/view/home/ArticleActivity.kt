package com.example.padicareapp.view.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.padicareapp.R

class ArticleActivity : AppCompatActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)

        val webView: WebView = findViewById(R.id.webview)
        val url = intent.getStringExtra(EXTRA_URL)
        webView.webViewClient = WebViewClient()
        webView.settings.apply {
            javaScriptEnabled = true
            cacheMode = WebSettings.LOAD_DEFAULT
        }
        if (!url.isNullOrEmpty()) {
            webView.loadUrl(url)
        }
    }
    companion object {
        const val EXTRA_URL = "extra_url"
    }
}