package com.talaba.zakhirah

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.talaba.zakhirah.databinding.ActivitySignupBinding
import com.talaba.zakhirah.models.User
import java.util.*

class SignupActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignupBinding
    lateinit var auth:FirebaseAuth
    lateinit var database: FirebaseDatabase
    lateinit var storage: FirebaseStorage
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()
        binding.signupSignup.setOnClickListener(View.OnClickListener {
            if(binding.signupPassword.text.toString()==binding.signupConfirmPassword.text.toString() && !binding.signupEmail.text.isEmpty()) {
                auth.createUserWithEmailAndPassword(
                    binding.signupEmail.text.toString(),
                    binding.signupConfirmPassword.text.toString()
                )
                    .addOnSuccessListener {
                        var user =
                            User(binding.signupUsername.text.toString(),binding.signupEmail.text.toString(),binding.signupPhone.text.toString(),binding.signupConfirmPassword.text.toString(),binding.signupState.selectedItem.toString(),
                               Date().time)
                        database.reference.child("users")
                            .child(FirebaseAuth.getInstance().uid.toString()).setValue(user)
                            .addOnSuccessListener {
                                Toast.makeText(
                                    this,
                                    "welcome to zakhirah", Toast.LENGTH_SHORT
                                ).show()
                                var intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                            }
                    }
            }
            else{
                Toast.makeText(this,"Password not matched",Toast.LENGTH_SHORT).show()
            }
        })
        binding.signupLogin.setOnClickListener{
            startActivity(Intent(this,LoginActivity::class.java))
        }
    }
}