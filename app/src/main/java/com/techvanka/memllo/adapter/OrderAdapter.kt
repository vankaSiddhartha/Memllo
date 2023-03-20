package com.techvanka.memllo.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.techvanka.memllo.databinding.CancelAlertDailogBinding
import com.techvanka.memllo.databinding.LayoutLanguageBinding
import com.techvanka.memllo.databinding.TrackProductBinding
import com.techvanka.memllo.model.OrderItemModel
import com.techvanka.memllo.ui.ShopItemView

class OrderAdapter(var context: Context, var list:ArrayList<OrderItemModel>):RecyclerView.Adapter<OrderAdapter.ViewHolder>() {
    class ViewHolder(var binding:TrackProductBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = TrackProductBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.card.setOnClickListener {
            var intent = Intent(context, ShopItemView::class.java)
            intent.putExtra("imgUrl",list[position].productImg)
            intent.putExtra("pid",list[position].productId)
            intent.putExtra("money",list[position].productCost)
            intent.putExtra("dis",list[position].productDis)
            intent.putExtra("title",list[position].productName)
            intent.putExtra("c","true")
            context.startActivity(intent)
        }
        Glide.with(context).load(list[position].productImg).into(holder.binding.productIv)
        holder.binding.comment.visibility = View.GONE
        holder.binding.produtTitle.text = list[position].productName.toString()
        holder.binding.cost.text = list[position].productCost
        holder.binding.trackStatus.text = list[position].productTrack
        if (list[position].productTrack.equals("Delivered")) {
            holder.binding.comment.visibility = View.VISIBLE
        }
        holder.binding.closeBtn.setOnClickListener {

            val builder = AlertDialog.Builder(context)
            var bindingVIEW = CancelAlertDailogBinding.inflate(LayoutInflater.from(context))
            builder.setView(bindingVIEW.root)
            val dialog = builder.create()
            dialog.show()
            bindingVIEW.cancelBtn.setOnClickListener {
                    val db = Firebase.firestore
                    db.collection("CancelList").add(list[position])
                        .addOnSuccessListener { documentReference ->
                            dialog.hide()
                            holder.binding.card.visibility =  View.GONE
                            val db = FirebaseFirestore.getInstance()
                            val collectionRef = db.collection("BoughtProducts")
                            val query = collectionRef.whereEqualTo("productId", list[position].productId)
                            query.get()
                                .addOnSuccessListener { documents ->
                                    for (document in documents) {
                                        document.reference.delete()
                                    }
                                }
                                .addOnFailureListener { exception ->
                                    Toast.makeText(context, "$exception", Toast.LENGTH_SHORT).show()
                                    dialog.hide()
                                }
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
                        }

            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}