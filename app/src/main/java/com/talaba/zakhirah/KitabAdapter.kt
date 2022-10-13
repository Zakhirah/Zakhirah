package com.talaba.zakhirah

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.talaba.zakhirah.databinding.SampleKitabBinding
import com.talaba.zakhirah.models.Kitab

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
            holder.binding.kitabName.setText(kitab.kitab_name)
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