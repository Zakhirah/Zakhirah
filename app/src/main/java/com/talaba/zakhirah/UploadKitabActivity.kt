package com.talaba.zakhirah

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.talaba.zakhirah.databinding.ActivityUploadKitabBinding

class UploadKitabActivity : AppCompatActivity() {
    lateinit var binding: ActivityUploadKitabBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadKitabBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}