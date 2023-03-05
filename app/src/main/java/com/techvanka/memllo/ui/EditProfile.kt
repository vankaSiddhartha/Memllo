package com.techvanka.memllo.ui

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.techvanka.memllo.databinding.ActivityEditProfileBinding
import com.techvanka.memllo.model.User
import java.util.*
import kotlin.collections.ArrayList

class EditProfile : AppCompatActivity() {
    private lateinit var binding:ActivityEditProfileBinding
    private lateinit var img:Uri
    private lateinit var list:ArrayList<String>
    private lateinit var getImg:String
    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        list = arrayListOf()
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.editPbLoading.visibility = View.GONE
        binding.loadUpdate.visibility = View.GONE
        getInfo()
      //  binding.userNameEt.setText(intent.getStringExtra("name"))
        //Glide.with(this).load(intent.getStringExtra("profile")).into(binding.userProfileIv)

        binding.uploadInfo.setOnClickListener {
            binding.loadUpdate.visibility = View.VISIBLE
            if (list.isEmpty()){
                uploadNewData(binding.userNameEt)
            }else{

            }

        }
        binding.userProfileIv.setOnClickListener {
            binding.editPbLoading.visibility = View.VISIBLE
            readExternalPermissionContract.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        getInfo()

    }

    private fun getInfo() {
        FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().currentUser!!.uid).addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var data = snapshot.getValue(User::class.java)

                Glide.with(this@EditProfile).load(data!!.profile).into(binding.userProfileIv)
                binding.userNameEt.setText(data!!.name )
                binding.loadUpdate.visibility = View.GONE

            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun uploadNewData(userText: EditText) {
        FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().currentUser!!.uid).addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var data = snapshot.getValue(User::class.java)
                FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(User(userText.text.toString(),data!!.uid,data.profile)).addOnSuccessListener {
                    binding.loadUpdate.visibility = View.GONE
                }.addOnFailureListener {
                    binding.loadUpdate.visibility = View.GONE
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }


    private fun uploadNewDataImg(img: String) {

        FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().currentUser!!.uid).addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var data = snapshot.getValue(User::class.java)
                FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(User(data!!.name,
                    data.uid,img)).addOnSuccessListener {
                    binding.loadUpdate.visibility = View.GONE
                }.addOnFailureListener {
                    binding.loadUpdate.visibility = View.GONE
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })

    }

    private val readExternalPermissionContract =  registerForActivityResult(ActivityResultContracts.RequestPermission()) { isPermissionAccepted ->
        if(isPermissionAccepted) {
            getVideo.launch("image/*")
           // selectImg = true
        } else {
            Toast.makeText(this@EditProfile, "Permission is declined", Toast.LENGTH_SHORT).show()

        }
    }
    private var getVideo = registerForActivityResult(ActivityResultContracts.GetContent()){ img->

        var put = FirebaseStorage.getInstance().reference.child("Profiles").child(
            UUID.randomUUID().toString())
        put.putFile(img!!).addOnSuccessListener {

            put.downloadUrl.addOnSuccessListener {link->
                getImg = link.toString()
                uploadNewDataImg(getImg)
               list.add(link.toString())
                Glide.with(this).load(link.toString()).listener(object :
                    RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {

                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                     binding.editPbLoading.visibility = View.GONE
                        return false
                    }
                }).into(binding.userProfileIv)

            }
        }

    }

}
