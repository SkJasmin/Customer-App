package com.example.customerapp.Adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.customerapp.databinding.CartitemLayoutBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.ref.Reference
import kotlin.apply



class CartAdapter(
    private val context: Context,
    private val cartItems:MutableList<String>,
    private val cartItemPrices:MutableList<String>,
    private var cartImages:MutableList<String>,
    private val productDis: MutableList<String>,
    private val discounts:MutableList<String>,
    private val productHighlights:MutableList<String>,
    private val productsLefts:MutableList<String>,
    private val deliveryCharges:MutableList<String>,
    private val productQuantity:MutableList<Int>



    ):RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
        //Instance firebase
     private val auth = FirebaseAuth.getInstance()

    init{
        //Initalize Firebase
        val database = FirebaseDatabase.getInstance()
        val userId=auth.currentUser?.uid?:""
        val cartItemNumber=cartItems.size

        itemQuantities=IntArray(cartItemNumber){1}
        cartItemsReference = database.reference.child("user").child(userId).child("CartItems")


    }
    companion object{
        private var itemQuantities:IntArray = intArrayOf()
        private lateinit var cartItemsReference: DatabaseReference
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = CartitemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CartViewHolder(binding)
    }
    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(position)
    }
    override fun getItemCount(): Int  = cartItems.size

   /* //get updated quatity
    fun getUpdatedItemsQuantities():MutableList<Int> {
       val itemQuantity=mutableListOf<Int>()
        itemQuantity.addAll(productQuantity)
        return itemQuantity

    }*/
   fun getUpdatedItemsQuantities(): MutableList<Int> {
       return itemQuantities.toMutableList()
   }

    inner class CartViewHolder(private val binding:CartitemLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int) {
            binding.apply {
                val quantity = itemQuantities[position]
                productName.text = cartItems[position]
                productPrice.text = cartItemPrices[position]
               //load image using glide
                val uriString=cartImages[position]
                val uri= Uri.parse(uriString)
                Glide.with(context).load(uri).into(binding.productImageView)
                binding.quantity.text = productQuantity[position].toString()
                // Handle button clicks
                minusButton.setOnClickListener {
                    decreaseQuantity(position)
                }
                plusButton.setOnClickListener {
                    increaseQuantity(position)
                }
                deleteButton.setOnClickListener {
                    val itemPosition = adapterPosition
                    if (itemPosition != RecyclerView.NO_POSITION) {
                        deleteItem(position)
                    }
                }
            }
        }

        private fun decreaseQuantity(position: Int) {
            if (itemQuantities[position] > 1) {
                itemQuantities[position]--
                binding.quantity.text = itemQuantities[position].toString()
            }
        }

        private fun increaseQuantity(position: Int) {
            if (itemQuantities[position] < 10) { // Set maximum quantity to 10
                itemQuantities[position]++
                binding.quantity.text = itemQuantities[position].toString()
            }
        }

        private fun deleteItem(position: Int) {
           val positionRetrieve=position
            getUniqueKeyAtPosition(positionRetrieve){uniqueKey->
                if(uniqueKey != null){
                    removeItem(position,uniqueKey)
                }
            }
        }
        private fun removeItem(position: Int, uniqueKey: String) {
            if(uniqueKey !=null){
                cartItemsReference.child(uniqueKey).removeValue().addOnSuccessListener {
                    cartItems.removeAt(position)
                    cartImages.removeAt(position)
                    cartItemPrices.removeAt(position)
                    productQuantity.removeAt(position)
                    productDis.removeAt(position)
                    discounts.removeAt(position)
                    productsLefts.removeAt(position)
                    productHighlights.removeAt(position)
                    deliveryCharges.removeAt(position)
                    Toast.makeText(context,"Item deleted successfully", Toast.LENGTH_SHORT).show()
                    //update item quantity
                    itemQuantities = itemQuantities.filterIndexed{ index, t ->index != position}.toIntArray()
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position,cartItems.size)
                }.addOnFailureListener {
                    Toast.makeText(context,"Failed to delete", Toast.LENGTH_SHORT).show()
                }
            }
        }

        private fun getUniqueKeyAtPosition(positionRetrieve: Int,onComplete:(String?)->Unit) {
            cartItemsReference.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    var uniqueKey:String?=null
                    //loop for snapshot children
                    snapshot.children.forEachIndexed { index,dataSnapshot ->
                        if(index ==positionRetrieve){
                            uniqueKey = dataSnapshot.key
                            return@forEachIndexed
                        }
                    }
                    onComplete(uniqueKey)
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }
    }
}


