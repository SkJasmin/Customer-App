package com.example.customerapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.customerapp.databinding.ActivityStartBinding

class StartActivity : AppCompatActivity() {
        private val binding: ActivityStartBinding by lazy {
            ActivityStartBinding.inflate(layoutInflater)
        }
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(binding.root)
            binding.next.setOnClickListener {
                val intent = Intent(this,LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }

