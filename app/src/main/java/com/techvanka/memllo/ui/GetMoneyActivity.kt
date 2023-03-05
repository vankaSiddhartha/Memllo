package com.techvanka.memllo.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.techvanka.memllo.R

class GetMoneyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_money)
        Toast.makeText(this, "${intent.getStringExtra("money")}", Toast.LENGTH_SHORT).show()
    }
}