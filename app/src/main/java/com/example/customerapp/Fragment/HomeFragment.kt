package com.example.customerapp.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.customerapp.Adapter.PopularAdapter
import com.example.customerapp.Adapter.ViewProductsAdapter
import com.example.customerapp.R
import com.example.customerapp.ViewProductsBottomsheetFragment
import com.example.customerapp.databinding.FragmentHomeBinding
import com.example.customerapp.model.MenuItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var menuItems: MutableList<MenuItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        binding.viewProducts.setOnClickListener {
            val bottomSheetDialog = ViewProductsBottomsheetFragment()
            bottomSheetDialog.show(parentFragmentManager,"Test")
        }
        //retrive and display  Popular Menu items
        retraieveAndDisplayPopulerItems()
        return binding.root

    }

    private fun retraieveAndDisplayPopulerItems() {
        //get reference to the database
        database = FirebaseDatabase.getInstance()
        val foodRef: DatabaseReference = database.reference.child("menu")
        menuItems = mutableListOf()

        //retrive menu items from the database
        foodRef.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot){
                for(foodSnapshot in snapshot.children){
                    val menuItem = foodSnapshot.getValue(MenuItem::class.java)
                    menuItem?.let { menuItems.add(it) }
                }
                //display a rendom popular items
                randomPopularItems()
            }

            private fun randomPopularItems() {
                //create as shuffled list of items
                val index = menuItems.indices.toList().shuffled()
                val numItemToShow = 6
                val subsetMenuItems = index.take(numItemToShow).map{menuItems[it]}

                setPopularItemsAdapter(subsetMenuItems)
            }

            private fun setPopularItemsAdapter(subsetMenuItems: List<MenuItem>) {
                val adapter = ViewProductsAdapter(subsetMenuItems, requireContext())
                binding.popularRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                binding.popularRecyclerView.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.womenbanner, scaleType = ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.men, scaleType = ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.phonebanner, scaleType = ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.tvbanner, scaleType = ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.boybanner, scaleType = ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.girlsbanner, scaleType = ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.grocerybanner, scaleType = ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.beauty, scaleType = ScaleTypes.FIT))

        val imageSlider = binding.imageSlider
        imageSlider.setImageList(imageList)
        imageSlider.setImageList(imageList,ScaleTypes.FIT)
        imageSlider.setItemClickListener(object : ItemClickListener {
            override fun doubleClick(position: Int) {
                TODO("Not yet implemented")
            }
            override fun onItemSelected(position: Int){
                val itemPosition = imageList[position]
                val itemMessage = "Selected Image $position"
                Toast.makeText(requireContext(),itemMessage,Toast.LENGTH_SHORT).show()
            }
        })


    }
}