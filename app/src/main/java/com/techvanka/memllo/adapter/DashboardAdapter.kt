package com.techvanka.memllo.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.techvanka.memllo.databinding.DashBoardItemBinding
import com.techvanka.memllo.model.VideoUploadModel
import com.techvanka.memllo.roomDB.Model
import com.techvanka.memllo.ui.GetMoneyActivity
import java.lang.Float
import kotlin.properties.Delegates

class DashboardAdapter(var context: Context, var list: ArrayList <VideoUploadModel>):RecyclerView.Adapter<DashboardAdapter.ViewHolder>() {
    class ViewHolder(var binding:DashBoardItemBinding):RecyclerView.ViewHolder(binding.root)

 private var Money:Int =0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = DashBoardItemBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      Glide.with(context).load(list[position].videoLink).into(holder.binding.dashboardIv)
        countComments(position,holder.binding.commentsTv)

       countLikes(position,holder.binding.likesTv)
     countViews(position,holder.binding.viewTv)
     countMoney(position,holder.binding.viewTv,holder.binding.money,holder.binding.likesTv)
        holder.binding.videosCard.setOnClickListener {
           var views: Double = holder.binding.viewTv.text.toString().toDouble()*0.04
            var likes:Double = holder.binding.likesTv.text.toString().toDouble()*0.1
            var money = views+likes
            holder.binding.money.setText(money.toString())

        }
        holder.binding.getMoneyBtn.setOnClickListener {
            var views: Double = holder.binding.viewTv.text.toString().toDouble()*0.04
            var likes:Double = holder.binding.likesTv.text.toString().toDouble()*0.1
            var money = views+likes
            var Intent = Intent(context,GetMoneyActivity::class.java)
            Intent.putExtra("money",money.toString())
            context.startActivity(Intent)
        }






    }

    private fun countMoney(position: Int, viewTv: TextView, money: TextView, likesTv: TextView) {
        Toast.makeText(context, Money.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun getItemCount(): Int {
        return  list.size
    }
    fun countLikes(postion:Int,likeText:TextView) {
        var likeCount =0
        FirebaseDatabase.getInstance().getReference("Likes").child(list[postion].videoId.toString()).addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (i in snapshot.children){
                    likeCount++

                }

                likeText.setText(likeCount.toString())

                Money += likeCount


            }

            override fun onCancelled(error: DatabaseError) {

            }


        })


    }
    fun countViews(postion: Int,ViewsText: TextView) {
        var view =0
       FirebaseDatabase.getInstance().getReference("Views").child(FirebaseAuth.getInstance().currentUser!!.uid).child(list[postion].videoId.toString())
           .addValueEventListener(object : ValueEventListener{
               override fun onDataChange(snapshot: DataSnapshot) {
                for (i in snapshot.children){
                    view++
                }

                   ViewsText.setText(view.toString())
                  Money +=view

               }

               override fun onCancelled(error: DatabaseError) {

               }

           })


    }
    fun countComments(postion: Int,Comments:TextView) {
        var commentsCount=0
        FirebaseDatabase.getInstance().getReference("Comments").child(list[postion].videoId.toString())
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                   for (i in snapshot.children){
                       commentsCount++
                   }
                    Comments.setText(commentsCount.toString())


                }


                override fun onCancelled(error: DatabaseError) {

                }

            })

    }
}