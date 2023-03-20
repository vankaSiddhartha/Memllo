package com.techvanka.memllo.ui

import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.techvanka.memllo.R
import com.techvanka.memllo.adapter.GridVideoShowAdapter
import com.techvanka.memllo.databinding.ActivityLikedVideosBinding
import com.techvanka.memllo.model.VideoUploadModel

class LikedVideos : AppCompatActivity() {
    private lateinit var binding:ActivityLikedVideosBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setBackgroundDrawable( ColorDrawable( ContextCompat.getColor(this, R.color.btn_text_colour)))
        val titleColor = ContextCompat.getColor(this, R.color.background_colour)
        supportActionBar?.title = HtmlCompat.fromHtml("<font color='$titleColor'>${"Liked Videos"}</font>", HtmlCompat.FROM_HTML_MODE_LEGACY)
        var list = arrayListOf<VideoUploadModel>()
        binding = ActivityLikedVideosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.likedVideoRv.layoutManager = GridLayoutManager(this,3)
        FirebaseDatabase.getInstance().getReference("MyVideo").child(FirebaseAuth.getInstance().currentUser!!.uid)
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(data in snapshot.children){
                        var getData = data.getValue(VideoUploadModel::class.java)
                        list.add(getData!!)
                    }
                    binding.likedVideoRv.adapter = GridVideoShowAdapter(this@LikedVideos,list)
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

    }
}