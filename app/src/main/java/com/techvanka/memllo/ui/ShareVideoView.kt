package com.techvanka.memllo.ui

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.techvanka.memllo.MainActivity
import com.techvanka.memllo.R
import com.techvanka.memllo.adapter.CommentsAdapter
import com.techvanka.memllo.databinding.ActivityShareVideoViewBinding
import com.techvanka.memllo.model.CommentDataClass
import com.techvanka.memllo.model.Likes
import com.techvanka.memllo.model.VideoUploadModel
import java.io.File

class ShareVideoView : AppCompatActivity() {
    private lateinit var binding:ActivityShareVideoViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShareVideoViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var IntentId = "https://memllo.page.link?apn=com.techvanka.memllo&ibi=com.example.ios&link="
        var getingVideoId = intent.getStringExtra("id")
         getingVideoId = getingVideoId?.replace(IntentId,"")
        FirebaseDatabase.getInstance().getReference("Videos").child(getingVideoId!!).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {


                    var getData = snapshot.getValue(VideoUploadModel::class.java)

                

                        binding.viewViewVideoTitle.text = getData.videoTitle
                        Glide.with(this@ShareVideoView).asFile().load(getData.videoLink)
                            .into(object : CustomTarget<File?>() {
                                override fun onResourceReady(
                                    resource: File,
                                    transition: Transition<in File?>?
                                ) {
                                    val uri = Uri.fromFile(resource)

                                   binding.playerView1.setVideoURI(uri)
                                  binding.playerView1.setOnPreparedListener {
                                        it.isLooping = true
                                        it.start()
                                      binding.pbLoading.visibility = View.GONE
                                    }

                                }

                                override fun onLoadCleared(placeholder: Drawable?) {

                                }


                            })




            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })





            }

    override fun onPause() {
        super.onPause()
        startActivity(Intent(this,MainActivity::class.java))
    }
}