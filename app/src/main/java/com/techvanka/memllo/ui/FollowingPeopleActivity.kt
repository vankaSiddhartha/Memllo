package com.techvanka.memllo.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.techvanka.memllo.R
import com.techvanka.memllo.adapter.FallowingPeopleAdapter
import com.techvanka.memllo.databinding.ActivityFollowingPeopleBinding
import com.techvanka.memllo.model.User

class FollowingPeopleActivity : AppCompatActivity() {
    private lateinit var binding:ActivityFollowingPeopleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityFollowingPeopleBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(binding.root)
        binding.followPeopleList.layoutManager =  LinearLayoutManager(this)
        if (intent.getStringExtra("code").equals("1")){
            showFollowers()
        }else {
            showData()
        }
    }

    private fun showFollowers() {
        var list = arrayListOf<User>()
        FirebaseDatabase.getInstance().getReference("Fallow").child(FirebaseAuth.getInstance().currentUser!!.uid)
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (data in snapshot.children){
                        var getData = data.getValue(User::class.java)
                        list.add(getData!!)
                    }
                    binding.followPeopleList.adapter = FallowingPeopleAdapter(this@FollowingPeopleActivity,list
                    )
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }

    private fun showData() {
        var list = arrayListOf<User>()
        FirebaseDatabase.getInstance().getReference("IFallow").child(FirebaseAuth.getInstance().currentUser!!.uid)
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (data in snapshot.children){
                        var getData = data.getValue(User::class.java)
                        list.add(getData!!)
                    }
                    binding.followPeopleList.adapter = FallowingPeopleAdapter(this@FollowingPeopleActivity,list
                    )
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }
}