package com.techvanka.memllo.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import  com.techvanka.memllo.model.*
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.techvanka.memllo.MainActivity
import com.techvanka.memllo.R
import com.techvanka.memllo.adapter.AccountAdapter
import com.techvanka.memllo.databinding.FragmentAccountBinding

class AccountFragment : Fragment() {
       private lateinit var binding: FragmentAccountBinding
       private lateinit var list:ArrayList<String>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var binding:FragmentAccountBinding= FragmentAccountBinding.inflate(inflater,container,false)
     binding.accountSettingRV.layoutManager = LinearLayoutManager(requireContext())
        var Imagelist = arrayListOf<Int>()
        var TitleList = arrayListOf<String>()
        Imagelist.add(R.drawable.user_account_foreground)
        TitleList.add("My Account")
        Imagelist.add(R.drawable.ic_market_foreground)
        TitleList.add("My Orders")
        Imagelist.add(R.drawable.shopingcart_foreground)
        TitleList.add("Cart")
        Imagelist.add(R.drawable.ic_add_foreground)
        TitleList.add("My Videos")

        Imagelist.add(R.drawable.moneyicon_foreground)
        TitleList.add("My Earnings")
        Imagelist.add(R.drawable.edittext_foreground)
        TitleList.add("Edit Profile")
        Imagelist.add(R.drawable.mymemers_foreground)
        TitleList.add("Favorite Memers")
        Imagelist.add(R.drawable.likedimg_foreground)
        TitleList.add("Liked Videos")
        Imagelist.add(R.drawable.mode_foreground)
        TitleList.add("Mode")

        binding.accountSettingRV.adapter = AccountAdapter(Imagelist,requireContext(),TitleList,MainActivity())
        val sharedPref = requireContext().getSharedPreferences("myProfile", Context.MODE_PRIVATE)
        val myProfile = sharedPref.getString("plink", "no")
        val myName = sharedPref.getString("pname","no")
        if (myName.equals("no")){
           FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().currentUser!!.uid)
               .addListenerForSingleValueEvent(object :ValueEventListener{
                   override fun onDataChange(snapshot: DataSnapshot) {
                       var data = snapshot.getValue(User::class.java)
                       binding.userTitle.setText(data!!.name)
                   }

                   override fun onCancelled(error: DatabaseError) {

                   }

               })
        }else{
            binding.userTitle.setText(myName)
        }
        if (myProfile.equals("no")) {
            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().currentUser!!.uid)
                .addListenerForSingleValueEvent(object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var data = snapshot.getValue(User::class.java)
                        Glide.with(requireContext()).load(data!!.profile)
                            .into(binding.changeProfile)
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                })

        }else{
            Glide.with(requireContext()).load(myProfile).into(binding.changeProfile)
        }

        binding.userEmail.setText(FirebaseAuth.getInstance().currentUser!!.email)

        return binding.root
    }



}