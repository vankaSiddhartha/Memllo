package com.techvanka.memllo.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.techvanka.memllo.MainActivity
import com.techvanka.memllo.databinding.ActivityCreateAccountBinding
import com.techvanka.memllo.databinding.ActivitySplashScreenBinding
import com.techvanka.memllo.model.User


class CreateAccount : AppCompatActivity() {
    private lateinit var binding: ActivityCreateAccountBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // hiding support action bar
        supportActionBar?.hide()
        binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.CreateAccountprogressBar.visibility = View.GONE
        binding.CreateAccountBtn.setOnClickListener {
            binding.CreateAccountprogressBar.visibility = View.VISIBLE
            createAccountFuntion(binding.CreateAccountName,binding.CreateAccountEmail,binding.CreateAccountPassword)
        }
    }

    private fun createAccountFuntion(createAccountName: EditText, createAccountEmail: EditText, createAccountPassword: EditText) {
      var name = createAccountName.text.toString()
        var email = createAccountEmail.text.toString().trim()
        var password = createAccountPassword.text.toString().trim()
        if (email.isNotEmpty()&&password.isNotEmpty()){
            if(password.length>=8){
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).addOnSuccessListener {

                 userName(name,email)


                }.addOnFailureListener { exception->
                    binding.CreateAccountprogressBar.visibility = View.GONE
                    if (exception.toString().contains("FirebaseAuthInvalidUserException")){
                        Toast.makeText(this, "Email is not record please create account", Toast.LENGTH_SHORT).show()
                    }else if (exception.toString().contains("FirebaseAuthWeakPasswordException")){
                        Toast.makeText(this, "Password is week", Toast.LENGTH_SHORT).show()
                    }else if (exception.toString().contains("FirebaseAuthUserCollisionException")||exception.toString().contains("FirebaseAuthInvalidCredentialsException")){
                        Toast.makeText(this, "Already exists an account with the given email address", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(this, "", Toast.LENGTH_SHORT).show()
                    }

                }
            }else{
                binding.CreateAccountprogressBar.visibility = View.GONE
                Toast.makeText(this, "Password is week", Toast.LENGTH_SHORT).show()
            }
        }else{
            binding.CreateAccountprogressBar.visibility = View.GONE
            Toast.makeText(this, "Empty fields not allowed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun userName(name: String, email: String) {
        var data = User(name,FirebaseAuth.getInstance().currentUser!!.uid)
      FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(data)
          .addOnSuccessListener {
              binding.CreateAccountprogressBar.visibility = View.GONE
              Toast.makeText(this, "Welcome to club", Toast.LENGTH_SHORT).show()
              startActivity(Intent(this,MainActivity::class.java))
          }.addOnFailureListener {
              Toast.makeText(this, "Error!!", Toast.LENGTH_SHORT).show()
          }

    }
}