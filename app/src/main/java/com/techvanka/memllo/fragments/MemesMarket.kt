package com.techvanka.memllo.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.techvanka.memllo.R
import com.techvanka.memllo.adapter.ShopingAdapter
import com.techvanka.memllo.databinding.FragmentMemesBinding
import com.techvanka.memllo.databinding.FragmentMemesMarketBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MemesMarket.newInstance] factory method to
 * create an instance of this fragment.
 */
class MemesMarket : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentMemesMarketBinding = FragmentMemesMarketBinding.inflate(inflater, container, false)
        binding.shopRv.layoutManager = GridLayoutManager(requireContext(),2)
        var list = arrayListOf<String>()
        list.add("The boys T-shirt")
        list.add("Women hoodie")
        list.add("The boys T-shirt")
        list.add("Women hoodie")
        list.add("The boys T-shirt")
        list.add("Women hoodie")
        binding.shopRv.adapter= ShopingAdapter(requireContext(),list)
        return binding.root
    }


}