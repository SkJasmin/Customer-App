package com.example.customerapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.customerapp.databinding.ActivitySignupBinding
import com.example.customerapp.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database

class SignupActivity : AppCompatActivity() {
    private lateinit var email: String
    private lateinit var password:String
    private lateinit var username:String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
        private val binding: ActivitySignupBinding by lazy {
            ActivitySignupBinding.inflate(layoutInflater)
        }
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(binding.root)
            //Initalize Firebase auth
            auth = FirebaseAuth.getInstance()

            //Initalize firebase Database
            database= Firebase.database.reference




            binding.createAccountButton.setOnClickListener {
                username = binding.name.text.toString()
                email = binding.email.text.toString().trim()
                password = binding.password.text.toString().trim()

                if(email.isBlank()||password.isBlank()||username.isBlank()){
                    Toast.makeText(this,"Please fill all the details", Toast.LENGTH_SHORT).show()
                }
                else{
                    createAccount(email,password)
                }
            }
            binding.alreadyHaveAnAccount.setOnClickListener {
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
            }
        }
    private fun SignupActivity.createAccount(email: String, password: String) {
       auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
           task->
           if(task.isSuccessful){
               Toast.makeText(this,"Account created successfully", Toast.LENGTH_SHORT).show()
               saveUserData()
               startActivity(Intent(this, LoginActivity::class.java))
               finish()
           }
           else{
               Toast.makeText(this,"Account creation failed", Toast.LENGTH_SHORT).show()
               Log.d("Account","createAccount: Failure",task.exception)
           }
       }
    }
    private fun SignupActivity.saveUserData() {
        //retrive data from input filed
        username=binding.name.text.toString()
        email=binding.email.text.toString().trim()
        password = binding.password.text.toString().trim()
        val user = UserModel(username,email, password)
        val userId= FirebaseAuth.getInstance().currentUser!!.uid
        //save data to Firebase Database
        database.child("user").child(userId).setValue(user)
    }

}



