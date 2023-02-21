package com.techvanka.memllo.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.exoplayer2.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.dynamiclinks.ktx.androidParameters
import com.google.firebase.dynamiclinks.ktx.dynamicLink
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.dynamiclinks.ktx.iosParameters
import com.google.firebase.ktx.Firebase
import com.techvanka.memllo.databinding.VideoItemBinding
import com.techvanka.memllo.model.CommentDataClass
import com.techvanka.memllo.model.Likes
import com.techvanka.memllo.model.VideoUploadModel
import kotlinx.coroutines.*
import java.io.File


class VideoAdapter(
    var context: Context,
    var list: ArrayList<VideoUploadModel>, private val listInt:ArrayList<Int>
):RecyclerView.Adapter<VideoAdapter.ViewHolder>() {
    private var exoPlayer: ExoPlayer? = null
    private var playbackPosition = 0L
    private var playWhenReady = true
    val videoJob: Job? = null

    var comments_count=0


    class ViewHolder(var binding: VideoItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = VideoItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       var IntentId = "https://memllo.page.link?apn=com.techvanka.memllo&ibi=com.example.ios&link="

        Glide.with(context).load(list[position].CreatorProfile).into(holder.binding.userProfile)


        if (holder.binding.videoHeartBtn.isChecked) {
            holder.binding.videoHeartBtn.setChecked(false);
        }


            holder.binding.videoViewUserName.text = list[position].CreatorName
            holder.binding.viewViewVideoTitle.text = list[position].videoTitle


            holder.binding.playerView.setOnCompletionListener { mediaPlayer -> mediaPlayer.start() }

            Glide.with(context).asFile().load(list[position].videoLink)
                .into(object : CustomTarget<File?>() {
                    override fun onResourceReady(
                        resource: File,
                        transition: Transition<in File?>?
                    ) {
                        val uri = Uri.fromFile(resource)

                        holder.binding.playerView.setVideoURI(uri)
                        holder.binding.playerView.setOnPreparedListener {
                            it.isLooping = true
                            it.start()
                            holder.binding.pbLoading.visibility = View.GONE
                        }

                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        TODO("Not yet implemented")
                    }


                })


            val view: View = (context as FragmentActivity).layoutInflater.inflate(
                listInt[0],
                null
            )
            val dialog = BottomSheetDialog(view.context)
            dialog.setContentView(view)
            view.findViewById<RecyclerView>(listInt[1]).layoutManager = LinearLayoutManager(context)
            var listText = arrayListOf<String>()


            var listS = arrayListOf<CommentDataClass>()

//            FirebaseDatabase.getInstance().getReference("Comments").child(list[position].videoId.toString()).push().setValue(CommentDataClass("RondomMan",view.findViewById<EditText>(listInt[2]).text.toString()))
//            getData(position)
            FirebaseDatabase.getInstance().getReference("Comments")
                .child(list[position].videoId.toString())
                .addValueEventListener(object : ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (data in snapshot.children) {
                            var get_data = data.getValue(CommentDataClass::class.java)
                            if (get_data != null) {
                                listS.add(get_data)
                                comments_count++

                            }
                        }
                        holder.binding.commentsCount.setText(comments_count.toString())
                        comments_count=0
                        view.findViewById<RecyclerView>(listInt[1]).adapter =
                            CommentsAdapter(view.context, listS)

                        //     view.findViewById<RecyclerView>(listInt[1]).adapter = CommentsAdapter(view.context ,listS,list[postion].videoLink)

                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(view.context, "${error}", Toast.LENGTH_SHORT).show()
                    }

                })



            listText.add(view.findViewById<EditText>(listInt[2]).text.toString())

            view.findViewById<ImageView>(listInt[3]).setOnClickListener {
                FirebaseDatabase.getInstance().getReference("Comments")
                    .child(list[position].videoId.toString()).push().setValue(
                        CommentDataClass(
                            "RondomMan",
                            view.findViewById<EditText>(listInt[2]).text.toString()
                        )
                    )
                view.findViewById<EditText>(listInt[2]).setText("")
                listS.clear()
            }
            holder.binding.videoHeartBtn.setOnClickListener {
                if (!holder.binding.videoHeartBtn.isChecked){
                    holder.binding.videoHeartBtn.setChecked(true);


                }else {

                    FirebaseDatabase.getInstance().getReference("Likes")
                        .child(list[position].videoId.toString()).push().setValue(
                            Likes(
                                list[position].videoLink,
                                list[position].videoLink,
                                FirebaseAuth.getInstance().currentUser!!.uid

                            )
                        )
                    // count likes
                    getLikes(position, holder.binding.likesCount, holder.binding.videoHeartBtn)


                }






            }
        getLikes(position, holder.binding.likesCount, holder.binding.videoHeartBtn)



        holder.binding.videoListComment.setOnClickListener {

            dialog.show()

        }

        holder.binding.share.setOnClickListener {
            val dynamicLink = Firebase.dynamicLinks.dynamicLink {
                link = Uri.parse(list[position].videoId)
                domainUriPrefix = "https://memllo.page.link"
                // Open links with this app on Android
                androidParameters { }
                // Open links with com.example.ios on iOS
                iosParameters("com.example.ios") { }
            }

            val dynamicLinkUri = dynamicLink.uri
            Log.e("mess","${dynamicLinkUri}")
        }

    }

        fun getLikes(position: Int, likesCount: TextView, videoHeartBtn: CheckBox) {
            var k = 0
            var i=0

            FirebaseDatabase.getInstance().getReference("Likes")
                .child(list[position].videoId.toString())
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
                            i++
                        }
                        if (i == 0) {
                            likesCount.setText("0")
                        }else {
                            // Toast.makeText(context, "$i", Toast.LENGTH_SHORT).show()
                            likesCount.setText(i.toString())
                            i=0
                        }


                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })

        }



        override fun getItemCount(): Int {
            return list.size
        }

        fun gib() {

        }

        fun getData(postion: Int) {
            var listS = arrayListOf<CommentDataClass>()

            val view: View = (context as FragmentActivity).layoutInflater.inflate(
                listInt[0],
                null
            )
            view.findViewById<RecyclerView>(listInt[1]).layoutManager =
                LinearLayoutManager(view.context)
            FirebaseDatabase.getInstance().getReference("Comments")
                .child(list[postion].videoId.toString())
                .addValueEventListener(object : ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (data in snapshot.children) {
                            var get_data = data.getValue(CommentDataClass::class.java)
                            if (get_data != null) {
                                listS.add(get_data)
                                // Toast.makeText(view.context, "$get_data", Toast.LENGTH_SHORT).show()
                            }
                        }

                        //     view.findViewById<RecyclerView>(listInt[1]).adapter = CommentsAdapter(view.context ,listS,list[postion].videoLink)

                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(view.context, "${error}", Toast.LENGTH_SHORT).show()
                    }

                })


        }
    }

