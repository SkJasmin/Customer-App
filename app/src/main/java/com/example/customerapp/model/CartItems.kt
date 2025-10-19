package com.example.customerapp.model

data class CartItems(
    val productName:String? = null,
    val productPrice:String? = null,
    val productDis:String? = null,
    val productImage:String? = null,
    val discount:String? = null,
    val productsLeft:String? = null,
    val deliveryCharges:String? = null,
    val productHighlights:String? = null,
    val productQuantity:Int? = null
)
