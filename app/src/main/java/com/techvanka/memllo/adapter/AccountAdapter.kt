package com.techvanka.memllo.adapter

import android.app.UiModeManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.techvanka.memllo.MainActivity
import com.techvanka.memllo.R
import com.techvanka.memllo.databinding.AccountLayoutBinding
import com.techvanka.memllo.model.User
import com.techvanka.memllo.ui.*
import java.util.*
import kotlin.collections.ArrayList

class AccountAdapter(
    var list: ArrayList<Int>,
    var context: Context,
    var textlist: ArrayList<String>,
    accountFragment: MainActivity
):RecyclerView.Adapter<AccountAdapter.ViewHolder>() {




    class ViewHolder(var binding:AccountLayoutBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AccountLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
         var CHANGE_KEY=0
        var i=0
        val uiModeManager = context.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
        val isNightMode = uiModeManager.nightMode == UiModeManager.MODE_NIGHT_YES
     holder.binding.icon.setImageResource(list[position])
        holder.binding.iconText.text = textlist[position]
        holder.binding.iconText.setOnClickListener {

            if (textlist[position] == "My Orders"){
                context.startActivity(Intent(context,ProductTrackActivity::class.java))
            }else if(textlist[position]=="Cart"){
                context.startActivity(Intent(context,CartShowActivity::class.java))
            }else if(textlist[position]=="My Videos"){
                context.startActivity(Intent(context,CreatorDashBoard::class.java))
            }else if(textlist[position]=="Mode"){
                val customColor = ContextCompat.getColor(context, R.color.btn_text_colour)
                Toast.makeText(context, "$isNightMode", Toast.LENGTH_SHORT).show()
                if (holder.binding.iconText.currentTextColor==Color.WHITE) {
                    CHANGE_KEY=1
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                   i++

                }else{
                   CHANGE_KEY=0
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                 i++
                }


            }else if (textlist[position]=="Edit Profile"){
               context.startActivity(Intent(context,EditProfileActivity::class.java))
            }else if (textlist[position]=="Favorite Memers"){
                context.startActivity(Intent(context,FollowingPeopleActivity::class.java))
            }else if(textlist[position]=="My Account"){
                val sharedPref = context.getSharedPreferences("myProfile", Context.MODE_PRIVATE)
                val myProfile = sharedPref.getString("plink", "no")
                val myName = sharedPref.getString("pname","no")
                var intent = Intent(context, CreatorMemesActivity::class.java)
                intent.putExtra("cid",FirebaseAuth.getInstance().currentUser!!.uid)
                if (myName.equals("no")){
                    intent.putExtra("name",FirebaseAuth.getInstance().currentUser!!.displayName)
                }else{
                   intent.putExtra("name",myName)
                }
                if (myProfile.equals("no")){

                    intent.putExtra("profile",FirebaseAuth.getInstance().currentUser!!.photoUrl.toString())
                }else{
                    intent.putExtra("profile",myProfile)
                }


                intent.putExtra("fallow","hide")
                context.startActivity(intent)
            }else if(textlist[position]=="Liked Videos"){
                context.startActivity(Intent(context,LikedVideos::class.java))
            }
        }

    }


    override fun getItemCount(): Int {
        return  list.size
    }
}