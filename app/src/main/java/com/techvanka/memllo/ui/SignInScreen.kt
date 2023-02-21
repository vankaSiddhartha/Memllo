 package com.techvanka.memllo.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.techvanka.memllo.MainActivity
import com.techvanka.memllo.databinding.ActivitySignInBinding

class SignIn : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.SignInprogressBar.visibility = View.GONE
        // intent to Create Account
        binding.CreateAccountText.setOnClickListener {
            startActivity(Intent(this,CreateAccount::class.java))
        }
        binding.SignInButton.setOnClickListener {
            binding.SignInprogressBar.visibility = View.VISIBLE
            signInFunction(binding.signInEmail,binding.signInPassword)
        }

    }

    private fun signInFunction(signInEmail: EditText, signInPassword: EditText) {
       var getEmail = signInEmail.text.toString().trim()
        var getPassword = signInPassword.text.toString().trim()

        if(getEmail.isNotEmpty()&&getPassword.isNotEmpty()){
            if(getPassword.length>=8)
            {
              FirebaseAuth.getInstance().signInWithEmailAndPassword(getEmail,getPassword).addOnSuccessListener {
                  binding.SignInprogressBar.visibility = View.GONE
                  startActivity(Intent(this,MainActivity::class.java))
              }.addOnFailureListener { exception->
                  binding.SignInprogressBar.visibility = View.GONE
                  if (exception.toString().contains("FirebaseAuthInvalidUserException")){
                      Toast.makeText(this, "Email is not record please create account", Toast.LENGTH_SHORT).show()
                  }else if (exception.toString().contains("FirebaseAuthWeakPasswordException")){
                          Toast.makeText(this, "Password is week", Toast.LENGTH_SHORT).show()
                      }else{
                      Toast.makeText(this, "Invalid email", Toast.LENGTH_SHORT).show()
                  }

              }
            }else{

                binding.SignInprogressBar.visibility = View.GONE
                Toast.makeText(this, "Password length should be 8", Toast.LENGTH_SHORT).show()
            }
        }else{

            binding.SignInprogressBar.visibility = View.GONE
            Toast.makeText(this, "Empty fill is not allowed!!", Toast.LENGTH_SHORT).show()
        }
    }
}