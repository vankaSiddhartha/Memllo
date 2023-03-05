package com.techvanka.memllo.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.techvanka.memllo.adapter.SearchAdapter
import com.techvanka.memllo.adapter.SwipingAdapter
import com.techvanka.memllo.databinding.FragmentInboxBinding
import com.techvanka.memllo.databinding.SearchDailogViewBinding
import com.techvanka.memllo.model.User
import com.techvanka.memllo.ui.FriendRequestActivity
import com.techvanka.memllo.ui.FriendsChatActivity


class InboxFragment : Fragment() {
  private lateinit var binding: FragmentInboxBinding
  private lateinit var userList:ArrayList<User>



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userList = arrayListOf()
        binding = FragmentInboxBinding.inflate(inflater, container, false)
      getUsers()
        binding.searchBtn.setOnClickListener {
            showSearchDailog()
        }
        binding.searchBtn2.setOnClickListener {
            startActivity(Intent(requireContext(), FriendRequestActivity::class.java))

        }
        binding.myChatBtn.setOnClickListener {
            startActivity(Intent(requireContext(),FriendsChatActivity::class.java))
        }



        return binding.root
    }

    private fun showSearchDailog() {

        val builder = AlertDialog.Builder(requireContext())
        var bindingVIEW = SearchDailogViewBinding.inflate(layoutInflater)
        builder.setView(bindingVIEW.root)
        bindingVIEW.searchUserRv.layoutManager = LinearLayoutManager(requireContext())
        bindingVIEW.search.setOnClickListener {
            getDailogUserData(bindingVIEW.searchUserRv,bindingVIEW.searchEt)
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun getDailogUserData(searchUserRv: RecyclerView, searchEt: EditText) {
        userList.shuffle()
        searchUserRv.adapter = SearchAdapter(requireContext(),userList,searchEt.text.toString())

    }

    private fun getUsers() {
        var list:ArrayList<User> = arrayListOf()
        FirebaseDatabase.getInstance().getReference("Users").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children){
                    var getData=data.getValue(User::class.java)
                    list.add(getData!!)
                }
                try {

                    binding.UserViewPager.adapter = SwipingAdapter(requireContext(), list)
                }catch (e:Exception){

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
        userList = list

    }


}