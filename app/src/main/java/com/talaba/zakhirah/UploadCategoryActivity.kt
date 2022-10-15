package com.talaba.zakhirah

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import com.talaba.zakhirah.databinding.ActivityUploadCategoryBinding
import java.util.*

class UploadCategoryActivity : AppCompatActivity() {
    lateinit var binding : ActivityUploadCategoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.uploadCategoryUpload.setOnClickListener{
            if (binding.uploadCategoryName.text.isNotEmpty()){
                var date = Date()
                FirebaseDatabase.getInstance().reference.child("category").child(binding.uploadCategoryName.text.toString()).setValue(date.time)
                Toast.makeText(this,"Your Category Uploaded Successfully",Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,MainActivity::class.java))
            }
        }
    }
}