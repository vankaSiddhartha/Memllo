package com.techvanka.memllo.adapter

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Looper

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.exoplayer2.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.dynamiclinks.ktx.androidParameters
import com.google.firebase.dynamiclinks.ktx.dynamicLink
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.dynamiclinks.ktx.iosParameters
import com.google.firebase.ktx.Firebase
import com.techvanka.memllo.databinding.VideoItemBinding
import com.techvanka.memllo.model.CommentDataClass
import com.techvanka.memllo.model.User
import com.techvanka.memllo.model.VideoUploadModel
import com.techvanka.memllo.ui.CreatorMemesActivity
import kotlinx.coroutines.*
import java.io.File


class VideoAdapter(
    var context: Context,
    var list: ArrayList<VideoUploadModel>,
    private val listInt: ArrayList<Int>
):RecyclerView.Adapter<VideoAdapter.ViewHolder>() {
    private var exoPlayer: ExoPlayer? = null
    private var playbackPosition = 0L
    private var playWhenReady = true
    val videoJob: Job? = null

    var comments_count = 0


    class ViewHolder(var binding: VideoItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = VideoItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        CoroutineScope(Dispatchers.Default).launch {
            getLikes(position,holder.binding.likesCount,holder.binding.videoHeartBtn)
        }
        holder.binding.videoViewUserName.setOnClickListener {
            intentToNewActivity(position,holder.binding.followBtn)
        }
        android.os.Handler(Looper.getMainLooper()).postDelayed({
            countViews(position)
        },15000)
        holder.binding.pause.visibility = View.GONE
        holder.binding.play.visibility = View.GONE
        var isPlaying = true
        holder.binding.playerView.setOnClickListener {
            if (isPlaying) {
                // Video is currently playing, pause it
                holder.binding.pause.visibility = View.VISIBLE

                android.os.Handler(Looper.getMainLooper()).postDelayed({
                    holder.binding.pause.visibility = View.GONE
                },500)
              holder.binding.playerView.pause()

                isPlaying = false
            } else {
                // Video is currently paused, start it
                holder.binding.play.visibility = View.VISIBLE

                android.os.Handler(Looper.getMainLooper()).postDelayed({
                    holder.binding.play.visibility = View.GONE
                },500)
                holder.binding.playerView.start()

                isPlaying = true
            }
        }

//       android.os.Handler(Looper.getMainLooper()).postDelayed({
//           countViews(position)
//       },15000)
        Glide.with(context).load(list[position].CreatorProfile).into(holder.binding.userProfile)

        if (holder.binding.followBtn.visibility == View.GONE) {
            holder.binding.followBtn.visibility = View.VISIBLE
        }
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

                }


            })


        val view: View = (context as FragmentActivity).layoutInflater.inflate(
            listInt[0],
            null
        )
        val dialog = BottomSheetDialog(view.context)
        dialog.setContentView(view)
        val layoutManager = LinearLayoutManager(context)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true


        view.findViewById<RecyclerView>(listInt[1]).layoutManager = layoutManager

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
                    comments_count = 0
                    view.findViewById<RecyclerView>(listInt[1]).adapter =
                        CommentsAdapter(view.context, listS)

                    //     view.findViewById<RecyclerView>(listInt[1]).adapter = CommentsAdapter(view.context ,listS,list[postion].videoLink)

                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(view.context, "${error}", Toast.LENGTH_SHORT).show()
                }

            })



        listText.add(view.findViewById<EditText>(listInt[2]).text.toString())
        val sharedPref = context.getSharedPreferences("myProfile", Context.MODE_PRIVATE)
        val myProfile = sharedPref.getString("plink", "no")
        val myName = sharedPref.getString("pname","no")
        if (myProfile.equals("no")) {
            if (myName.equals("no")) {
                view.findViewById<ImageView>(listInt[3]).setOnClickListener {

                    FirebaseDatabase.getInstance().getReference("Comments")
                        .child(list[position].videoId.toString()).push().setValue(
                            CommentDataClass(
                                FirebaseAuth.getInstance().currentUser!!.photoUrl.toString(),
                                FirebaseAuth.getInstance().currentUser!!.displayName,
                                view.findViewById<EditText>(listInt[2]).text.toString()
                            )
                        )

                    view.findViewById<EditText>(listInt[2]).setText("")
                    listS.clear()
                }
            }else{
                view.findViewById<ImageView>(listInt[3]).setOnClickListener {

                    FirebaseDatabase.getInstance().getReference("Comments")
                        .child(list[position].videoId.toString()).push().setValue(
                            CommentDataClass(
                                FirebaseAuth.getInstance().currentUser!!.photoUrl.toString(),
                                myName,
                                view.findViewById<EditText>(listInt[2]).text.toString()
                            )
                        )

                    view.findViewById<EditText>(listInt[2]).setText("")
                    listS.clear()
                }
            }
        }else{
            if (myName.equals("no")) {
                view.findViewById<ImageView>(listInt[3]).setOnClickListener {
                    FirebaseDatabase.getInstance().getReference("Comments")
                        .child(list[position].videoId.toString()).push().setValue(
                            CommentDataClass(
                                myProfile,
                                FirebaseAuth.getInstance().currentUser!!.displayName,
                                view.findViewById<EditText>(listInt[2]).text.toString()
                            )
                        )
                    view.findViewById<EditText>(listInt[2]).setText("")
                    listS.clear()
                }
            }else{
                view.findViewById<ImageView>(listInt[3]).setOnClickListener {
                    FirebaseDatabase.getInstance().getReference("Comments")
                        .child(list[position].videoId.toString()).push().setValue(
                            CommentDataClass(
                                myProfile,
                               myName,
                                view.findViewById<EditText>(listInt[2]).text.toString()
                            )
                        )
                    view.findViewById<EditText>(listInt[2]).setText("")
                    listS.clear()
                }
            }
        }


