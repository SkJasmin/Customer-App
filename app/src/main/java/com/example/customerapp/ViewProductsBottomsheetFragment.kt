package com.example.customerapp


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.customerapp.Adapter.ViewProductsAdapter
import com.example.customerapp.databinding.FragmentViewProductsBottomsheetBinding
import com.example.customerapp.model.MenuItem
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ViewProductsBottomsheetFragment : BottomSheetDialogFragment(){
    private lateinit var binding:FragmentViewProductsBottomsheetBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var menuItems: MutableList<MenuItem>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentViewProductsBottomsheetBinding.inflate(inflater,container,false)
        binding.buttonBack.setOnClickListener{
            dismiss()
        }
       retrieveMenuItems()


        return binding.root
    }

    private fun retrieveMenuItems() {
        database = FirebaseDatabase.getInstance()
        val foodRef: DatabaseReference = database.reference.child("menu")
        menuItems = mutableListOf()

        foodRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot){
                for(foodSnapshot in snapshot.children){
                    val menuItem = foodSnapshot.getValue(MenuItem::class.java)
                    menuItem?.let{menuItems.add(it)}
                }
                Log.d("ITEMS","on DataChange:Data Received")
                //Once data recived ,set to adapter
                setAdapter()
            }



            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
    private fun setAdapter() {
        if (menuItems.isNotEmpty()) {
            val adapter = ViewProductsAdapter(menuItems, requireContext())
            binding.ViewProductsRecyclerView.layoutManager =
                LinearLayoutManager(requireContext())
            binding.ViewProductsRecyclerView.adapter = adapter
            Log.d("Items","setAdapter:data set")
        }
        else{
            Log.d("Items","setAdapter:data not set")
        }
    }

    companion object {

    }
}