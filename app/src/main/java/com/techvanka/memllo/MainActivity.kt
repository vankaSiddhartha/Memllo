package com.techvanka.memllo

import android.app.ProgressDialog.show
import android.content.Context
import android.content.SharedPreferences
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.techvanka.memllo.databinding.ActivityMainBinding
import com.techvanka.memllo.fragments.*
import org.json.JSONArray

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        LoadFrag(MemesVideoFragment())

        var title = "Memllo"
        supportActionBar?.setBackgroundDrawable( ColorDrawable( ContextCompat.getColor(this, R.color.btn_text_colour)))
        val titleColor = ContextCompat.getColor(this, R.color.background_colour)
        supportActionBar?.title = HtmlCompat.fromHtml("<font color='$titleColor'>${title}</font>", HtmlCompat.FROM_HTML_MODE_LEGACY)

       binding.navView.setOnItemSelectedListener { menuItem->
           when (menuItem.itemId){
              R.id.memesVideoFragment->{
                  LoadFrag(MemesVideoFragment())
                  true
              }

               R.id.uploadFragment->{
                   UploadFragment().show(supportFragmentManager,"UploadFragment")
                   true
               }
               R.id.memesMarket->{
                   LoadFrag(MemesMarket())
                   true
               }
               R.id.accountFragment->
               {
                   LoadFrag(AccountFragment())
                   true
               }
               else -> {
                   LoadFrag(MemesVideoFragment())
                   true
               }
           }

       }









}

    private fun LoadFrag(fragment: Fragment) {
        var load = supportFragmentManager.beginTransaction()
        load.replace(R.id.nav_host_fragment_activity_main,fragment)
        load.commit()
    }







    }