//        holder.binding.videoHeartBtn.setOnCheckedChangeListener { _, isChecked ->
//            if (isChecked) {
//                FirebaseDatabase.getInstance().getReference("Likes")
//                    .child(list[position].videoId.toString()).child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(
//                        FirebaseAuth.getInstance().currentUser!!.uid
//                    ).addOnSuccessListener{
//                        FirebaseDatabase.getInstance().getReference("MyLikes").child(FirebaseAuth.getInstance().currentUser!!.uid).child(list[position].videoId.toString()).setValue( list)
//                        getLikes(position, holder.binding.likesCount, holder.binding.videoHeartBtn)
//                    }
//            } else {
//              var ref =   FirebaseDatabase.getInstance().getReference("Likes").child(list[position].videoId.toString())
//                var rif = FirebaseDatabase.getInstance().getReference("MyLikes").child(FirebaseAuth.getInstance().currentUser!!.uid)
//
//                ref.removeValue()
//                rif.child(list[position].videoId.toString()).removeValue()
//            }
        holder.binding.videoHeartBtn.setOnClickListener{
            if (holder.binding.videoHeartBtn.isChecked){
                FirebaseDatabase.getInstance().getReference("Likes")
                    .child(list[position].videoId.toString()).child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(
                        FirebaseAuth.getInstance().currentUser!!.uid
                    ).addOnSuccessListener{
                        FirebaseDatabase.getInstance().getReference("MyLikes").child(FirebaseAuth.getInstance().currentUser!!.uid).child(list[position].videoId.toString()).setValue( list)
                        getLikes(position, holder.binding.likesCount, holder.binding.videoHeartBtn)
                    }
            }else{
                var ref =   FirebaseDatabase.getInstance().getReference("Likes").child(list[position].videoId.toString())
                var rif = FirebaseDatabase.getInstance().getReference("MyLikes").child(FirebaseAuth.getInstance().currentUser!!.uid)

                ref.child(FirebaseAuth.getInstance().currentUser!!.uid).removeValue()
                rif.child(list[position].videoId.toString()).removeValue()
            }
        }
