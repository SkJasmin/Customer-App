package com.example.customerapp.Adapter


import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface.OnClickListener
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.persistableBundleOf
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.customerapp.DetailsActivity
import com.example.customerapp.databinding.ViewproductsLayoutBinding
import com.example.customerapp.model.MenuItem

class ViewProductsAdapter(
    private val menuItems:List<MenuItem>,
    private val requireContext: Context
) : RecyclerView.Adapter<ViewProductsAdapter.ProductsViewHolder>() {


    @SuppressLint("SuspiciousIndentation")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val binding = ViewproductsLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int =menuItems.size

    inner class ProductsViewHolder(private val binding: ViewproductsLayoutBinding)
        : RecyclerView.ViewHolder(binding.root) {
        init{
            binding.root.setOnClickListener{
                val Position = adapterPosition
                if(Position != RecyclerView.NO_POSITION){
                    openDetailsActivity(Position)
                }

            }
        }
        private fun ViewProductsAdapter.ProductsViewHolder.openDetailsActivity(position: Int)
        {
            val menuItem=menuItems[position]

            //a intent to open details activity and pass data
            val intent= Intent(requireContext, DetailsActivity::class.java).apply {
                putExtra("productName",menuItem.productName)
                putExtra("productImage",menuItem.productImage)
                putExtra("productPrice",menuItem.productPrice)
                putExtra("productDis",menuItem.productDis)
                putExtra("discount",menuItem.discount)
                putExtra("productsLeft",menuItem.productsLeft)
                putExtra("deliveryCharges",menuItem.deliveryCharges)
                putExtra("productHighLights",menuItem.productHighlights)
            }
            //start the details activity
            requireContext.startActivity(intent)

        }
        //set datd into recyclerview items name,price,image
        fun bind(position: Int) {
            val menuItem=menuItems[position]
            binding.apply {
                productName.text = menuItem.productName
                productPrice.text = menuItem.productPrice
                val uri = Uri.parse(menuItem.productImage)
                Glide.with(requireContext).load(uri).into(productImage)
            }
        }
    }
}





