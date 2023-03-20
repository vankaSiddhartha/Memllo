package com.techvanka.memllo.ui

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.techvanka.memllo.R
import com.techvanka.memllo.adapter.GridVideoShowAdapter
import com.techvanka.memllo.databinding.ActivityCreatorMemesBinding
import com.techvanka.memllo.model.User
import com.techvanka.memllo.model.VideoUploadModel

class CreatorMemesActivity : AppCompatActivity() {
    private lateinit var binding:ActivityCreatorMemesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        binding = ActivityCreatorMemesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.memesRv.layoutManager = GridLayoutManager(this,3)
        binding.fallowersCount.setOnClickListener {
            if (intent.getStringExtra("cid").equals(FirebaseAuth.getInstance().currentUser!!.uid)){
                var intent = Intent(this,FollowingPeopleActivity::class.java)
                intent.putExtra("code","1")
                startActivity(intent)
            }
        }

        var  btnClick = intent.getStringExtra("fallow").equals("true")
        binding.followBtn.setOnClickListener {

             if (btnClick){
                 var value =  FirebaseDatabase.getInstance().getReference("Fallow")
                     .child(intent.getStringExtra("cid").toString())
                 binding.followBtn.setBackgroundResource(R.drawable.fallowbtnback)
                 btnClick = false

                // Change the button text
                binding.followBtn.text = "Follow"
                value.child(FirebaseAuth.getInstance().currentUser!!.uid).removeValue()
                 countFallowers(intent.getStringExtra("cid"))
            }else{

                FirebaseDatabase.getInstance().getReference("Fallow")
                    .child(intent.getStringExtra("cid").toString()).child(FirebaseAuth.getInstance().currentUser!!.uid)
                    .setValue(
                        User(
                            FirebaseAuth.getInstance().currentUser!!.displayName.toString(),
                            FirebaseAuth.getInstance().currentUser!!.uid,
                            FirebaseAuth.getInstance().currentUser!!.photoUrl.toString())
                    ).addOnSuccessListener {
                        binding.followBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_200))

                        // Change the button text
                        binding.followBtn.text = "Following"
                        btnClick = true
                        countFallowers(intent.getStringExtra("cid"))
                    }
            }
            }

        countFallowers(intent.getStringExtra("cid"))
        Glide.with(this).load(intent.getStringExtra("profile")).into(binding.changeProfile)
        binding.userTitle.text = intent.getStringExtra("name")
        if (intent.getStringExtra("fallow").equals("true")){

            binding.followBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.followedBtn))

            // Change the button text
            binding.followBtn.text = "Following"
        }
        getCreatorData(intent.getStringExtra("cid"))
    }

    private fun getCreatorData(cid:String?) {
        var countVideos =0
        var list = arrayListOf<VideoUploadModel>()
        FirebaseDatabase.getInstance().getReference("MyVideo").child(cid.toString()).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children){
                    var getData = data.getValue(VideoUploadModel::class.java)
                    list.add(getData!!)
                    countVideos++
                }
                binding.videosCount.text = "$countVideos videos"
                binding.memesRv.adapter = GridVideoShowAdapter(this@CreatorMemesActivity,list)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }
    private fun countFallowers(cid:String?){
        var fallowCount =0
        FirebaseDatabase.getInstance().getReference("Fallow").child(cid.toString()).addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(data in snapshot.children){
                 fallowCount++
                }
                binding.fallowersCount.text = "$fallowCount fallowers"
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}


