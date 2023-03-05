package com.techvanka.memllo.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.techvanka.memllo.MainActivity
import com.techvanka.memllo.R
import com.techvanka.memllo.databinding.ActivityCreateAccountBinding
import com.techvanka.memllo.databinding.ActivitySplashScreenBinding
import com.techvanka.memllo.databinding.LayoutLanguageBinding
import com.techvanka.memllo.model.User
import org.json.JSONArray


class CreateAccount : AppCompatActivity() {
    private var CODEENG=0
    private var CODETEL =0
    private var CODEHIN=0
    private var CODETAM =0
    private var CODEBANGLA =0
    private var CODEMARATHI=0
    private lateinit var binding: ActivityCreateAccountBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth
    var list:ArrayList<String> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.createUserPb.visibility =View.GONE
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        firebaseAuth = Firebase.auth
        binding.googleSignBtn.setOnClickListener {
            val intent = googleSignInClient.signInIntent
            googleSignInActivityResultLauncher.launch(intent)

        }


    }


    private fun showDailog() {
        val builder = AlertDialog.Builder(this)
        var bindingVIEW = LayoutLanguageBinding.inflate(layoutInflater)
        builder.setView(bindingVIEW.root)
        val dialog = builder.create()
        bindingVIEW.teluguCard.setOnClickListener {

            if (CODETEL%2==0){
                list.add("tel")
                bindingVIEW.teluguCard.setCardBackgroundColor(ContextCompat.getColor(applicationContext,
                    R.color.teal_200))
            }else{
                list.remove("tel")
                bindingVIEW.teluguCard.setCardBackgroundColor(ContextCompat.getColor(applicationContext,
                    R.color.white
                ))

            }
            CODETEL++

        }
        bindingVIEW.HindiCard.setOnClickListener {
            if (CODEHIN%2==0){
                list.add("hin")
                bindingVIEW.HindiCard.setCardBackgroundColor(ContextCompat.getColor(applicationContext,
                    R.color.teal_200))
            }else{
                list.remove("hin")
                bindingVIEW.HindiCard.setCardBackgroundColor(ContextCompat.getColor(applicationContext,
                    R.color.white
                ))

            }
            CODEHIN++




            }

        bindingVIEW.tamilCard.setOnClickListener {
            if (CODETAM%2==0){
                list.add("tamil")
                bindingVIEW.tamilCard.setCardBackgroundColor(ContextCompat.getColor(applicationContext,
                    R.color.teal_200))
            }else{
                list.remove("tamil")
                bindingVIEW.tamilCard.setCardBackgroundColor(ContextCompat.getColor(applicationContext,
                    R.color.white
                ))

            }
            CODETAM++




        }
        bindingVIEW.EnglishCard.setOnClickListener {
            if (CODEENG%2==0){
                list.add("eng")
                bindingVIEW.EnglishCard.setCardBackgroundColor(ContextCompat.getColor(applicationContext,
                    R.color.teal_200))
            }else{
                list.remove("eng")
                bindingVIEW.EnglishCard.setCardBackgroundColor(ContextCompat.getColor(applicationContext,
                    R.color.white
                ))

            }
            CODEENG++


        }
        bindingVIEW.bengaliCard.setOnClickListener {
            if (CODEBANGLA%2==0){
                list.add("ben")
                bindingVIEW.bengaliCard.setCardBackgroundColor(ContextCompat.getColor(applicationContext,
                    R.color.teal_200))
            }else{
                list.remove("ben")
                bindingVIEW.bengaliCard.setCardBackgroundColor(ContextCompat.getColor(applicationContext,
                    R.color.white
                ))

            }
            CODEBANGLA++

        }
        bindingVIEW.MarathiCard.setOnClickListener {
            if (CODEMARATHI%2==0){
                list.add("mar")
                bindingVIEW.MarathiCard.setCardBackgroundColor(ContextCompat.getColor(applicationContext,
                    R.color.teal_200))
            }else{
                list.remove("mar")
                bindingVIEW.MarathiCard.setCardBackgroundColor(ContextCompat.getColor(applicationContext,
                    R.color.white
                ))

            }
            CODEMARATHI++

        }
        bindingVIEW.continueBtn.setOnClickListener {
        val prefs =
                getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val editor = prefs.edit()
            val jsonArray = JSONArray(list)
            editor.putString("list", jsonArray.toString())
            editor.apply()

            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().currentUser!!.uid)
                .setValue(User(FirebaseAuth.getInstance().currentUser!!.displayName,FirebaseAuth.getInstance().currentUser!!.uid,FirebaseAuth.getInstance().currentUser!!.photoUrl.toString(),list)).addOnSuccessListener {
                    binding.createUserPb.visibility =View.GONE

                    Toast.makeText(this, "Welcome to Memllo", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this,MainActivity::class.java))
                }

            Toast.makeText(this, "$list", Toast.LENGTH_SHORT).show()
        }
        dialog.show()

    }

    private val googleSignInActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            if (result.resultCode == RESULT_OK) {


                val accountTask = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = accountTask.getResult(ApiException::class.java)
                    firebaseAuthWithGoogleAccount(account)
                } catch (e: ApiException) {
                    Toast.makeText(this, "Error!!", Toast.LENGTH_SHORT).show()
                }
            } else {

            }
        }

    private fun firebaseAuthWithGoogleAccount(account: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(account!!.idToken, null)
        binding.googeText.visibility = View.GONE
        binding.createUserPb.visibility =View.VISIBLE
        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener { authRes ->

                if (authRes.additionalUserInfo!!.isNewUser) {

                    binding.createUserPb.visibility =View.GONE
                    showDailog()



                } else {
                    binding.createUserPb.visibility =View.GONE
                    Toast.makeText(this, "Welcome to Memllo", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this,MainActivity::class.java))


                }

            }
            .addOnFailureListener { err ->
                Toast.makeText(this, "Error!!", Toast.LENGTH_SHORT).show()
            }
    }


}