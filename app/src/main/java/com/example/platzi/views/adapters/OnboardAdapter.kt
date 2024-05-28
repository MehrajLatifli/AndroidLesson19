package com.example.platzi.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.platzi.databinding.ItemOnboardsBinding
import com.example.platzi.models.onboarding.Onboard

class OnboardAdapter : RecyclerView.Adapter<OnboardAdapter.OnboardViewHolder>() {

    private var itemList = arrayListOf<Onboard>()

    inner class OnboardViewHolder(val itemBinding: ItemOnboardsBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardViewHolder {
        val view = ItemOnboardsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OnboardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: OnboardViewHolder, position: Int) {
        val item = itemList[position]


        holder.itemBinding.item = item


    }

    fun updateList(newList: List<Onboard>) {
        itemList.clear()
        itemList.addAll(newList)
        notifyDataSetChanged()
    }
}