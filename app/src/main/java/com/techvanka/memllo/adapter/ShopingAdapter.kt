package com.techvanka.memllo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.techvanka.memllo.databinding.FragmentInboxBinding.inflate
import com.techvanka.memllo.databinding.ShopItemsLayoutBinding

class ShopingAdapter(var context: Context,var list: ArrayList<String>):RecyclerView.Adapter<ShopingAdapter.ViewHolder>(){
    class ViewHolder(var binding: ShopItemsLayoutBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = ShopItemsLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return ShopingAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.binding.shopTextv.text=list[position]
    }

    override fun getItemCount(): Int {
     return  list.size
    }
}

