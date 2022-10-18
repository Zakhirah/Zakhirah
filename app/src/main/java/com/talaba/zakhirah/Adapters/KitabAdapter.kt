package com.talaba.zakhirah.Adapters

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.AsyncTask
import android.os.Environment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.loader.content.Loader
import androidx.recyclerview.widget.RecyclerView
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener
import com.github.barteksc.pdfviewer.listener.OnTapListener
import com.shockwave.pdfium.PdfDocument
import com.shockwave.pdfium.PdfiumCore
import com.talaba.zakhirah.KitabViewActivity
import com.talaba.zakhirah.R
import com.talaba.zakhirah.databinding.SampleKitabBinding
import com.talaba.zakhirah.models.Kitab
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class KitabAdapter : RecyclerView.Adapter<KitabAdapter.KitabViewHolder> {
    var context: Context? = null
    var Kitabs: ArrayList<Kitab>? = null
    constructor(context: Context?, Kitabs: ArrayList<Kitab>?) {
        this.context = context
        this.Kitabs = Kitabs
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KitabViewHolder {
        var view : View = LayoutInflater.from(context).inflate(R.layout.sample_kitab,parent,false)
        return KitabViewHolder(view)
    }
    override fun onBindViewHolder(holder: KitabViewHolder, position: Int) {
        var kitab:Kitab = Kitabs?.get(position)!!
        if (kitab != null) {
            holder.binding.kitabName.text = kitab.kitab_name
        }
        holder.binding.kitabThumbnail.setOnClickListener {
            var intent = Intent(context, KitabViewActivity::class.java)
            if (kitab != null) {
                intent.putExtra("url", kitab.kitab_url)
                intent.putExtra("id", kitab.kitab_id)
            }
            context?.startActivity(intent)
        }
        holder.binding.kitabRefreshThumbnail.setOnClickListener{
            if (kitab != null) {
                RetrievePDFFromURL(holder.binding.kitabThumbnail,holder).execute(kitab.kitab_url)
                holder.binding.kitabProgressBar.visibility = View.VISIBLE
                holder.binding.kitabRefreshThumbnail.visibility = View.GONE
            }
        }
        if (kitab != null) {
            RetrievePDFFromURL(holder.binding.kitabThumbnail,holder).execute(kitab.kitab_url)
            holder.binding.kitabProgressBar.visibility = View.VISIBLE
            holder.binding.kitabRefreshThumbnail.visibility = View.GONE
        }
    }
    class RetrievePDFFromURL(pdfView: PDFView,holder: KitabViewHolder) :
        AsyncTask<String, Void, InputStream>() {
        // on below line we are creating a variable for our pdf view.
        val mypdfView: PDFView = pdfView
        val holder : KitabViewHolder = holder
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
            mypdfView.fromStream(result)
                .onLoad(OnLoadCompleteListener {
                    holder.binding.kitabProgressBar.visibility = View.GONE
                    holder.binding.kitabRefreshThumbnail.visibility = View.VISIBLE
                })
                .load()
        }
    }


    override fun getItemCount(): Int {
        return Kitabs?.size ?: 0
    }
    class KitabViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var binding : SampleKitabBinding
        init {
            binding = SampleKitabBinding.bind(itemView)
        }
    }
}