//        }
//        holder.binding.videoHeartBtn.setOnClickListener {
//            if (!holder.binding.videoHeartBtn.isChecked) {
//                holder.binding.videoHeartBtn.setChecked(true);
//
//
//            } else {
//
//                FirebaseDatabase.getInstance().getReference("Likes")
//                    .child(list[position].videoId.toString()).child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(
//                       FirebaseAuth.getInstance().currentUser!!.uid
//                    ).addOnSuccessListener{
//                        FirebaseDatabase.getInstance().getReference("MyLikes").child(FirebaseAuth.getInstance().currentUser!!.uid).setValue( Likes(
//                            list[position].videoId,
//                            list[position].videoLink,
//                            FirebaseAuth.getInstance().currentUser!!.uid
//
//                        ))
//                    }
//                // count likes
//
//
//
//            }
//
//
//        }
     //   getLikes(position, holder.binding.likesCount, holder.binding.videoHeartBtn)



        holder.binding.videoListComment.setOnClickListener {

            dialog.show()

        }
        holder.binding.followBtn.setOnClickListener {
            followBtnClick(holder.binding.followBtn, position)
        }
        followCheck(position, holder.binding.followBtn)

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
            //Log.e("mess","${dynamicLinkUri}")
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, dynamicLinkUri.toString());
            context.startActivity(Intent.createChooser(shareIntent, "Send to"))
        }

    }

    private fun intentToNewActivity(position: Int, followBtn: AppCompatButton) {
        var intent = Intent(context,CreatorMemesActivity::class.java)
        intent.putExtra("cid",list[position].CreatorId)
        intent.putExtra("name",list[position].CreatorName)
        intent.putExtra("profile",list[position].CreatorProfile)
        if (followBtn.visibility==View.GONE){
            intent.putExtra("fallow","true")
        }else{
            intent.putExtra("fallow","false")
        }

        context.startActivity(intent)
    }

    fun getLikes(position: Int, likesCount: TextView, videoHeartBtn: CheckBox) {
        var k = 0
        var i = 0

        FirebaseDatabase.getInstance().getReference("Likes")
            .child(list[position].videoId.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (d in snapshot.children) {
                        try {
                            var data = d.getValue(String::class.java)
                            if (data.equals(FirebaseAuth.getInstance().currentUser!!.uid)) {
                                videoHeartBtn.isChecked = true;
                                k = 1
                                //   Toast.makeText(context, "True", Toast.LENGTH_SHORT).show()

                            }

                        } catch (e: Exception) {

                        }
                        i++
                    }
                    if (i == 0) {
                        likesCount.text = "0"
                    } else {
                        // Toast.makeText(context, "$i", Toast.LENGTH_SHORT).show()
                        likesCount.setText(i.toString())
                        i = 0
                    }


                }

                override fun onCancelled(error: DatabaseError) {

                }

            })


    }


    override fun getItemCount(): Int {
        return list.size
    }

    fun followBtnClick(btn: Button, position: Int) {
        FirebaseDatabase.getInstance().getReference("IFallow").child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child(list[position].CreatorId.toString()).setValue(User(list[position].CreatorName,list[position].CreatorId,list[position].CreatorProfile.toString()))
        val sharedPref = context.getSharedPreferences("myProfile", Context.MODE_PRIVATE)
        val myProfile = sharedPref.getString("plink", "no")
        val myName = sharedPref.getString("pname", "no")
        if (myProfile.equals("no")) {
            if (myName.equals("no")) {
                FirebaseDatabase.getInstance().getReference("Fallow")
                    .child(list[position].CreatorId.toString())
                    .child(FirebaseAuth.getInstance().currentUser!!.uid)
                    .setValue(
                        User(
                            FirebaseAuth.getInstance().currentUser!!.displayName.toString(),
                            FirebaseAuth.getInstance().currentUser!!.uid,
                            FirebaseAuth.getInstance().currentUser!!.photoUrl.toString()
                        )
                    ).addOnSuccessListener {
                        btn.visibility = View.GONE
                        FirebaseDatabase.getInstance().getReference("IFallow").child(FirebaseAuth.getInstance().currentUser!!.uid)
                            .child(list[position].CreatorId.toString()).setValue(User(list[position].CreatorName,list[position].CreatorId,list[position].CreatorProfile.toString()))
                    }
            }else{
                FirebaseDatabase.getInstance().getReference("Fallow")
                    .child(list[position].CreatorId.toString())
                    .child(FirebaseAuth.getInstance().currentUser!!.uid)
                    .setValue(
                        User(
                            myName,
                            FirebaseAuth.getInstance().currentUser!!.uid,
                            FirebaseAuth.getInstance().currentUser!!.photoUrl.toString()
                        )
                    ).addOnSuccessListener {
                        btn.visibility = View.GONE
                        FirebaseDatabase.getInstance().getReference("IFallow").child(FirebaseAuth.getInstance().currentUser!!.uid)
                            .child(list[position].CreatorId.toString()).setValue(User(list[position].CreatorName,list[position].CreatorId,list[position].CreatorProfile.toString()))
                    }
            }
        }else {
            if (myName.equals("no")) {
                FirebaseDatabase.getInstance().getReference("Fallow")
                    .child(list[position].CreatorId.toString())
                    .child(FirebaseAuth.getInstance().currentUser!!.uid)
                    .setValue(
                        User(
                            FirebaseAuth.getInstance().currentUser!!.displayName,
                            FirebaseAuth.getInstance().currentUser!!.uid,
                            myProfile
                        )
                    ).addOnSuccessListener {
                        btn.visibility = View.GONE
                        FirebaseDatabase.getInstance().getReference("IFallow").child(FirebaseAuth.getInstance().currentUser!!.uid)
                            .child(list[position].CreatorId.toString()).setValue(User(list[position].CreatorName,list[position].CreatorId,list[position].CreatorProfile.toString()))
                    }
            }else{
                FirebaseDatabase.getInstance().getReference("Fallow")
                    .child(list[position].CreatorId.toString())
                    .child(FirebaseAuth.getInstance().currentUser!!.uid)
                    .setValue(
                        User(
                            myName,
                            FirebaseAuth.getInstance().currentUser!!.uid,
                            myProfile
                        )
                    ).addOnSuccessListener {
                        btn.visibility = View.GONE

                    }
            }
        }
    }





    fun followCheck(position: Int, btn: Button){
        try {
            FirebaseDatabase.getInstance().getReference("Fallow")
                .child(list[position].CreatorId.toString())
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        for (idata in snapshot.children) {

                            try {
                                var getData = idata.getValue(User::class.java)
                                if (FirebaseAuth.getInstance().currentUser?.uid.toString().equals(
                                        getData?.uid.toString()
                                    )

                                ) {
                                    btn.visibility = View.GONE
                                }
                                break

                            } catch (e: Exception) {

                            }
                        }


                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                })
        } catch (e: Exception) {

        }


    }

    fun countViews(position: Int) {
        var k = FirebaseAuth.getInstance().currentUser!!.uid

        FirebaseDatabase.getInstance().getReference("Views")
            .child(list[position].CreatorId.toString()).child(list[position].videoId.toString())
            .child(k).setValue(k.toString())





    }
}


