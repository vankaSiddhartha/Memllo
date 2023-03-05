package com.techvanka.memllo.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.techvanka.memllo.MainActivity
import com.techvanka.memllo.R

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        var uri = intent!!.data
        Handler(Looper.getMainLooper()).postDelayed({
            if (FirebaseAuth.getInstance().currentUser != null&& uri == null) {
                startActivity(Intent(this, MainActivity::class.java))
//               startActivity(Intent(this,VideosActivity::class.java))
                finish()
            }else if(uri != null) {
                var intent = Intent(this, ShareVideoView::class.java)
                intent.putExtra("id", uri.toString())

                startActivity(intent)
                finish()

            } else
             {
                startActivity(Intent(this, CreateAccount::class.java))
                finish()
            }
        }, 1000)
    }


}