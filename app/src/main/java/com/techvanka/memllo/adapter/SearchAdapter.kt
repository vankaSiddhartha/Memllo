package com.techvanka.memllo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.techvanka.memllo.databinding.UserProfileBinding
import com.techvanka.memllo.model.FriendRequestModel
import com.techvanka.memllo.model.User

class SearchAdapter(var context: Context, var list:ArrayList<User>,var user_name:String):RecyclerView.Adapter<SearchAdapter.ViewHolder>() {
    class ViewHolder(var binding:UserProfileBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = UserProfileBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var data = FriendRequestModel("false",FirebaseAuth.getInstance().currentUser!!.uid,FirebaseAuth.getInstance().currentUser!!.photoUrl.toString(),FirebaseAuth.getInstance().currentUser!!.displayName,FirebaseAuth.getInstance().currentUser!!.uid)
        var Frienddata = FriendRequestModel("false",list[position].uid,list[position].profile,list[position].name,FirebaseAuth.getInstance().currentUser!!.uid)
        holder.binding.addFriend.setOnClickListener {
            FirebaseDatabase.getInstance().getReference("FriendRequest").child(list[position].uid.toString())
                .child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(data).addOnSuccessListener {
                    FirebaseDatabase.getInstance().getReference("FriendRequest")
                        .child(FirebaseAuth.getInstance().currentUser!!.uid)
                        .child(list[position].uid.toString()).setValue(Frienddata).addOnSuccessListener {
                            Toast.makeText(context, "Friend request has sent to ${list[position].name}", Toast.LENGTH_SHORT).show()
                            holder.binding.addFriend.visibility = View.GONE
                        }

                }
        }

        if (list[position].name!!.contains(user_name, ignoreCase = true)) {
            Glide.with(context).load(list[position].profile).into(holder.binding.userSearchIv)
            holder.binding.userTv.text = list[position].name
        }else{
            holder.binding.userSearchIv.visibility = View.GONE
            holder.binding.userTv.visibility = View.GONE
            holder.binding.addFriend.visibility = View.GONE
        }

    }

    override fun getItemCount(): Int {
      return list.size
    }
}