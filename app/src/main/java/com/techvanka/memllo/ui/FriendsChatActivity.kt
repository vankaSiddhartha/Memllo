package com.techvanka.memllo.ui

import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.techvanka.memllo.R
import com.techvanka.memllo.adapter.FriendsChatsAdapter
import com.techvanka.memllo.databinding.ActivityFriendsChatBinding
import com.techvanka.memllo.model.FriendRequestModel

class FriendsChatActivity : AppCompatActivity() {
    private lateinit var binding:ActivityFriendsChatBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        var title = "My Chats"
        supportActionBar?.setBackgroundDrawable( ColorDrawable( ContextCompat.getColor(this, R.color.btn_text_colour)))
        val titleColor = ContextCompat.getColor(this, R.color.background_colour)
        supportActionBar?.title = HtmlCompat.fromHtml("<font color='$titleColor'>${title}</font>", HtmlCompat.FROM_HTML_MODE_LEGACY)
        super.onCreate(savedInstanceState)
        binding = ActivityFriendsChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.friendsList.layoutManager = LinearLayoutManager(this)
        getFriends()
    }

    private fun getFriends() {
        var list = arrayListOf<FriendRequestModel>()
        FirebaseDatabase.getInstance().getReference("FriendRequest").child(FirebaseAuth.getInstance().currentUser!!.uid)
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(data in snapshot.children){
                        var getData = data.getValue(FriendRequestModel::class.java)
                        if (getData?.isFriend.equals("true")) {
                            list.add(getData!!)
                        }
                    }
                    binding.friendsList.adapter = FriendsChatsAdapter(this@FriendsChatActivity,list)

                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }
}