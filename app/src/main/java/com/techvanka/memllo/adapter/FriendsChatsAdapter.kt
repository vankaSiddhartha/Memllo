package com.techvanka.memllo.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.techvanka.memllo.databinding.FriendLayoutBinding
import com.techvanka.memllo.databinding.UserProfileBinding
import com.techvanka.memllo.model.FriendRequestModel
import com.techvanka.memllo.ui.ChatingActivity

class FriendsChatsAdapter(var context: Context,var list:ArrayList<FriendRequestModel>):RecyclerView.Adapter<FriendsChatsAdapter.ViewHolder>() {
    class ViewHolder(var binding:FriendLayoutBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = FriendLayoutBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Glide.with(context).load(list[position].profile).into(holder.binding.userSearchIv)
        holder.binding.userTv.text = list[position].name
        holder.binding.cardFri.setOnClickListener {
            var intent = Intent(context,ChatingActivity::class.java)
            intent.putExtra("name",list[position].name)
            intent.putExtra("rid",list[position].uid)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return  list.size
    }


}