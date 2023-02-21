package com.techvanka.memllo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.techvanka.memllo.R
import com.techvanka.memllo.adapter.CommentsAdapter
import com.techvanka.memllo.databinding.FragmentCommentsABinding


class CommentsFragmentA : BottomSheetDialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentCommentsABinding = FragmentCommentsABinding.inflate(inflater,container,false)

        binding.commentRv.layoutManager = LinearLayoutManager(requireContext())
        var list = arrayListOf<String>()

        list.add("yghk")
        list.add("yak")
        list.add("yghk")
        list.add("yghk")

        binding.commentsEt.setOnClickListener {
            //Toast.makeText(requireContext(), "lol", Toast.LENGTH_SHORT).show()
        }
        binding.commentsSendbtn.setOnClickListener {
            Toast.makeText(requireContext(), "jjjj", Toast.LENGTH_SHORT).show()
        }

        return inflater.inflate(R.layout.fragment_comments_a, container, false)
    }

}