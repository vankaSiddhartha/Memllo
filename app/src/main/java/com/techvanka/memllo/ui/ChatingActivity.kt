package com.techvanka.memllo.ui

import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
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
import com.techvanka.memllo.adapter.ChatAdapter
import com.techvanka.memllo.databinding.ActivityChatingBinding
import com.techvanka.memllo.model.MessageModel
import com.techvanka.memllo.model.User
import com.techvanka.memllo.notificationApi.ApiUtils
import com.techvanka.memllo.notificationApi.NotificationData
import com.techvanka.memllo.notificationApi.PushNotification
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList

class ChatingActivity : AppCompatActivity() {
    private lateinit var binding:ActivityChatingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setBackgroundDrawable( ColorDrawable( ContextCompat.getColor(this, R.color.btn_text_colour)))
        val titleColor = ContextCompat.getColor(this, R.color.background_colour)
        supportActionBar?.title = HtmlCompat.fromHtml("<font color='$titleColor'>${intent.getStringExtra("name")}</font>", HtmlCompat.FROM_HTML_MODE_LEGACY)
       binding.rvM.layoutManager = LinearLayoutManager(this)
        getData(FirebaseAuth.getInstance().currentUser!!.uid+intent.getStringExtra("rid"))

        binding.sendbtn.setOnClickListener {

                UploadMessage(intent.getStringExtra("rid"),FirebaseAuth.getInstance().currentUser!!.uid,binding.messege,intent.getStringExtra("name")
                )

       }





    }
    private fun UploadMessage(rid: String?, sid: String, messege: EditText?, name: String?) {

        var mess = messege!!.text.toString()

        var data = MessageModel(sid,mess)
        var roomid = sid+rid
        var revRoom = rid+sid



        FirebaseDatabase.getInstance().reference.child("Chats").child(roomid).push().setValue(data).addOnSuccessListener {

            FirebaseDatabase.getInstance().reference.child("Chats").child(revRoom).push().setValue(data).addOnSuccessListener {
                Toast.makeText(this, "Message sent!!", Toast.LENGTH_SHORT).show()
                sendNotification(mess,rid!!,name)
                binding.messege.setText("")

            }
        }


    }

    private fun sendNotification(mess: String, rid: String, name: String?) {
        var Ndata = NotificationData(name,mess)
        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().currentUser!!.uid).addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var data = snapshot.getValue(User::class.java)
                var sendData = PushNotification(Ndata,data?.fcmToken)
                ApiUtils.getInstance().sendNotification(
                    sendData
                ).enqueue(object : Callback<PushNotification>{
                    override fun onResponse(
                        call: Call<PushNotification>,
                        response: Response<PushNotification>
                    ) {
                        Toast.makeText(this@ChatingActivity, "${sendData}", Toast.LENGTH_SHORT).show()
                    }

                    override fun onFailure(call: Call<PushNotification>, t: Throwable) {
                        Toast.makeText(this@ChatingActivity, "$sendData", Toast.LENGTH_SHORT).show()
                    }

                })
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

    private fun getData(ROOMID: String) {
        var list:ArrayList<MessageModel> = arrayListOf()
        FirebaseDatabase.getInstance().getReference("Chats").child(ROOMID)
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    list = ArrayList<MessageModel>()

                    for (data in snapshot.children){
                        if(snapshot.exists()){

                            var getInfo = data.getValue(MessageModel::class.java)

                            list.add(getInfo!!)


                        }

                    }

                  binding.rvM.adapter = ChatAdapter(this@ChatingActivity,list)
                 //   binding.rvM.smoothScrollToPosition(list.size-1)



                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }

}