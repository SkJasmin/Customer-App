package com.example.customerapp

import android.R
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.customerapp.databinding.ActivityChooseLocationBinding

class ChooseLocationActivity : AppCompatActivity() {
        private val binding: ActivityChooseLocationBinding
                by lazy{
                    ActivityChooseLocationBinding.inflate(layoutInflater)
                }
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(binding.root)
            val locationList: Array<String> = arrayOf("Jaipur","Odisa","Bundi","Sikar")
            val adapter = ArrayAdapter(this, R.layout.simple_list_item_1,locationList)
            val autoCompleteTextView = binding.listofLocation
            autoCompleteTextView.setAdapter(adapter)
        }
    }