package com.techvanka.memllo.ui

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
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
        var comments_count=0
        var getingVideoId = intent.getStringExtra("id")
         getingVideoId = getingVideoId?.replace(IntentId,"")
        FirebaseDatabase.getInstance().getReference("Videos").child(getingVideoId!!).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {


                    var getData = snapshot.getValue(VideoUploadModel::class.java)



                        binding.viewViewVideoTitle.text = getData?.videoTitle
                        Glide.with(this@ShareVideoView).asFile().load(getData?.videoLink)
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
                val view: View = (this@ShareVideoView as FragmentActivity).layoutInflater.inflate(
                    R.layout.fragment_comments_a,
                    null
                )
                val dialog = BottomSheetDialog(view.context)
                dialog.setContentView(view)
                view.findViewById<RecyclerView>(R.id.comment_rv).layoutManager = LinearLayoutManager(view.context)
                var listText = arrayListOf<String>()
                var listS = arrayListOf<CommentDataClass>()
                FirebaseDatabase.getInstance().getReference("Comments")
                    .child(getData?.videoId.toString())
                    .addValueEventListener(object : ValueEventListener {

                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (data in snapshot.children) {
                                var get_data = data.getValue(CommentDataClass::class.java)
                                if (get_data != null) {
                                    listS.add(get_data)
                                    comments_count++

                                }
                            }
                          binding.commentsCount.setText(comments_count.toString())
                            comments_count=0
                            view.findViewById<RecyclerView>(R.id.comment_rv).adapter =
                                CommentsAdapter(view.context, listS)

                            //     view.findViewById<RecyclerView>(listInt[1]).adapter = CommentsAdapter(view.context ,listS,list[postion].videoLink)

                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(view.context, "${error}", Toast.LENGTH_SHORT).show()
                        }

                    })
                listText.add(view.findViewById<EditText>(R.id.comments_et).text.toString())

                view.findViewById<ImageView>(R.id.comments_sendbtn).setOnClickListener {
                    FirebaseDatabase.getInstance().getReference("Comments")
                        .child(getData?.videoId.toString()).push().setValue(
                            CommentDataClass(
                                "RondomMan",
                                view.findViewById<EditText>(R.id.comments_et).text.toString()
                            )
                        )
                    view.findViewById<EditText>(R.id.comments_et).setText("")
                    listS.clear()
                }
              binding.videoHeartBtn.setOnClickListener {
                  if (!binding.videoHeartBtn.isChecked) {
                      binding.videoHeartBtn.setChecked(true);


                  } else {

                      FirebaseDatabase.getInstance().getReference("Likes")
                          .child(getData?.videoId.toString()).push().setValue(
                              Likes(
                                  getData?.videoId,
                                  getData?.videoLink,
                                  FirebaseAuth.getInstance().currentUser!!.uid

                              )
                          )
                      // count likes
                      getLikes(getData?.videoId, binding.likesCount, binding.videoHeartBtn)
                  }
              }
                getLikes(getData?.videoId, binding.likesCount, binding.videoHeartBtn)



               binding.videoListComment.setOnClickListener {

                    dialog.show()

                }
                Glide.with(this@ShareVideoView).load(getData?.CreatorProfile).into(binding.userProfile)
                binding.viewViewVideoTitle.text = getData?.videoTitle
                binding.videoViewUserName.text = getData?.CreatorName

            }


            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
            })







            }
    fun getLikes(videoId:String?, likesCount: TextView, videoHeartBtn: CheckBox) {
        var k = 0
        var im=0

        FirebaseDatabase.getInstance().getReference("Likes")
            .child(videoId.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (d in snapshot.children) {
                        try {
                            var data = d.getValue(Likes::class.java)
                            if (data!!.user_Id.equals(FirebaseAuth.getInstance().currentUser!!.uid)) {
                                videoHeartBtn.setChecked(true);
                                k=1
                                //   Toast.makeText(context, "True", Toast.LENGTH_SHORT).show()

                            }

                        } catch (e: Exception) {

                        }
                        im++
                    }
                    if (im == 0) {
                        likesCount.setText("0")
                    }else {
                        // Toast.makeText(context, "$i", Toast.LENGTH_SHORT).show()
                        likesCount.setText(im.toString())
                        im=0
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