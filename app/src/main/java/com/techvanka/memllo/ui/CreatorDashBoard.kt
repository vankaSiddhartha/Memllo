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
import com.techvanka.memllo.R
import com.techvanka.memllo.adapter.DashboardAdapter
import com.techvanka.memllo.databinding.ActivityCreatorDashBoardBinding
import com.techvanka.memllo.model.VideoUploadModel

class CreatorDashBoard : AppCompatActivity() {
    var list = arrayListOf<VideoUploadModel>()
    private lateinit var binding:ActivityCreatorDashBoardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //supportActionBar?.hide()



         supportActionBar?.setBackgroundDrawable( ColorDrawable( ContextCompat.getColor(this, R.color.btn_text_colour)))
        val titleColor = ContextCompat.getColor(this, R.color.background_colour)
       supportActionBar?.title = HtmlCompat.fromHtml("<font color='$titleColor'>${title}</font>", HtmlCompat.FROM_HTML_MODE_LEGACY)


        binding = ActivityCreatorDashBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.dashnoardRv.layoutManager = LinearLayoutManager(this)
        FirebaseDatabase.getInstance().getReference("MyVideo").child(FirebaseAuth.getInstance().currentUser!!.uid).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var allData = snapshot.children
                for(data in allData) {
                   var getValue = data.getValue(VideoUploadModel::class.java)
                    ///Toast.makeText(this@CreatorDashBoard, "${data}", Toast.LENGTH_SHORT).show()
                    list.add(getValue!!)

                }
               binding.dashnoardRv.adapter = DashboardAdapter(this@CreatorDashBoard, list)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@CreatorDashBoard, "Error!!", Toast.LENGTH_SHORT).show()
            }

        })


    }
}