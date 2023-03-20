package com.techvanka.memllo.ui

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.techvanka.memllo.R
import com.techvanka.memllo.databinding.ActivityUploadVideoBinding
import com.techvanka.memllo.databinding.LayoutLanguageBinding
import com.techvanka.memllo.model.VideoUploadModel
import java.util.*
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationCompat
import com.techvanka.memllo.MainActivity

class UploadVideoActivity : AppCompatActivity() {
    private var CODEENG=0
    private var CODETEL =0
    private var CODEHIN=0
    private var CODETAM =0
    private var CODEBANGLA =0
    private var CODEMARATHI=0
    private var list:ArrayList<String> = arrayListOf()
    private lateinit var binding: ActivityUploadVideoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        binding = ActivityUploadVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.uploadPb.visibility = View.GONE
        val context = this

        Glide.with(this).load(intent.getStringExtra("imgLink")).into(binding.uploadImgView)
       binding.languageSetter.setOnClickListener {
           Toast.makeText(this, "lol", Toast.LENGTH_SHORT).show()
           showDailog()
       }


    }

    private fun uploadData(videoLink: String?, list: ArrayList<String>) {
        var put = FirebaseStorage.getInstance().reference.child("Videos").child(
            UUID.randomUUID().toString()
        )
        put.putFile(Uri.parse(videoLink)).addOnSuccessListener {

            put.downloadUrl.addOnSuccessListener { vid ->

                UploadVideoNode(vid,list)
            }
        }
    }

    private fun UploadVideoNode(vid: Uri?, list: ArrayList<String>) {

        var videoId =
            FirebaseAuth.getInstance().currentUser!!.uid + binding.titleInput.text.toString()


        var VideoData = VideoUploadModel(
            binding.titleInput.text.toString(),
            vid.toString(),
            videoId,
            FirebaseAuth.getInstance().currentUser!!.uid,
            FirebaseAuth.getInstance().currentUser!!.displayName,
            FirebaseAuth.getInstance().currentUser!!.photoUrl.toString(),
            list

        )
        FirebaseDatabase.getInstance().getReference("Videos").child(videoId).setValue(VideoData)
            .addOnSuccessListener {
                FirebaseDatabase.getInstance().getReference("MyVideo").child(FirebaseAuth.getInstance().currentUser!!.uid).push().setValue(VideoData).addOnSuccessListener {
                    binding.uploadPb.visibility = View.GONE
                    startActivity(Intent(this@UploadVideoActivity,MainActivity::class.java))
                }


            }.addOnFailureListener {
            binding.uploadPb.visibility = View.GONE
        }


    }
    private fun showDailog() {
        val builder = AlertDialog.Builder(this@UploadVideoActivity)
        var bindingVIEW = LayoutLanguageBinding.inflate(layoutInflater)
        builder.setView(bindingVIEW.root)
        val dialog = builder.create()
        dialog.show()
        bindingVIEW.teluguCard.setOnClickListener {

            if (CODETEL%2==0){
                list.add("tel")
                bindingVIEW.teluguCard.setCardBackgroundColor(
                    ContextCompat.getColor(applicationContext,
                    R.color.teal_200))
            }else{
                list.remove("tel")
                bindingVIEW.teluguCard.setCardBackgroundColor(
                    ContextCompat.getColor(applicationContext,
                        R.color.background_colour
                    ))

            }
            CODETEL++

        }
        bindingVIEW.HindiCard.setOnClickListener {
            if (CODEHIN%2==0){
                list.add("hin")
                bindingVIEW.HindiCard.setCardBackgroundColor(
                    ContextCompat.getColor(applicationContext,
                    R.color.teal_200))
            }else{
                list.remove("hin")
                bindingVIEW.HindiCard.setCardBackgroundColor(
                    ContextCompat.getColor(applicationContext,
                        R.color.background_colour
                    ))


            }
            CODEHIN++




        }

        bindingVIEW.tamilCard.setOnClickListener {
            if (CODETAM%2==0){
                list.add("tamil")
                bindingVIEW.tamilCard.setCardBackgroundColor(
                    ContextCompat.getColor(applicationContext,
                    R.color.teal_200))
            }else{
                list.remove("tamil")
                bindingVIEW.tamilCard.setCardBackgroundColor(
                    ContextCompat.getColor(applicationContext,
                        R.color.background_colour
                    ))


            }
            CODETAM++




        }
        bindingVIEW.EnglishCard.setOnClickListener {
            if (CODEENG%2==0){
                list.add("eng")
                bindingVIEW.EnglishCard.setCardBackgroundColor(
                    ContextCompat.getColor(applicationContext,
                    R.color.teal_200))
            }else{
                list.remove("eng")
                bindingVIEW.EnglishCard.setCardBackgroundColor(
                    ContextCompat.getColor(applicationContext,
                        R.color.background_colour
                    ))


            }
            CODEENG++


        }
        bindingVIEW.bengaliCard.setOnClickListener {
            if (CODEBANGLA%2==0){
                list.add("ben")
                bindingVIEW.bengaliCard.setCardBackgroundColor(
                    ContextCompat.getColor(applicationContext,
                        R.color.teal_200
                    ))

            }else{
                list.remove("ben")
                bindingVIEW.bengaliCard.setCardBackgroundColor(
                    ContextCompat.getColor(applicationContext,
                        R.color.background_colour
                    ))


            }
            CODEBANGLA++

        }
        bindingVIEW.MarathiCard.setOnClickListener {
            if (CODEMARATHI%2==0){
                list.add("mar")
                bindingVIEW.MarathiCard.setCardBackgroundColor(
                    ContextCompat.getColor(applicationContext,
                    R.color.teal_200))
            }else{
                list.remove("mar")
                bindingVIEW.MarathiCard.setCardBackgroundColor(
                    ContextCompat.getColor(applicationContext,
                    R.color.background_colour
                ))

            }
            CODEMARATHI++

        }
        bindingVIEW.continueBtn.setOnClickListener {
            binding.uploadPb.visibility = View.VISIBLE
            uploadData(intent.getStringExtra("imgLink"),list)
            dialog.hide()
        }


    }



}