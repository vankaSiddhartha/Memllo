package com.techvanka.memllo.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.techvanka.memllo.R
import com.techvanka.memllo.databinding.ActivityShopItemViewBinding
import com.techvanka.memllo.databinding.ShopingDailogLayoutBinding
import com.techvanka.memllo.model.OrderItemModel
import com.techvanka.memllo.model.ShopingModel
import com.techvanka.memllo.model.VideoUploadModel

class ShopItemView : AppCompatActivity() {
    private lateinit var binding:ActivityShopItemViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopItemViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        Glide.with(this).load(intent.getStringExtra("imgUrl")).into(binding.productImage)
       cartBtnLogic()
        binding.BuymowBtn.setOnClickListener {
             showDailog()
        }

    }

    private fun cartBtnLogic() {
        var list = arrayListOf<String>()
       try {
           if (intent.getStringExtra("c")!!.isNotEmpty()){
               binding.cartBtn.isChecked = true
           }
       }catch (e:Exception){

       }


        binding.cartBtn.setOnCheckedChangeListener{buttonView, isChecked ->
            if (isChecked){
                val db = Firebase.firestore
                intent.getStringExtra("imgUrl")?.let { list.add(it) }

                var data = ShopingModel(intent.getStringExtra("pid"),intent.getStringExtra("title"),intent.getStringExtra("dis"),list,intent.getStringExtra("money"))
                db.collection("Cart")
                    .add(data)
                    .addOnSuccessListener { documentReference ->
                        Toast.makeText(this, "Added in Cart", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this,CartShowActivity::class.java),)
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
                    }
            }else{
                Toast.makeText(this, "noo", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDailog() {
        var size=""
        var quantity = 0

        val builder = AlertDialog.Builder(this)
        var bindingVIEW = ShopingDailogLayoutBinding.inflate(layoutInflater)
        builder.setView(bindingVIEW.root)
        val dialog = builder.create()
        dialog.show()
        bindingVIEW.smallCard.setOnClickListener {
            size ="s"
            bindingVIEW.smallCard.setCardBackgroundColor(
                ContextCompat.getColor(applicationContext,
                R.color.teal_200))
            bindingVIEW.mediumCard.setCardBackgroundColor(ContextCompat.getColor(applicationContext,
                R.color.background_colour
            ))
            bindingVIEW.largeCard.setCardBackgroundColor(ContextCompat.getColor(applicationContext,
                R.color.background_colour
            ))
            bindingVIEW.largeCard.setCardBackgroundColor(ContextCompat.getColor(applicationContext,
                R.color.background_colour
            ))
            bindingVIEW.XlCard.setCardBackgroundColor(ContextCompat.getColor(applicationContext,
                R.color.background_colour
            ))
            bindingVIEW.XXLCard.setCardBackgroundColor(ContextCompat.getColor(applicationContext,
                R.color.background_colour
            ))
        }
        bindingVIEW.mediumCard.setOnClickListener {
            size = "m"
            bindingVIEW.smallCard.setCardBackgroundColor(
                ContextCompat.getColor(applicationContext,
                    R.color.background_colour
                ))
            bindingVIEW.mediumCard.setCardBackgroundColor(ContextCompat.getColor(applicationContext,
                R.color.teal_200
            ))
            bindingVIEW.largeCard.setCardBackgroundColor(ContextCompat.getColor(applicationContext,
                R.color.background_colour
            ))
            bindingVIEW.largeCard.setCardBackgroundColor(ContextCompat.getColor(applicationContext,
                R.color.background_colour
            ))
            bindingVIEW.XlCard.setCardBackgroundColor(ContextCompat.getColor(applicationContext,
                R.color.background_colour
            ))
            bindingVIEW.XXLCard.setCardBackgroundColor(ContextCompat.getColor(applicationContext,
                R.color.background_colour
            ))
        }
        bindingVIEW.largeCard.setOnClickListener {
            size ="l"
            bindingVIEW.smallCard.setCardBackgroundColor(
                ContextCompat.getColor(applicationContext,
                    R.color.background_colour
                ))
            bindingVIEW.mediumCard.setCardBackgroundColor(ContextCompat.getColor(applicationContext,
                R.color.background_colour
            ))
            bindingVIEW.largeCard.setCardBackgroundColor(ContextCompat.getColor(applicationContext,
                R.color.teal_200
            ))
            bindingVIEW.XlCard.setCardBackgroundColor(ContextCompat.getColor(applicationContext,
                R.color.background_colour
            ))
            bindingVIEW.XXLCard.setCardBackgroundColor(ContextCompat.getColor(applicationContext,
                R.color.background_colour
            ))
        }
        bindingVIEW.XlCard.setOnClickListener {
            size = "xl"
            bindingVIEW.smallCard.setCardBackgroundColor(
                ContextCompat.getColor(applicationContext,
                    R.color.background_colour
                ))
            bindingVIEW.mediumCard.setCardBackgroundColor(ContextCompat.getColor(applicationContext,
                R.color.background_colour
            ))
            bindingVIEW.largeCard.setCardBackgroundColor(ContextCompat.getColor(applicationContext,
                R.color.background_colour
            ))
            bindingVIEW.largeCard.setCardBackgroundColor(ContextCompat.getColor(applicationContext,
                R.color.background_colour
            ))
            bindingVIEW.XlCard.setCardBackgroundColor(ContextCompat.getColor(applicationContext,
                R.color.teal_200
            ))
            bindingVIEW.XXLCard.setCardBackgroundColor(ContextCompat.getColor(applicationContext,
                R.color.background_colour
            ))
        }
        bindingVIEW.XXLCard.setOnClickListener {
            size ="xxl"
            bindingVIEW.smallCard.setCardBackgroundColor(
                ContextCompat.getColor(applicationContext,
                    R.color.background_colour
                ))
            bindingVIEW.mediumCard.setCardBackgroundColor(ContextCompat.getColor(applicationContext,
                R.color.background_colour
            ))
            bindingVIEW.largeCard.setCardBackgroundColor(ContextCompat.getColor(applicationContext,
                R.color.background_colour
            ))
            bindingVIEW.largeCard.setCardBackgroundColor(ContextCompat.getColor(applicationContext,
                R.color.background_colour
            ))
            bindingVIEW.XlCard.setCardBackgroundColor(ContextCompat.getColor(applicationContext,
                R.color.background_colour
            ))
            bindingVIEW.XXLCard.setCardBackgroundColor(ContextCompat.getColor(applicationContext,
                R.color.teal_200
            ))
        }
        bindingVIEW.decementTv.setOnClickListener {
            if (bindingVIEW.quaantity.text.toString().equals("1")){
                quantity =1
                Toast.makeText(this, "Sorry!! you should try 1", Toast.LENGTH_SHORT).show()
            }else{
                var no = bindingVIEW.quaantity.text.toString().toInt()
                quantity = no-1
                bindingVIEW.quaantity.setText((no-1).toString())
            }
        }
        bindingVIEW.incrementTV.setOnClickListener {
            if (bindingVIEW.quaantity.text.toString().equals("10")){
                quantity = 10
                Toast.makeText(this, "Sorry you can,t purchase more then 10!!", Toast.LENGTH_SHORT).show()
            }else{
                var no = bindingVIEW.quaantity.text.toString().toInt()
                quantity =  no+1
                bindingVIEW.quaantity.setText((no+1).toString())
            }
        }
        bindingVIEW.ContinueBtn.setOnClickListener {
            val db = Firebase.firestore
            if (quantity==0){
                quantity=1
            }
            if (size.isEmpty()){
                Toast.makeText(this, "Select Size!!", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "${size} ${quantity}", Toast.LENGTH_SHORT).show()
            }
            var data = OrderItemModel(size,quantity.toString(),intent.getStringExtra("pid"),bindingVIEW.phoneNo.text.toString(),bindingVIEW.addressId.text.toString(),FirebaseAuth.getInstance().currentUser!!.uid,intent.getStringExtra("imgUrl"),intent.getStringExtra("money"),intent.getStringExtra("dis"),"At Memllo",intent.getStringExtra("title"))
            db.collection("BoughtProducts")
                .add(data)
                .addOnSuccessListener { documentReference ->
                    Toast.makeText(this, "Thanks for purchasing your product on track!!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this,ProductTrackActivity::class.java))
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
                }
        }

    }
}