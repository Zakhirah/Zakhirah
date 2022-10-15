package com.talaba.zakhirah

import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.barteksc.pdfviewer.listener.OnErrorListener
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener
import com.talaba.zakhirah.databinding.ActivityKitabViewBinding
import kotlinx.coroutines.flow.combine
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection


open class KitabViewActivity : AppCompatActivity() {
    lateinit var binding: ActivityKitabViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKitabViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
////        Toast.makeText(this,        intent.getStringExtra("url"),Toast.LENGTH_SHORT).show()
//      binding.web.settings.javaScriptEnabled
////        var pdf_url = "https://docs.google.com/gview?embedded=true&url=" + intent.getStringExtra("url")?.substringAfter("https://","")
////        if (pdf_url != null) {
////            binding.web.loadUrl(pdf_url)
////        }
//        binding.web.settings.allowFileAccessFromFileURLs
//        binding.web.loadUrl("https://docs.google.com/gview?embedded=true&url="+ (intent.getStringExtra("url")
//            ?.drop(8)))
        binding.kitabViewPdfview.fromUri(Uri.parse(intent.getStringExtra("url")))
            .onLoad(OnLoadCompleteListener {
                Toast.makeText(this,"Success",Toast.LENGTH_SHORT).show()
            })
            .onError(OnErrorListener {
                Toast.makeText(this,it.message,Toast.LENGTH_LONG).show()
            })
    }
}