package com.techvanka.memllo.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.techvanka.memllo.R
import com.techvanka.memllo.databinding.FragmentAccountBinding
import com.techvanka.memllo.model.User
import com.techvanka.memllo.ui.EditProfile

class AccountFragment : Fragment() {
       private lateinit var binding: FragmentAccountBinding
       private lateinit var list:ArrayList<String>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var binding:FragmentAccountBinding= FragmentAccountBinding.inflate(inflater,container,false)
        list = arrayListOf()
        binding.accountEditBtn.setOnClickListener {


            startActivity(Intent(requireContext(),EditProfile::class.java))
        }

        FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().currentUser!!.uid).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var data = snapshot.getValue(User::class.java)

              binding.accountUsername.text = data!!.name
                Glide.with(requireContext()).load(data.profile).into(binding.accountIv)
                data.name?.let { list.add(it) }
                data.profile?.let { list.add(it) }

            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        return binding.root
    }



}