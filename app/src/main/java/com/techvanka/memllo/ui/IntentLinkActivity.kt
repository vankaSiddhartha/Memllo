package com.techvanka.memllo.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.techvanka.memllo.R
import com.techvanka.memllo.model.VideoUploadModel

class IntentLinkActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intent_link)
        var IntentId = "https://memllo.page.link?apn=com.techvanka.memllo&ibi=com.example.ios&link="
        FirebaseDatabase.getInstance().getReference("Videos").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var data = snapshot.children
                for (i in data){
                    var getData = i.getValue(VideoUploadModel::class.java)
                   if (intent.getStringExtra("id").equals(IntentId+getData?.videoId)){
                       Toast.makeText(this@IntentLinkActivity, "lol", Toast.LENGTH_SHORT).show()
                   }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}