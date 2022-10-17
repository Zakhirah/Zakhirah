package com.talaba.zakhirah.Adapters

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.shockwave.pdfium.PdfDocument
import com.shockwave.pdfium.PdfiumCore
import com.talaba.zakhirah.KitabViewActivity
import com.talaba.zakhirah.R
import com.talaba.zakhirah.databinding.SampleKitabBinding
import com.talaba.zakhirah.models.Kitab
import java.io.File
import java.io.FileOutputStream

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
        var kitab = Kitabs?.get(position)
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
        if (kitab != null) {
            context?.let { it1 -> Glide.with(it1).asBitmap().load(kitab.kitab_url).placeholder(R.drawable.profile).into(holder.binding.kitabThumbnail) }
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