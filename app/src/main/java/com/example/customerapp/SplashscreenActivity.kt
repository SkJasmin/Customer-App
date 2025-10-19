package com.example.customerapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SplashscreenActivity : AppCompatActivity() {
            private val handler = Handler(Looper.getMainLooper())
            override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_splashscreen)
                handler.postDelayed({
                    val intent = Intent(this,StartActivity::class.java)
                    startActivity(intent)
                    finish()
                },3000)
            }
        }
