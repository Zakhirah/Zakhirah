package com.talaba.zakhirah

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.talaba.zakhirah.databinding.ActivityUploadKitabBinding
import com.talaba.zakhirah.models.Kitab
import java.time.LocalDateTime

class UploadKitabActivity : AppCompatActivity() {
    lateinit var binding: ActivityUploadKitabBinding
    lateinit var database:FirebaseDatabase
    lateinit var storage:FirebaseStorage
    lateinit var reference:StorageReference
    lateinit var upload_kitab_url : String
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadKitabBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()
        binding.uploadKitabUrl.setOnClickListener{
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "application/pdf"
            startActivityForResult(intent, 101)
        }
        binding.uploadKitabUpload.setOnClickListener{
            reference.child("kitab_pdf").putFile(Uri.parse(upload_kitab_url)).
            addOnSuccessListener {
                upload_kitab_url = reference.downloadUrl.toString()
            }
            if (!binding.uploadKitabName.text.isEmpty() && !upload_kitab_url.isEmpty()){
                var kitab = Kitab(binding.uploadKitabName.text.toString(),
                    binding.uploadKitabDescription.text.toString(),
                    FirebaseAuth.getInstance().uid.toString(),
                    upload_kitab_url,
                    LocalDateTime.now(),
                    binding.uploadKitabLanguage.text.toString(),
                    false)
                database.reference.child("kitab").child(database.reference.push().key.toString()).setValue(kitab)
            }
            else{
                Toast.makeText(this,"Kitab name is required...",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        upload_kitab_url = data.toString()
    }
}