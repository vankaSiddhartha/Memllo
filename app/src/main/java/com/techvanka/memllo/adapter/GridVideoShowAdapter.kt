package com.techvanka.memllo.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Adapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.bumptech.glide.Glide
import com.techvanka.memllo.databinding.VideoThumbnailLayoutBinding
import com.techvanka.memllo.model.VideoUploadModel
import com.techvanka.memllo.ui.GridVideoPlayer
import com.techvanka.memllo.ui.IntentLinkActivity

class GridVideoShowAdapter(var context:Context,var list:ArrayList<VideoUploadModel>):RecyclerView.Adapter<GridVideoShowAdapter.ViewHolder>() {
    class ViewHolder(var binding:VideoThumbnailLayoutBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(VideoThumbnailLayoutBinding.inflate(LayoutInflater.from(context),parent,false))
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
          Glide.with(context).load(list[position].videoLink).into(holder.binding.imageView)
        holder.binding.imageView.setOnClickListener {
            list.shuffle()
            var vidPostion =    list.indexOf(list.find { it.videoId!!.contains(list[position].videoId.toString()) })
            list[0] = list[vidPostion]
            list[vidPostion] = list[0]
            var intent = Intent(context,GridVideoPlayer::class.java)
             intent.putExtra("vid",list[position].videoId)
            intent.putExtra("cid",list[position].CreatorId)
            intent.putExtra("pos",position.toString())
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


}