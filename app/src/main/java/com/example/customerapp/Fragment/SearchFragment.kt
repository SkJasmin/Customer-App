package com.example.customerapp.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.customerapp.Adapter.ViewProductsAdapter
import com.example.customerapp.databinding.FragmentSearchBinding
import com.example.customerapp.model.MenuItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: ViewProductsAdapter
    private lateinit var database: FirebaseDatabase
    private var originalMenuItems = mutableListOf<MenuItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        //retrive menu Items from database
        retrieveMenuItem()

        //setup for search view
        setupSearchView()

        return binding.root
    }

    private fun retrieveMenuItem() {
        //get database reference
        database = FirebaseDatabase.getInstance()
        //reference to the menu node
        val foodReference: DatabaseReference = database.reference.child("menu")
        foodReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshort in snapshot.children) {
                    val menuItem = foodSnapshort.getValue(MenuItem::class.java)
                    menuItem?.let {
                        originalMenuItems.add(it)
                    }
                }
                showAllMenu()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun showAllMenu() {
        val filteredMenuItem = ArrayList(originalMenuItems)
        setAdapter(filteredMenuItem)

    }

    private fun setAdapter(filteredMenuItem: ArrayList<MenuItem>) {
        adapter = ViewProductsAdapter(filteredMenuItem, requireContext())
        binding.ViewProductsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.ViewProductsRecyclerView.adapter = adapter
    }


    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                filterProductItems(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filterProductItems(newText)
                return true
            }
        })
    }

    private fun filterProductItems(query: String) {
        val filteredMenuItems = originalMenuItems.filter {
            it.productName?.contains(query, ignoreCase = true) == true
        }
        //setAdapter(filteredMenuItems)
        setAdapter(filteredMenuItems as ArrayList<MenuItem>)
    }
            companion object {

            }
        }
