package com.techvanka.memllo.ui

import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.techvanka.memllo.R
import com.techvanka.memllo.adapter.FriendRequestAdapter
import com.techvanka.memllo.databinding.ActivityFriendRequestBinding
import com.techvanka.memllo.model.FriendRequestModel

class FriendRequestActivity : AppCompatActivity() {
    private lateinit var binding:ActivityFriendRequestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFriendRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var title = "Notification"
        supportActionBar?.setBackgroundDrawable( ColorDrawable( ContextCompat.getColor(this, R.color.btn_text_colour)))
        val titleColor = ContextCompat.getColor(this, R.color.background_colour)
        supportActionBar?.title = HtmlCompat.fromHtml("<font color='$titleColor'>${title}</font>", HtmlCompat.FROM_HTML_MODE_LEGACY)
        binding.friendRequestRv.layoutManager = LinearLayoutManager(this)
        getRequests()
    }

    private fun getRequests() {
        var list = arrayListOf<FriendRequestModel>()
        FirebaseDatabase.getInstance().getReference("FriendRequest").child(FirebaseAuth.getInstance().currentUser!!.uid)
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                   for (data in snapshot.children){
                       var getData = data.getValue(FriendRequestModel::class.java)
                       list.add(getData!!)
                   }
                    binding.friendRequestRv.adapter = FriendRequestAdapter(this@FriendRequestActivity,list)
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }
}