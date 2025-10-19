package com.example.customerapp.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.customerapp.Adapter.CartAdapter
import com.example.customerapp.PayOutActivity
import com.example.customerapp.R
import com.example.customerapp.databinding.FragmentCartBinding
import com.example.customerapp.model.CartItems
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var productNames: MutableList<String>
    private lateinit var productPrices: MutableList<String>
    private lateinit var productDis: MutableList<String>
    private lateinit var productImages: MutableList<String>
    private lateinit var discounts: MutableList<String>
    private lateinit var productsLefts: MutableList<String>
    private lateinit var deliveryCharges: MutableList<String>
    private lateinit var productHighlights: MutableList<String>
    private lateinit var quantity: MutableList<Int>
    private lateinit var cartAdapter: CartAdapter
    private lateinit var userId:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater,container,false)

        auth= FirebaseAuth.getInstance()
        retrieveCartItems()


        binding.proccedButton.setOnClickListener {
            //get order item details before proceeding to check out
            getOrderItemDetails()

        }
        return binding.root
    }
    private fun CartFragment.getOrderItemDetails() {
        val orderIdReference:DatabaseReference = database.reference.child("user").child(userId).child("CartItems")
        val productName=mutableListOf<String>()
        val productPrice=mutableListOf<String>()
        val productImage=mutableListOf<String>()
        val productDis=mutableListOf<String>()
        val productsLefts=mutableListOf<String>()
        val discounts=mutableListOf<String>()
        val deliveryCharges=mutableListOf<String>()
        val productHighlight=mutableListOf<String>()
        //getItems quantity
        val productQuantity = cartAdapter.getUpdatedItemsQuantities()
        orderIdReference.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
            for(foodSnapshot in snapshot.children){
                //get the cart item to the respective list
                val orderItems = foodSnapshot.getValue(CartItems::class.java)
               //add item details in to List
                orderItems?.productName?.let{productName.add(it)}
                orderItems?.productPrice?.let{productPrice.add(it)}
                orderItems?.productImage?.let{productImage.add(it)}
                orderItems?.productDis?.let{productDis.add(it)}
                orderItems?.discount?.let{discounts.add(it)}
                orderItems?.productsLeft?.let{productsLefts.add(it)}
                orderItems?.productHighlights?.let{productHighlight.add(it)}
                orderItems?.deliveryCharges?.let{deliveryCharges.add(it)}
                /*orderItems?.productQuantity?.let{productQuantity.add(it)}
                 */
            }
                orderNow(productName,productPrice,productImage,productDis,discounts,productsLefts,productHighlight,deliveryCharges,productQuantity)
            }

            private fun orderNow(
                productName: MutableList<String>,
                productPrice: MutableList<String>,
                productImage: MutableList<String>,
                productDis: MutableList<String>,
                discounts: MutableList<String>,
                productsLefts: MutableList<String>,
                productHighlight: MutableList<String>,
                deliveryCharges: MutableList<String>,
                productQuantity: MutableList<Int>
            ) {
                if (isAdded && context!=null){
                    val intent=Intent(requireContext(),PayOutActivity::class.java)
                    intent.putExtra("ProductName",productName as ArrayList<String>)
                    intent.putExtra("ProductPrice",productPrice as ArrayList<String>)
                    intent.putExtra("ProductImage",productImage as ArrayList<String>)
                    intent.putExtra("ProductDis",productDis as ArrayList<String>)
                    intent.putExtra("Discount",discounts as ArrayList<String>)
                    intent.putExtra("ProductsLeft",productsLefts as ArrayList<String>)
                    intent.putExtra("ProductHighlight",productHighlight as ArrayList<String>)
                    intent.putExtra("deliveryCharges",deliveryCharges as ArrayList<String>)
                    intent.putExtra("productQuantity",productQuantity as ArrayList<Int>)
                    startActivity(intent)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(),"Order make Failed.Please try again",Toast.LENGTH_SHORT).show()
            }

        })
    }
    private fun retrieveCartItems() {
        //database reference to the Firebase
        database = FirebaseDatabase.getInstance()
        userId = auth.currentUser?.uid?:""
        val foodReference: DatabaseReference=database.reference.child("user").child(userId).child("CartItems")

        //List to store cartItems
        productNames = mutableListOf()
        productPrices = mutableListOf()
        productDis = mutableListOf()
        productsLefts= mutableListOf()
        productImages = mutableListOf()
        productHighlights = mutableListOf()
        deliveryCharges = mutableListOf()
        discounts = mutableListOf()
        quantity = mutableListOf()

        //Fetch data from the database
        foodReference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                // Clear all lists before adding new data
                productNames.clear()
                productPrices.clear()
                productDis.clear()
                productsLefts.clear()
                productImages.clear()
                productHighlights.clear()
                deliveryCharges.clear()
                discounts.clear()
                quantity.clear()
                for(foodSnapshot in snapshot.children){

                    //Get the cart Items object from the child node
                    val cartItems = foodSnapshot.getValue(CartItems::class.java)
                    //add cartItem details to the list
                    cartItems?.productName?.let{productNames.add(it)}
                    cartItems?.productPrice?.let{productPrices.add(it)}
                    cartItems?.productDis?.let{productDis.add(it)}
                    cartItems?.productsLeft?.let{productsLefts.add(it)}
                    cartItems?.productHighlights?.let{productHighlights.add(it)}
                    cartItems?.productImage?.let{productImages.add(it)}
                    cartItems?.discount?.let{discounts.add(it)}
                    cartItems?.deliveryCharges?.let{deliveryCharges.add(it)}
                    cartItems?.productQuantity?.let{quantity.add(it)}

                }
                setAdapter()
            }

            private fun setAdapter() {
               cartAdapter= CartAdapter(requireContext(),
                    productNames,
                    productPrices,
                    productImages,
                               productDis,
                               discounts,
                               productHighlights,
                                productsLefts,
                                deliveryCharges,
                    quantity
                    )
                binding.cartRecyclerView.layoutManager = LinearLayoutManager(requireContext(),
                    LinearLayoutManager.VERTICAL,false)
                binding.cartRecyclerView.adapter = cartAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,"data not fetch",Toast.LENGTH_SHORT).show()
            }

        })
    }
    companion object {
    }
}





