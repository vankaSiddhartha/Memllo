package com.techvanka.memllo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.techvanka.memllo.databinding.CommentsListBinding
import com.techvanka.memllo.model.CommentDataClass

class CommentsAdapter(
    var context: Context,
    var data: ArrayList<CommentDataClass>

):RecyclerView.Adapter<CommentsAdapter.ViewHolder>(){
    class ViewHolder(var binding:CommentsListBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = CommentsListBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

       holder.binding.commentText.text = data[position].text
        holder.binding.userName.text = "Random User"
        //data.clear()
      //  Toast.makeText(context, "vhhvhb", Toast.LENGTH_SHORT).show()

    }

    override fun getItemCount(): Int {
       return data.size
    }
}