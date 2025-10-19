package com.example.customerapp.Adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import  android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import com.bumptech.glide.Glide
import com.example.customerapp.databinding.RecentBuyItemBinding




class RecentBuyAdapter(private var  context: Context,
                      private var productNameList: ArrayList<String>,
                       private var productPriceList: ArrayList<String>,
                       private var productImageList: ArrayList<String>,
                       private var productQuantityList: ArrayList<Int>
): RecyclerView.Adapter<RecentBuyAdapter.RecentViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentViewHolder {
        val binding = RecentBuyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecentViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = productNameList.size

   inner class RecentViewHolder(private  val binding: RecentBuyItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int) {
                binding.apply {
                    productName.text = productNameList[position]
                    productPrice.text = productPriceList[position]
                    productQuantity.text = productQuantityList[position].toString()
                    val uriString = productImageList[position]
                    val uri = Uri.parse(uriString)
                    Glide.with(context).load(uri).into(productImage)
                }
        }
    }

}