package com.example.customerapp.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.customerapp.DetailsActivity
import com.example.customerapp.databinding.PopularItemBinding


class PopularAdapter(private val items:List<String>,private val price:List<String>,private  val image:List<Int>,private val requireContext: Context): RecyclerView.Adapter<PopularAdapter.PopulerViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopulerViewHolder {
        return PopulerViewHolder(PopularItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return  items.size
    }

    override fun onBindViewHolder(holder: PopulerViewHolder, position: Int) {
        val item = items[position]
        val images = image[position]
        val prices = price[position]
        holder.bind(item,prices,images)

        holder.itemView.setOnClickListener {
            //SetOnClick listener to open details
            val intent = Intent(requireContext, DetailsActivity::class.java)
            intent.putExtra("ProductName",item)
            intent.putExtra("ProductImage",images)
            intent.putExtra("ProductPrice",prices)
            requireContext.startActivity(intent)
        }

    }
    class PopulerViewHolder(private  val binding: PopularItemBinding):RecyclerView.ViewHolder(binding.root){
        private val imagesView = binding.productImage
        fun bind(items: String, prices:String, images: Int) {
            binding.productName.text = items
            binding.productImage.drawable
            binding.productPrice.text = prices
            imagesView.setImageResource(images)
        }

    }
}