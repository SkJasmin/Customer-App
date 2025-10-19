package com.example.customerapp.Adapter

import  android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.customerapp.databinding.BuyAgainItemLayoutBinding



class BuyAgainAdapter(private val buyAgainProductName: MutableList<String>, private val buyAgainProductPrice: MutableList<String>,
                      private val buyAgainProductImage: MutableList<String>,
                      private  var requireContext: Context): RecyclerView.Adapter<BuyAgainAdapter.BuyAgainViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuyAgainViewHolder {
        val binding = BuyAgainItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return BuyAgainViewHolder(binding)
    }

    override fun getItemCount(): Int  = buyAgainProductName.size

    override fun onBindViewHolder(holder: BuyAgainViewHolder, position: Int) {
        holder.bind(buyAgainProductName[position],buyAgainProductPrice[position],buyAgainProductImage[position])
    }
    inner class BuyAgainViewHolder(private  val binding: BuyAgainItemLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(productName: String, productPrice: String, productImage: String) {
            binding.buyAgainProductName.text = productName
            binding.buyAgainProductPrice.text = productPrice
            val uriString = productImage
            val uri = Uri.parse(uriString)
            Glide.with(requireContext).load(uri).into(binding.buyAgainProductImage)
        }
    }
}