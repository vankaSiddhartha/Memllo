package com.techvanka.memllo.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.techvanka.memllo.databinding.FriendLayoutBinding
import com.techvanka.memllo.model.User
import com.techvanka.memllo.ui.CreatorMemesActivity

class FallowingPeopleAdapter(var context: Context,var list:ArrayList<User>):RecyclerView.Adapter<FallowingPeopleAdapter.ViewHolder> (){
    class ViewHolder(var binding:FriendLayoutBinding):RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(FriendLayoutBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.latestMess.visibility = View.GONE
        Glide.with(context).load(list[position].profile).into(holder.binding.userSearchIv)
        holder.binding.userTv.setText(list[position].name)
        holder.binding.cardFri.setOnClickListener {
            intentToNewActivity(position)
        }
    }
    private fun intentToNewActivity(position: Int) {
        var intent = Intent(context, CreatorMemesActivity::class.java)
        intent.putExtra("cid",list[position].uid)
        intent.putExtra("name",list[position].name)
        intent.putExtra("profile",list[position].profile)

            intent.putExtra("fallow","true")


        context.startActivity(intent)
    }
}