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
import com.google.firebase.storage.UploadTask
import com.talaba.zakhirah.databinding.ActivityUploadKitabBinding
import com.talaba.zakhirah.models.Kitab
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

class UploadKitabActivity : AppCompatActivity() {
    lateinit var binding: ActivityUploadKitabBinding
    lateinit var database:FirebaseDatabase
    lateinit var storage:FirebaseStorage
    lateinit var reference:StorageReference
    lateinit var upload_kitab_url :Uri
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadKitabBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()
        reference = FirebaseStorage.getInstance().reference
        binding.uploadKitabUrl.setOnClickListener{
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "application/pdf"
            startActivityForResult(intent, 101)
        }
        binding.uploadKitabUpload.setOnClickListener{
            var uploadTask : UploadTask
            var storageRef : StorageReference = FirebaseStorage.getInstance().getReference()
            var file = upload_kitab_url
            var riversRef = storageRef.child("kitab_pdf/${binding.uploadKitabName.text.toString() + FirebaseAuth.getInstance().uid}")
            uploadTask = riversRef.putFile(file)
            uploadTask.addOnFailureListener {
                Toast.makeText(this, uploadTask.exception?.message,Toast.LENGTH_SHORT).show()
                // Handle unsuccessful uploads
            }.addOnSuccessListener { taskSnapshot ->
                // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
                riversRef.downloadUrl.addOnSuccessListener{uri ->
                    if (!binding.uploadKitabName.text.isEmpty()){
                        val date = Date()
                        var kitab = Kitab(binding.uploadKitabName.text.toString(),
                            binding.uploadKitabDescription.text.toString(),
                            FirebaseAuth.getInstance().uid.toString(),
                            uri.toString(),
                            date.time,
                            binding.uploadKitabLanguage.text.toString(),
                            false)
                        database.reference.child("kitab").child(database.reference.push().key.toString()).setValue(kitab)
                            .addOnSuccessListener {
                                Toast.makeText(this,"Kitab Uploaded for processing...",Toast.LENGTH_SHORT).show()
                                startActivity(Intent(applicationContext,MainActivity::class.java))
                            }
                    }
                    else{
                        Toast.makeText(this,"Kitab name is required...",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            upload_kitab_url = data.data!!
        }
    }
}