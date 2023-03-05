package com.techvanka.memllo.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.techvanka.memllo.databinding.AccountLayoutBinding
import com.techvanka.memllo.ui.CartShowActivity
import com.techvanka.memllo.ui.ProductTrackActivity

class AccountAdapter(var list:ArrayList<Int>,var context: Context,var textlist:ArrayList<String>):RecyclerView.Adapter<AccountAdapter.ViewHolder>() {
   class ViewHolder(var binding:AccountLayoutBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AccountLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
     holder.binding.icon.setImageResource(list[position])
        holder.binding.iconText.text = textlist[position]
        holder.binding.iconText.setOnClickListener {
            if (textlist[position] == "My Orders"){
                context.startActivity(Intent(context,ProductTrackActivity::class.java))
            }else if(textlist[position]=="Cart"){
                context.startActivity(Intent(context,CartShowActivity::class.java))
            }
        }
    }

    override fun getItemCount(): Int {
        return  list.size
    }
}