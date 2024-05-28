package com.example.platzi.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.platzi.databinding.ItemProductBinding
import com.example.platzi.models.get.ProductResponse

class ProductAdapter : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private val productList = ArrayList<ProductResponse>()

    private var itemClickListener: ((Int) -> Unit)? = null

    inner class ProductViewHolder(val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun getItemCount(): Int = productList.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val item = productList[position]
        holder.binding.item = item

        holder.itemView.setOnClickListener {
            itemClickListener?.invoke(item.id!!)
        }
    }

    fun updateList(newList: ArrayList<ProductResponse>) {
        productList.clear()
        productList.addAll(newList)
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        itemClickListener = listener
    }
}
