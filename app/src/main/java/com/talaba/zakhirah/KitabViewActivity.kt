package com.talaba.zakhirah

import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.listener.OnErrorListener
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.talaba.zakhirah.databinding.ActivityKitabViewBinding
import kotlinx.coroutines.flow.combine
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import javax.net.ssl.HttpsURLConnection
import kotlin.collections.ArrayList
import kotlin.properties.Delegates


open class KitabViewActivity : AppCompatActivity() {
    lateinit var binding: ActivityKitabViewBinding
    lateinit var savedKitabs: ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKitabViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FirebaseDatabase.getInstance().reference
            .child("kitab")
            .child(intent.getStringExtra("id").toString())
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    supportActionBar?.title = snapshot.child("kitab_name").value.toString()
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
////        Toast.makeText(this,        intent.getStringExtra("url"),Toast.LENGTH_SHORT).show()
//      binding.web.settings.javaScriptEnabled
////        var pdf_url = "https://docs.google.com/gview?embedded=true&url=" + intent.getStringExtra("url")?.substringAfter("https://","")
////        if (pdf_url != null) {
////            binding.web.loadUrl(pdf_url)
////        }
//        binding.web.settings.allowFileAccessFromFileURLs
//        binding.web.loadUrl("https://docs.google.com/gview?embedded=true&url="+ (intent.getStringExtra("url")
//            ?.drop(8)))
//        binding.kitabViewPdfview.fromUri(Uri.parse(intent.getStringExtra("url")))
//            .onLoad(OnLoadCompleteListener {
//                Toast.makeText(this,"Success",Toast.LENGTH_SHORT).show()
//            })
//            .onError(OnErrorListener {
//                Toast.makeText(this,it.message,Toast.LENGTH_LONG).show()
//            })
        RetrievePDFFromURL(binding.kitabViewPdfview).execute(intent.getStringExtra("url"))
    }
    class RetrievePDFFromURL(pdfView: PDFView) :
        AsyncTask<String, Void, InputStream>() {

        // on below line we are creating a variable for our pdf view.
        val mypdfView: PDFView = pdfView

        // on below line we are calling our do in background method.
        override fun doInBackground(vararg params: String?): InputStream? {
            // on below line we are creating a variable for our input stream.
            var inputStream: InputStream? = null
            try {
                // on below line we are creating an url
                // for our url which we are passing as a string.
                val url = URL(params.get(0))

                // on below line we are creating our http url connection.
                val urlConnection: HttpURLConnection = url.openConnection() as HttpsURLConnection

                // on below line we are checking if the response
                // is successful with the help of response code
                // 200 response code means response is successful
                if (urlConnection.responseCode == 200) {
                    // on below line we are initializing our input stream
                    // if the response is successful.
                    inputStream = BufferedInputStream(urlConnection.inputStream)
                }
            }
            // on below line we are adding catch block to handle exception
            catch (e: Exception) {
                // on below line we are simply printing
                // our exception and returning null
                e.printStackTrace()
                return null;
            }
            // on below line we are returning input stream.
            return inputStream;
        }

        // on below line we are calling on post execute
        // method to load the url in our pdf view.
        override fun onPostExecute(result: InputStream?) {
            // on below line we are loading url within our
            // pdf view on below line using input stream.
            mypdfView.fromStream(result).load()

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.kitab_menu,menu)
        FirebaseDatabase.getInstance().reference.child("users").child(FirebaseAuth.getInstance().uid.toString())
            .child("saved")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        for (snapshot1 in snapshot.children){
                            if(intent.getStringExtra("id").toString() == snapshot1.key){
                                menu?.getItem(0)?.setIcon(R.drawable.saved)
                                break
                            }
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        FirebaseDatabase.getInstance().reference.child("users")
            .child(FirebaseAuth.getInstance().uid.toString())
            .child("saved")
            .child(intent.getStringExtra("id").toString())
            .addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        removeKitab(item)
                    }
                    else
                        saveKitab(item)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        return super.onOptionsItemSelected(item)
    }
    fun removeKitab(item: MenuItem){
        FirebaseAuth.getInstance().uid?.let {
            FirebaseDatabase.getInstance().reference.child("users")
                .child(FirebaseAuth.getInstance().uid.toString())
                .child("saved")
                .child(intent.getStringExtra("id").toString())
                .setValue(null)
            Toast.makeText(this,"Removed Kitab Successfully",Toast.LENGTH_SHORT).show()
            item.setIcon(R.drawable.save)
        }
    }
    fun saveKitab(item: MenuItem){
        FirebaseAuth.getInstance().uid?.let {
            FirebaseDatabase.getInstance().reference.child("users")
                .child(FirebaseAuth.getInstance().uid.toString())
                .child("saved")
                .child(intent.getStringExtra("id").toString())
                .setValue(Date().time)
            Toast.makeText(this,"Saved Kitab Successfully",Toast.LENGTH_SHORT).show()
            item.setIcon(R.drawable.saved)
        }
    }
}