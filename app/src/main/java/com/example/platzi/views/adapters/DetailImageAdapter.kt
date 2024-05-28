package com.example.platzi.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.platzi.databinding.ItemDetailBinding

class DetailImageAdapter : RecyclerView.Adapter<DetailImageAdapter.ImageSliderViewHolder>() {

    private var itemList = listOf<String>()

    inner class ImageSliderViewHolder(val itemBinding: ItemDetailBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageSliderViewHolder {
        val view = ItemDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageSliderViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ImageSliderViewHolder, position: Int) {
        val image = itemList[position]
        holder.itemBinding.image = image
        holder.itemBinding.executePendingBindings()
    }

    fun updateList(newList: List<String>) {
        itemList = newList
        notifyDataSetChanged()
    }
}
