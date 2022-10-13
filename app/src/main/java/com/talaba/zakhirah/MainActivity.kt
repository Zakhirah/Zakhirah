package com.talaba.zakhirah

import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.talaba.zakhirah.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
    override fun onBackPressed() {
        finishAffinity()
    }
}