package com.example.customerapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.customerapp.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
        private lateinit var binding: ActivityMainBinding
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)
            var NavController=findNavController(R.id.fragmentContainerView)
            var bottomnav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
            bottomnav.setupWithNavController(NavController)
            binding.notificationButton.setOnClickListener {
                val bottomSheetDialog = Notification_Bottom_Fragment()
                bottomSheetDialog.show(supportFragmentManager,"Test")
            }

        }
    }