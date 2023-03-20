package com.techvanka.memllo.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.techvanka.memllo.R
import com.techvanka.memllo.adapter.VideoAdapter
import com.techvanka.memllo.databinding.ActivityGridVideoPlayerBinding
import com.techvanka.memllo.model.VideoUploadModel
import org.json.JSONArray
import java.io.Serializable

class GridVideoPlayer : AppCompatActivity() {
    private lateinit var binding: ActivityGridVideoPlayerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityGridVideoPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val prefs: SharedPreferences =
            this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val json = prefs.getString("list", null)
        var list = arrayListOf<Int>()
        list.add(R.layout.fragment_comments_a)
        list.add(R.id.comment_rv)
        list.add(R.id.comments_et)
        list.add(R.id.comments_sendbtn)
        var list1 = arrayListOf<VideoUploadModel>()
        FirebaseDatabase.getInstance().getReference("MyVideo").child(intent.getStringExtra("cid").toString())
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



                     var temp = list1[0]
                    list1[0] = list1[intent.getStringExtra("pos")!!.toInt()]
                    list1[intent.getStringExtra("pos")!!.toInt()] = temp

                    try {
                        binding.memesVideosGrid.adapter =
                            VideoAdapter(this@GridVideoPlayer, list1, list)


                    } catch (e: Exception) {

                    }


                }


                override fun onCancelled(error: DatabaseError) {

                }

            })


    }
}