package com.techvanka.memllo.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.techvanka.memllo.databinding.FragmentInboxBinding.inflate
import com.techvanka.memllo.databinding.ShopItemsLayoutBinding
import com.techvanka.memllo.model.ShopingModel
import com.techvanka.memllo.ui.ShopItemView

class ShopingAdapter(var context: Context,var list: ArrayList<ShopingModel>):RecyclerView.Adapter<ShopingAdapter.ViewHolder>(){
    class ViewHolder(var binding: ShopItemsLayoutBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = ShopItemsLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return ShopingAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(list[position].imageList[0]).into(holder.binding.shopIv)
        holder.binding.shopTextv.text = list[position].title
        holder.binding.shopIv.setOnClickListener {
            var intent = Intent(context,ShopItemView::class.java)
            intent.putExtra("imgUrl",list[position].imageList[0])
            intent.putExtra("pid",list[position].productId)
            intent.putExtra("money",list[position].cost)
            intent.putExtra("dis",list[position].discretion)
            intent.putExtra("title",list[position].title)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
     return  list.size
    }
}

