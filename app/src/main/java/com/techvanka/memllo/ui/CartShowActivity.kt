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
import com.techvanka.memllo.adapter.CartAdapter
import com.techvanka.memllo.adapter.OrderAdapter
import com.techvanka.memllo.databinding.ActivityCartShowBinding
import com.techvanka.memllo.databinding.LayoutLanguageBinding
import com.techvanka.memllo.model.OrderItemModel
import com.techvanka.memllo.model.ShopingModel

class CartShowActivity : AppCompatActivity() {
   private lateinit var binding:ActivityCartShowBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityCartShowBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        var title = "My Cart"
        val db = Firebase.firestore
        supportActionBar?.setBackgroundDrawable( ColorDrawable( ContextCompat.getColor(this, R.color.btn_text_colour)))
        val titleColor = ContextCompat.getColor(this, R.color.background_colour)
        supportActionBar?.title = HtmlCompat.fromHtml("<font color='$titleColor'>${title}</font>", HtmlCompat.FROM_HTML_MODE_LEGACY)

        // Get a reference to the collection
        val collectionRef = db.collection("Cart")
        var list = arrayListOf<ShopingModel>()
// Query the collection for all documents
        collectionRef
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    list.add(document.toObject(ShopingModel::class.java))
                }
                try {

                    binding.cartRv.layoutManager = LinearLayoutManager(this)
                    binding.cartRv.adapter= CartAdapter(this,list)

                }catch (e:Exception){

                }

            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "lol", Toast.LENGTH_SHORT).show()
            }

    }
}