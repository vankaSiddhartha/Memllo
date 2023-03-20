package com.techvanka.memllo.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.techvanka.memllo.databinding.UserCardLayoutBinding
import com.techvanka.memllo.model.FriendRequestModel
import com.techvanka.memllo.model.User
import com.techvanka.memllo.ui.ChatingActivity

class SwipingAdapter(var context: Context,var list:ArrayList<User>):RecyclerView.Adapter<SwipingAdapter.ViewHolder>() {
    class ViewHolder(var binding:UserCardLayoutBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = UserCardLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (!holder.binding.friendRequestBtn.isVisible){
            holder.binding.friendRequestBtn.visibility= View.VISIBLE
        }


          var data = FriendRequestModel("false",FirebaseAuth.getInstance().currentUser!!.uid,FirebaseAuth.getInstance().currentUser!!.photoUrl.toString(),FirebaseAuth.getInstance().currentUser!!.displayName,FirebaseAuth.getInstance().currentUser!!.uid,list[position].fcmToken)
          var Frienddata = FriendRequestModel("false",list[position].uid,list[position].profile,list[position].name,FirebaseAuth.getInstance().currentUser!!.uid,list[position].fcmToken)
          holder.binding.friendRequestBtn.setOnClickListener {
              FirebaseDatabase.getInstance().getReference("FriendRequest").child(list[position].uid.toString())
                  .child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(data).addOnSuccessListener {
                      FirebaseDatabase.getInstance().getReference("FriendRequest")
                          .child(FirebaseAuth.getInstance().currentUser!!.uid)
                          .child(list[position].uid.toString()).setValue(Frienddata).addOnSuccessListener {
                              Toast.makeText(context, "Friend request has sent to ${list[position].name}", Toast.LENGTH_SHORT).show()
                              holder.binding.friendRequestBtn.visibility = View.GONE
                          }

                  }
          }

        Glide.with(context).load(list[position].profile).into(holder.binding.userImage)
        holder.binding.userTitle.text = list[position].name

    }

    override fun getItemCount(): Int {
        return list.size
    }

}