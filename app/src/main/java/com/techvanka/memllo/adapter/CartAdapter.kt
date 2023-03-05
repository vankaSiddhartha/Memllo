package com.techvanka.memllo.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.techvanka.memllo.databinding.AccountLayoutBinding
import com.techvanka.memllo.databinding.TrackProductBinding
import com.techvanka.memllo.model.ShopingModel
import com.techvanka.memllo.ui.ShopItemView

class CartAdapter(var context: Context,var list: ArrayList<ShopingModel>):RecyclerView.Adapter<CartAdapter.ViewHolder>() {
    class ViewHolder(var binding: TrackProductBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = TrackProductBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CartAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.card.setOnClickListener {
            var intent = Intent(context, ShopItemView::class.java)
            intent.putExtra("imgUrl",list[position].imageList[0])
            intent.putExtra("pid",list[position].productId)
            intent.putExtra("money",list[position].cost)
            intent.putExtra("dis",list[position].discretion)
            intent.putExtra("title",list[position].title)
            intent.putExtra("c","true")
            context.startActivity(intent)
        }
        Glide.with(context).load(list[position].imageList[0]).into(holder.binding.productIv)
        holder.binding.comment.visibility = View.GONE
        holder.binding.produtTitle.text = list[position].title.toString()
        holder.binding.cost.text = list[position].cost
        holder.binding.trackStatus.setText("In your Cart")
        holder.binding.closeBtn.setOnClickListener {
            holder.binding.card.visibility =  View.GONE
            val db = FirebaseFirestore.getInstance()
            val collectionRef = db.collection("Cart")
            val query = collectionRef.whereEqualTo("productId", list[position].productId)
            query.get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        document.reference.delete()
                    }

                }
                .addOnFailureListener { exception ->
                    Toast.makeText(context, "$exception", Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun getItemCount(): Int {
      return list.size
    }

}