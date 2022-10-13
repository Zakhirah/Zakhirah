package com.talaba.zakhirah

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.talaba.zakhirah.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (FirebaseAuth.getInstance().currentUser!=null){
            startActivity(Intent(this, MainActivity::class.java))
        }
        auth = FirebaseAuth.getInstance()
        binding.signup.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this,SignupActivity::class.java))
        })
        binding.signin.setOnClickListener(View.OnClickListener {
            try {
                auth.signInWithEmailAndPassword(
                    binding.email.text.toString(),
                    binding.password.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful){
                        Toast.makeText(this, "welcome back", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                    }
                    else{
                        Toast.makeText(this, it.exception?.message.toString(),Toast.LENGTH_LONG).show()
                    }
                }
            }
            catch (e: Exception){
                Toast.makeText(this,e.message,Toast.LENGTH_LONG).show()
            }
        })
    }
}