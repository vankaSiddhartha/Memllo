package com.techvanka.memllo.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ExoPlayer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.techvanka.memllo.R
import com.techvanka.memllo.databinding.ActivityUploadVideoBinding
import com.techvanka.memllo.fragments.UploadFragment
import com.techvanka.memllo.model.User
import com.techvanka.memllo.model.VideoUploadModel
import java.util.*

class UploadVideoActivity : AppCompatActivity() {
  private lateinit var binding: ActivityUploadVideoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        binding = ActivityUploadVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.uploadPb.visibility = View.GONE
        Glide.with(this).load(intent.getStringExtra("imgLink")).into(binding.uploadImgView)
        binding.UploadBtn.setOnClickListener {
            binding.uploadPb.visibility = View.VISIBLE
            uploadData(intent.getStringExtra("imgLink"))


        }


    }

    private fun uploadData(videoLink: String?) {
        var put = FirebaseStorage.getInstance().reference.child("Videos").child(
            UUID.randomUUID().toString())
        put.putFile(Uri.parse(videoLink)).addOnSuccessListener {

            put.downloadUrl.addOnSuccessListener {vid->

                UploadVideoNode(vid)
            }
        }
    }

    private fun UploadVideoNode(vid: Uri?) {
        var videoId = FirebaseAuth.getInstance().currentUser!!.uid+binding.titleInput.text.toString()
     FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().currentUser!!.uid)
         .addListenerForSingleValueEvent(object : ValueEventListener{
             override fun onDataChange(snapshot: DataSnapshot) {
                 var data = snapshot.getValue(User::class.java)
    var VideoData = VideoUploadModel(binding.titleInput.text.toString(),vid.toString(),videoId,FirebaseAuth.getInstance().currentUser!!.uid,data?.name,data?.profile)
                 FirebaseDatabase.getInstance().getReference("Videos").child(videoId).setValue(VideoData).addOnSuccessListener {
                     binding.uploadPb.visibility = View.GONE

                 }.addOnFailureListener {
                     binding.uploadPb.visibility = View.GONE
                 }
             }

             override fun onCancelled(error: DatabaseError) {
                 TODO("Not yet implemented")
             }

         })
    }

    override fun onStart() {
        super.onStart()
        ExoPlayer.Builder(this).build().release()
    }
}