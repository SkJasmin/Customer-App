package com.example.customerapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.customerapp.Adapter.RecentBuyAdapter
import com.example.customerapp.databinding.ActivityMainBinding
import com.example.customerapp.databinding.ActivityRecentOrderItemsBinding
import com.example.customerapp.model.OrderDetails

class RecentOrderItems : AppCompatActivity() {
    private val binding: ActivityRecentOrderItemsBinding by lazy{
        ActivityRecentOrderItemsBinding.inflate(layoutInflater)
    }
    private lateinit var allProductNames: ArrayList<String>
    private lateinit var allProductPrices: ArrayList<String>
    private lateinit var allProductImages: ArrayList<String>
    private lateinit var allProductQuantities: ArrayList<Int>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            finish()
        }

        val recentOrderItems = intent.getSerializableExtra("RecentBuyOrderItem") as? ArrayList<OrderDetails>

        recentOrderItems ?.let{orderDetails ->
             if(orderDetails.isNotEmpty()){
                 val recentOrderItem = orderDetails[0]


                 allProductNames =recentOrderItem.productNames as ArrayList<String>
                 allProductPrices =recentOrderItem.productPrices as ArrayList<String>
                 allProductImages =recentOrderItem.productImages as ArrayList<String>
                 allProductQuantities =recentOrderItem.productQuantity as ArrayList<Int>
             }
        }
  setAdapter()

    }

    private fun setAdapter() {
        val rv = binding.recyclerViewRecentBuy
        rv.layoutManager = LinearLayoutManager(this)
        val adapter = RecentBuyAdapter(this,allProductNames,allProductPrices,
            allProductImages,allProductQuantities)
        rv.adapter = adapter
    }
}