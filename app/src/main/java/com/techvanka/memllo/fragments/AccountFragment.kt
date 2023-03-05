package com.techvanka.memllo.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.techvanka.memllo.R
import com.techvanka.memllo.adapter.AccountAdapter
import com.techvanka.memllo.databinding.FragmentAccountBinding
import com.techvanka.memllo.model.User
import com.techvanka.memllo.ui.CreatorDashBoard
import com.techvanka.memllo.ui.EditProfile

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
        Imagelist.add(R.drawable.ic_market_foreground)
        TitleList.add("My Orders")
        Imagelist.add(R.drawable.shopingcart_foreground)
        TitleList.add("Cart")
        Imagelist.add(R.drawable.moneyicon_foreground)
        TitleList.add("My Earnings")
        Imagelist.add(R.drawable.user_account_foreground)
        TitleList.add("Change Profile")
        Imagelist.add(R.drawable.edittext_foreground)
        TitleList.add("Change Name")
        Imagelist.add(R.drawable.mymemers_foreground)
        TitleList.add("Favorite Memers")
        Imagelist.add(R.drawable.likedimg_foreground)
        TitleList.add("Liked Videos")
        Imagelist.add(R.drawable.ic_add_foreground)
        TitleList.add("My Videos")
        Imagelist.add(R.drawable.mode_foreground)
        TitleList.add("Mode")

        binding.accountSettingRV.adapter = AccountAdapter(Imagelist,requireContext(),TitleList)
        Glide.with(requireContext()).load(FirebaseAuth.getInstance().currentUser!!.photoUrl).into(binding.changeProfile)
        binding.userTitle.setText(FirebaseAuth.getInstance().currentUser!!.displayName)
        binding.userEmail.setText(FirebaseAuth.getInstance().currentUser!!.email)

        return binding.root
    }



}