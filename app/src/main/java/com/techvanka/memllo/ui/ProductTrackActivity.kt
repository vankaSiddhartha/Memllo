package com.techvanka.memllo.ui

import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.techvanka.memllo.R
import com.techvanka.memllo.adapter.OrderAdapter
import com.techvanka.memllo.databinding.ActivityProductTrackBinding
import com.techvanka.memllo.model.OrderItemModel

class ProductTrackActivity : AppCompatActivity() {
    private lateinit var binding:ActivityProductTrackBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductTrackBinding.inflate(layoutInflater)

        setContentView(binding.root)

        var title = "Track"
        supportActionBar?.setBackgroundDrawable( ColorDrawable( ContextCompat.getColor(this, R.color.btn_text_colour)))
        val titleColor = ContextCompat.getColor(this, R.color.background_colour)
        supportActionBar?.title = HtmlCompat.fromHtml("<font color='$titleColor'>${title}</font>", HtmlCompat.FROM_HTML_MODE_LEGACY)

        val db = Firebase.firestore

// Get a reference to the collection
        val collectionRef = db.collection("BoughtProducts")
        var list = arrayListOf<OrderItemModel>()
// Query the collection for all documents
        collectionRef.whereEqualTo("userId", FirebaseAuth.getInstance().currentUser!!.uid.toString())
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    list.add(document.toObject(OrderItemModel::class.java))
                }
                try {
                    binding.productRv.layoutManager = LinearLayoutManager(this)

                    binding.productRv.adapter= OrderAdapter(this,list)

                }catch (e:Exception){

                }

            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "lol", Toast.LENGTH_SHORT).show()
            }
    }
}