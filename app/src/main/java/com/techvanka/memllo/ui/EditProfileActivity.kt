package com.techvanka.memllo.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.techvanka.memllo.MainActivity
import com.techvanka.memllo.databinding.ActivityEditProfile2Binding
import com.techvanka.memllo.model.User
import java.util.*

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding:ActivityEditProfile2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
       supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfile2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imageLoding.visibility = View.GONE
        val sharedPref = getSharedPreferences("myProfile", Context.MODE_PRIVATE)
        val myProfile = sharedPref.getString("plink", "no")
        val myName = sharedPref.getString("pname","no")
        if (myProfile.equals("no")){
            Glide.with(this).load(FirebaseAuth.getInstance().currentUser!!.photoUrl).into(binding.userPic)
        }else{
            Glide.with(this).load(myProfile).into(binding.userPic)
        }
        if (myName.equals("no")){
            binding.userNameEditEt.setText(FirebaseAuth.getInstance().currentUser!!.displayName.toString())
        }else{
            binding.userNameEditEt.setText(myName)
        }

        binding.picCard.setOnClickListener {
            binding.imageLoding.visibility = View.VISIBLE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                readExternalPermissionContract.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
            }else{
                readExternalPermissionContract.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
        binding.userPic.setOnClickListener {
            binding.imageLoding.visibility = View.VISIBLE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                readExternalPermissionContract.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
            }else{
                readExternalPermissionContract.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
        binding.okBtn.setOnClickListener {
            if (binding.imageLoding.visibility == View.VISIBLE){
                Toast.makeText(this, "Profile is uploading please wait!!", Toast.LENGTH_SHORT).show()
            }else{
                if (binding.changeImgTv.text.toString().isNotEmpty()){
                    changeName(binding.userNameEditEt.text.toString())
                }else{
                    startActivity(Intent(this@EditProfileActivity,MainActivity::class.java))
                }
            }
        }
        binding.noBtn.setOnClickListener {
            startActivity(Intent(this@EditProfileActivity,MainActivity::class.java))
        }

    }
    private val readExternalPermissionContract = registerForActivityResult(
        ActivityResultContracts.RequestPermission()) { isPermissionAccepted ->
        if(isPermissionAccepted) {
            getVideo.launch("image/*")
        } else {
            Toast.makeText(this, "Permission is declined", Toast.LENGTH_SHORT).show()

        }
    }
    private var getVideo = registerForActivityResult(ActivityResultContracts.GetContent()){


        IntentToUpload(it)

    }

    private fun IntentToUpload(it: Uri?) {
        var put = FirebaseStorage.getInstance().reference.child("changedProfile").child(
            UUID.randomUUID().toString()
        )
        put.putFile(it!!).addOnSuccessListener {

            put.downloadUrl.addOnSuccessListener { vid ->

                Firebase.database.getReference("Users").child(FirebaseAuth.getInstance().currentUser!!.uid)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            var data = snapshot.getValue(User::class.java)
                          FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().currentUser!!.uid)
                              .setValue(User(data!!.name,data.uid,vid.toString(),data.list,data.fcmToken)).addOnSuccessListener {
                                  val sharedPref = getSharedPreferences("myProfile", Context.MODE_PRIVATE)
                                  val editor = sharedPref.edit()
                                  editor.putString("plink",vid.toString())
                                  editor.apply()
                                  binding.imageLoding.visibility = View.GONE
                                  Glide.with(this@EditProfileActivity).load(vid).into(binding.userPic)
                              }

                        }

                        override fun onCancelled(error: DatabaseError) {

                        }

                    })
            }
        }
    }
    fun changeName(name: String) {
        Firebase.database.getReference("Users").child(FirebaseAuth.getInstance().currentUser!!.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var data = snapshot.getValue(User::class.java)
                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().currentUser!!.uid)
                        .setValue(User(name,data!!.uid,data.profile.toString(),data.list,data.fcmToken)).addOnSuccessListener {
                            binding.userNameEditEt.setText(name)
                            val sharedPref = getSharedPreferences("myProfile", Context.MODE_PRIVATE)
                            val editor = sharedPref.edit()
                            editor.putString("pname",name)
                            editor.apply()
                            startActivity(Intent(this@EditProfileActivity,MainActivity::class.java))
                        }

                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }
}