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
import com.techvanka.memllo.databinding.FriendReqLayoutBinding
import com.techvanka.memllo.model.FriendRequestModel

class FriendRequestAdapter(var context: Context,var list:ArrayList<FriendRequestModel>):RecyclerView.Adapter<FriendRequestAdapter.ViewHolder>() {
    class  ViewHolder(var binding:FriendReqLayoutBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = FriendReqLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.pendingText.visibility = View.GONE
        if (list[position].isFriend.equals("true")){
            holder.binding.declineBtn.visibility = View.GONE
            holder.binding.acceptBtn.visibility = View.GONE
            holder.binding.friendProflile.visibility = View.GONE
            holder.binding.userTv.visibility = View.GONE
        }
        holder.binding.pendingText.visibility = View.GONE
        Glide.with(context).load(list[position].profile).into(holder.binding.friendProflile)
        holder.binding.userTv.text =list[position].name
        if (FirebaseAuth.getInstance().currentUser!!.uid == list[position].reqUid){
            holder.binding.declineBtn.visibility = View.GONE
            holder.binding.acceptBtn.visibility = View.GONE
            holder.binding.pendingText.visibility = View.VISIBLE
        }
        var Frienddata = FriendRequestModel("true",list[position].uid,list[position].profile,list[position].name)
        var data = FriendRequestModel("true",FirebaseAuth.getInstance().currentUser!!.uid,FirebaseAuth.getInstance().currentUser!!.photoUrl.toString(),FirebaseAuth.getInstance().currentUser!!.displayName)

       holder.binding.acceptBtn.setOnClickListener {
           FirebaseDatabase.getInstance().getReference("FriendRequest").child(FirebaseAuth.getInstance().currentUser!!.uid)
               .child(list[position].uid.toString()).setValue(Frienddata).addOnSuccessListener {
               FirebaseDatabase.getInstance().getReference("FriendRequest").child(list[position].uid.toString()).child(FirebaseAuth.getInstance().currentUser!!.uid)
                   .setValue(data).addOnSuccessListener {
                       Toast.makeText(context, "Congratulations your are friend with ${list[position].name}", Toast.LENGTH_SHORT).show()
                       holder.binding.acceptBtn.visibility = View.GONE
                       holder.binding.declineBtn.visibility = View.GONE
                   }

               }
       }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}