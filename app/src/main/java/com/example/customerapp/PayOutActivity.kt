package com.example.customerapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.customerapp.databinding.ActivityPayOutBinding
import com.example.customerapp.model.OrderDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.ref.WeakReference

class PayOutActivity : AppCompatActivity() {
        private lateinit var binding:ActivityPayOutBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var name:String
    private lateinit var address:String
    private lateinit var phone:String
    private lateinit var totalAmount: String
    private lateinit var productName: ArrayList<String>
    private lateinit var productPrice: ArrayList<String>
    private lateinit var productImage: ArrayList<String>
    private lateinit var productDis: ArrayList<String>
    private lateinit var discount: ArrayList<String>
    private lateinit var productsLeft: ArrayList<String>
    private lateinit var productHighlight: ArrayList<String>
    private lateinit var deliveryCharges: ArrayList<String>
    private lateinit var productQuantity: ArrayList<Int>
    private lateinit var databaseReference: DatabaseReference
    private lateinit var userId: String

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityPayOutBinding.inflate(layoutInflater)
            setContentView(binding.root)

            //Initalizing firebase and user details
            auth = FirebaseAuth.getInstance()
            databaseReference= FirebaseDatabase.getInstance().getReference()

            //set user data
            setUserData()
//get user datails from firebase
            val intent= intent
            productName = intent.getStringArrayListExtra("ProductName") as ArrayList<String>
            productPrice = intent.getStringArrayListExtra("ProductPrice") as ArrayList<String>
            productImage = intent.getStringArrayListExtra("ProductImage") as ArrayList<String>
            productDis = intent.getStringArrayListExtra("ProductDis") as ArrayList<String>
            discount = intent.getStringArrayListExtra("Discount") as ArrayList<String>
            productsLeft = intent.getStringArrayListExtra("ProductsLeft") as ArrayList<String>
            productHighlight = intent.getStringArrayListExtra("ProductHighlight") as ArrayList<String>
            deliveryCharges = intent.getStringArrayListExtra("deliveryCharges") as ArrayList<String>
           productQuantity = intent.getIntegerArrayListExtra("productQuantity") as ArrayList<Int>

            /*totalAmount = calculateTotalAmount().toString() + "₹"
            binding.totalAmount.isEnabled = false
            binding.totalAmount.setText(totalAmount)*/

            // Calculate total safely and show with ₹
            val total = calculateTotalAmount()
            totalAmount = "₹$total"
            binding.totalAmount.isEnabled = false
            binding.totalAmount.setText(totalAmount)

            binding.placeOrderButton.setOnClickListener {
                //get data from textView
                name = binding.name.text.toString().trim()
                address = binding.address.text.toString().trim()
                phone = binding.phone.text.toString().trim()
                if (name.isBlank() || address.isBlank() || phone.isBlank()){
                    Toast.makeText(this, "Please Enter all the Details", Toast.LENGTH_SHORT).show()
                }
                else{
                    placeOrder()
                }

                val bottomSheetDialog = CongratsBottomSheetFragment()
                bottomSheetDialog.show(supportFragmentManager,"Test")
            }
        }
    private fun placeOrder() {
       userId = auth.currentUser?.uid?:""
        val time=System.currentTimeMillis()
        val itemPushKey=databaseReference.child("OrderDetails").push().key

        val orderDetails= OrderDetails(userId,name,productName,productPrice,productImage,productDis,discount,
            productsLeft,deliveryCharges,productHighlight,productQuantity,address,totalAmount,phone,time,itemPushKey,false,false)
        val orderReference = databaseReference.child("OrderDetails").child(itemPushKey!!)
        orderReference.setValue(orderDetails).addOnSuccessListener {
            val bottomSheetDialog = CongratsBottomSheetFragment()
            bottomSheetDialog.show(supportFragmentManager,"Test")
            removeItemFromCart()
            addOrderToHistory(orderDetails)

        }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to order", Toast.LENGTH_SHORT).show()
            }
    }

    private fun removeItemFromCart() {
      val cartItemsReference = databaseReference.child("user")
          .child(userId)
          .child("CartItems")
        cartItemsReference.removeValue()
    }
    private fun addOrderToHistory(orderDetails: OrderDetails) {
        databaseReference.child("user").child(userId).child("ByHistory") // ✅ match HistoryFragment
            .child(orderDetails.itemPushKey!!)
            .setValue(orderDetails)
            .addOnSuccessListener {
                Toast.makeText(this, "Order saved to history", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to save order history: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }


    /*
    private fun calculateTotalAmount(): Int{
        var totalAmount = 0
        for(i in 0 until productPrice.size){
            var price = productPrice[i]
            val lastChar = price.last()
            val priceIntValue = if(lastChar == '₹'){
                price.dropLast(1).toInt()
            }
            else{
                price.toInt()
            }
            var quantity = productQuantity[i]
            totalAmount +=priceIntValue * quantity
        }
        return totalAmount
    }*/
    private fun calculateTotalAmount(): Int {
        var totalAmount = 0
        for (i in 0 until productPrice.size) {
            val cleanPrice = productPrice[i].replace("₹", "").trim()
            val priceInt = cleanPrice.toIntOrNull() ?: 0
            val quantity = productQuantity[i]
            totalAmount += priceInt * quantity
        }
        return totalAmount
    }
/*
    private fun calculateTotalAmount(): Int {
    var totalAmount = 0
    for (i in 0 until productPrice.size) {
        // Remove ₹ symbol and any spaces
        val cleanPrice = productPrice[i].replace("₹", "").trim()

        // Convert to number safely
        val priceInt = cleanPrice.toIntOrNull() ?: 0

        // Multiply by quantity
        val quantity = productQuantity[i]
        totalAmount += priceInt * quantity
    }

    return totalAmount
}
*/

    private fun setUserData() {
        val user = auth.currentUser
        if(user!=null) {
            val userId=user.uid
            val userReference=databaseReference.child("user").child(userId)

            userReference.addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        val names=snapshot.child("name").getValue(String::class.java)?:""
                        val addresss=snapshot.child("address").getValue(String::class.java)?:""
                        val phones=snapshot.child("phone").getValue(String::class.java)?:""
                        binding.apply {
                            name.setText(names)
                            address.setText(addresss)
                            phone.setText(phones)
                        }
                    }

                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        }
    }
}




