package com.example.customerapp.Fragment

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.customerapp.Adapter.BuyAgainAdapter
import com.example.customerapp.R
import com.example.customerapp.databinding.FragmentHistoryBinding
import com.example.customerapp.model.OrderDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import androidx.core.net.toUri
import com.example.customerapp.RecentOrderItems
import kotlin.math.log


class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var buyAgainAdapter: BuyAgainAdapter
    private  lateinit var database: FirebaseDatabase
    private lateinit var  auth: FirebaseAuth
    private lateinit var userId: String
    private var listOfOrderItem: MutableList<OrderDetails> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(layoutInflater,container,false)
        // Inflate the layout for this fragment

        //initalize firebase auth
        auth = FirebaseAuth.getInstance()
        //initalize firebase detabase
        database = FirebaseDatabase.getInstance()

        //Retrive and display the user Order History
        retrieveByHistory()

        //recent buy button click
        binding.recentBuyItem.setOnClickListener {
            seeItemsRecentBuy()
        }
        binding.receivedButton.setOnClickListener {
            updateOrderStatus()
        }
        return binding.root
    }
    private fun updateOrderStatus() {
    val itemPushKey = listOfOrderItem[0].itemPushKey
        val completeOrderReference = database.reference.child("CompletedOrder").child(itemPushKey!!)
        completeOrderReference.child("paymentReceived").setValue(true)
    }

    //function to see  items recent buy
    private fun seeItemsRecentBuy() {
        listOfOrderItem.firstOrNull()?.let{recentBuy ->

        val intent  = Intent(requireContext(), RecentOrderItems::class.java)
            intent.putExtra("RecentBuyOrderItem", ArrayList(listOfOrderItem)) // ✅ FIXED
          startActivity(intent)
}
    }
    //function to retrive items buy history
    private fun retrieveByHistory() {
        binding.recentBuyItem.visibility = View.INVISIBLE
        userId = auth.currentUser?.uid ?: ""

        val buyItemReference: DatabaseReference =
            database.reference.child("user").child(userId).child("ByHistory")
        val shortingQuery = buyItemReference.orderByChild("currentTime")
        shortingQuery.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                for (buySnapshort in snapshot.children) {
                    val buyHistoryItem = buySnapshort.getValue(OrderDetails::class.java)

                    buyHistoryItem?.let {
                        listOfOrderItem.add(it)
                    }
                }
                listOfOrderItem.reverse()
                if (listOfOrderItem.isNotEmpty()) {
                    //display the most recent order details
                    setDataInRecentBuyItem()
                    //set up the recyclerview with previous order details
                    setPreviousBuyItemsRecyclerView()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("HistoryFragment", "Database error: ${error.message}")
            }

        })
    }
    //display the most recent order details
            private fun setDataInRecentBuyItem() {
                binding.recentBuyItem.visibility = View.VISIBLE
                val recentOrderItem = listOfOrderItem.firstOrNull()
                recentOrderItem?.let{
                    with(binding){
                        productName.text = it.productNames?.firstOrNull()?:""
                        productPrice.text = it.productPrices?.firstOrNull()?:""
                        val image = it.productImages?.firstOrNull()?:""
                        val uri = image.toUri()
                        //val uri = Uri.parse(image)

                        Glide.with(requireContext()).load(uri).into(productImage)

                         val isOrderIsAccepted = listOfOrderItem[0].orderAccepted
                        Log.d("TAG","setDataIn RecentBuyItem:$isOrderIsAccepted")
                        if(isOrderIsAccepted){
                            orderStatus.background.setTint(Color.GREEN)
                            receivedButton.visibility = View.VISIBLE
                        }
                    }
                }
            }
    //set up the recyclerview with previous order details
    private fun setPreviousBuyItemsRecyclerView() {
                val buyAgainProductName = mutableListOf<String>()
                val buyAgainProductPrice = mutableListOf<String>()
                val buyAgainProductImage = mutableListOf<String>()
                for (i in 1 until listOfOrderItem.size) {
                    listOfOrderItem[i].productNames?.firstOrNull()?.let {
                        buyAgainProductName.add(it)
                        listOfOrderItem[i].productPrices?.firstOrNull()?.let {
                            buyAgainProductPrice.add(it)
                            listOfOrderItem[i].productImages?.firstOrNull()?.let {
                                buyAgainProductImage.add(it)
                            }
                        }
                        val rv = binding.buyAgainRecyclerView
                        rv.layoutManager = LinearLayoutManager(requireContext())
                        buyAgainAdapter = BuyAgainAdapter(buyAgainProductName,buyAgainProductPrice,buyAgainProductImage,requireContext())
                        rv.adapter= buyAgainAdapter
                    }
                }

            }
}



