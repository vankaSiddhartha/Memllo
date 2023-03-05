package com.techvanka.memllo.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.techvanka.memllo.R
import com.techvanka.memllo.adapter.ShopingAdapter
import com.techvanka.memllo.databinding.FragmentMemesBinding
import com.techvanka.memllo.databinding.FragmentMemesMarketBinding
import com.techvanka.memllo.model.ShopingModel

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
         var list = arrayListOf<ShopingModel>()
        val binding: FragmentMemesMarketBinding = FragmentMemesMarketBinding.inflate(inflater, container, false)

        val db = Firebase.firestore

// Get a reference to the collection
        val collectionRef = db.collection("ShopList")

// Query the collection for all documents
        collectionRef.get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    list.add(document.toObject(ShopingModel::class.java))
                }
                try {
                    binding.shopRv.layoutManager = GridLayoutManager(requireContext(),2)

                    binding.shopRv.adapter= ShopingAdapter(requireContext(),list)
                }catch (e:Exception){

                }

            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "lol", Toast.LENGTH_SHORT).show()
            }

        return binding.root
    }


}