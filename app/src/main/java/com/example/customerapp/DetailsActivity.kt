package com.example.customerapp

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.customerapp.databinding.ActivityDetailsBinding
import com.example.customerapp.model.CartItems
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding
    private lateinit var auth: FirebaseAuth
    private var productName: String? = null
    private var productImage: String? = null
    private var productPrice: String? = null
    private var productDis: String? = null
    private var discount: String? = null
    private var productsLeft: String? = null
    private var deliveryCharges: String? = null
    private var productHighlights: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //initalize firebase auth
        auth = FirebaseAuth.getInstance()


        productName = intent.getStringExtra("productName")
        productPrice = intent.getStringExtra("productPrice")
        productDis = intent.getStringExtra("productDis")
        productsLeft = intent.getStringExtra("productsLeft")
        productHighlights = intent.getStringExtra("productHighLights")
        discount = intent.getStringExtra("discount")
        deliveryCharges = intent.getStringExtra("deliveryCharges")

        productImage = intent.getStringExtra("productImage")

        with(binding) {
            detailProductName.text = productName
            detailProductPrice.text = productPrice
            delivery.text = deliveryCharges
            productLeft.text = productsLeft
            productsHighlights.text = productHighlights
            productDiscount.text = discount
            productsDis.text = productDis
            Glide.with(this@DetailsActivity).load(Uri.parse(productImage))
                .into(detailProductImage)

        }
        binding.buttonBack.setOnClickListener {
            finish()
        }
        binding.addItemButton.setOnClickListener {
            addItemToCart()
        }

    }

    private fun DetailsActivity.addItemToCart() {

        val database = FirebaseDatabase.getInstance().reference
        val userId = auth.currentUser?.uid ?: ""

        //create a cartItems object
        val cartItem = CartItems(
            productName.toString(),
            productPrice.toString(),
            productDis.toString(),
            productImage.toString(),
            discount.toString(),
            productsLeft.toString(),
            deliveryCharges.toString(),
            productHighlights.toString(),
            1
        )

        //save data to cart item to firebase database

        database.child("user").child(userId).child("CartItems").push().setValue(cartItem)
            .addOnSuccessListener {
                Toast.makeText(this, "Items added into the cart successfully", Toast.LENGTH_SHORT)
                    .show()
            }.addOnFailureListener {
            Toast.makeText(this, "Item Not Added", Toast.LENGTH_SHORT).show()
        }
    }
}
