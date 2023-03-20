package com.techvanka.memllo.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.techvanka.memllo.MainActivity
import com.techvanka.memllo.R
import com.techvanka.memllo.adapter.VideoAdapter
import com.techvanka.memllo.databinding.ActivityShareVideoViewBinding
import com.techvanka.memllo.model.VideoUploadModel
import org.json.JSONArray

class  ShareVideoView : AppCompatActivity() {
    private lateinit var binding:ActivityShareVideoViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShareVideoViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var list = arrayListOf<Int>()
        list.add(R.layout.fragment_comments_a)
        list.add(R.id.comment_rv)
        list.add(R.id.comments_et)
        list.add(R.id.comments_sendbtn)
        var IntentId = "https://memllo.page.link?apn=com.techvanka.memllo&ibi=com.example.ios&link="
        var comments_count=0
        val prefs: SharedPreferences =
            this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val json = prefs.getString("list", null)
        var getingVideoId = intent.getStringExtra("id")
         getingVideoId = getingVideoId?.replace(IntentId,"")
        FirebaseDatabase.getInstance().getReference("Videos")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    var list1 = ArrayList<VideoUploadModel>()
                    for (snap in snapshot.children) {
                        var data = snap.getValue(VideoUploadModel::class.java)


                        if (json != null) {
                            val jsonArray = JSONArray(json)
                            val list4 = ArrayList<String>()
                            for (i in 0 until jsonArray.length()) {
                                val item = jsonArray.getString(i)
                                list4.add(item)
                            }

                            if (data?.list!!.containsAll(list4) || list4.containsAll(data.list) || data.list.contains(
                                    "eng"
                                )
                            ) {
                                list1.add(data)
                            } else {

                                list1.add(data)
                            }
                        }


                    }

                    list1.shuffle()
                    var vidPostion =    list1.indexOf(list1.find { it.videoId!!.contains(getingVideoId.toString()) })
                    list1[0] = list1[vidPostion]
                    list1[vidPostion] = list1[0]
                    try {
                        binding.memesViewer.adapter =
                            VideoAdapter(this@ShareVideoView, list1, list)


                    } catch (e: Exception) {

                    }


                }


                override fun onCancelled(error: DatabaseError) {

                }

            })

    }


    override fun onPause() {
        super.onPause()
        startActivity(Intent(this,MainActivity::class.java))
    }
}