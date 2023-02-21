package com.techvanka.memllo.ui

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.techvanka.memllo.MainActivity
import com.techvanka.memllo.R
import com.techvanka.memllo.databinding.ActivityShareVideoViewBinding
import com.techvanka.memllo.model.VideoUploadModel
import java.io.File

class ShareVideoView : AppCompatActivity() {
    private lateinit var binding:ActivityShareVideoViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShareVideoViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var IntentId = "https://memllo.page.link?apn=com.techvanka.memllo&ibi=com.example.ios&link="
        FirebaseDatabase.getInstance().getReference("Videos").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var data = snapshot.children
                for (i in data){
                    var getData = i.getValue(VideoUploadModel::class.java)
                    if (intent.getStringExtra("id").equals(IntentId+getData?.videoId)){
                        Toast.makeText(this@ShareVideoView,getData!!.videoLink.toString(), Toast.LENGTH_SHORT).show()

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

                }
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