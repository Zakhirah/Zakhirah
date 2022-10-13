package com.talaba.zakhirah

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.talaba.zakhirah.databinding.ActivityMainBinding
import com.talaba.zakhirah.databinding.ActivitySignupBinding
import com.talaba.zakhirah.models.User
import java.sql.Date
import java.sql.Time
import java.time.LocalDateTime

class SignupActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignupBinding
    lateinit var auth:FirebaseAuth
    lateinit var database: FirebaseDatabase
    lateinit var storage: FirebaseStorage
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
                            User(binding.signupUsername.text.toString(),binding.signupEmail.text.toString(),Integer.parseInt(binding.signupPhone.text.toString()),binding.signupConfirmPassword.text.toString(),binding.signupState.selectedItem.toString(),
                                LocalDateTime.now())
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
    }
}