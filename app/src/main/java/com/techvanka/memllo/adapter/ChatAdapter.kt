package com.techvanka.memllo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.techvanka.memllo.databinding.MessageItemBinding
import com.techvanka.memllo.model.MessageModel

class ChatAdapter(var context: Context,var list: ArrayList<MessageModel>):RecyclerView.Adapter<ChatAdapter.ViewHolder>(){
    private val RECIVE_CODE =0
    private val SEND_CODE=1
    private var CHOISE_CODE =0
    override fun getItemViewType(position: Int): Int {
        if (list[position].SID.equals(FirebaseAuth.getInstance().currentUser!!.uid)){
            return RECIVE_CODE
        }else{
            return  SEND_CODE
        }
    }

    class ViewHolder(var binding:MessageItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = MessageItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        if (viewType==RECIVE_CODE){
          CHOISE_CODE=RECIVE_CODE
        }else{
             CHOISE_CODE = SEND_CODE
        }
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (CHOISE_CODE==RECIVE_CODE){
            holder.binding.sendCard.visibility = View.GONE
            holder.binding.reciveTv.text = list[position].message
        }else{
            holder.binding.reciveCard.visibility = View.GONE
            holder.binding.sendTv.text = list[position].message
        }


    }

    override fun getItemCount(): Int {
        return list.size
    }
}