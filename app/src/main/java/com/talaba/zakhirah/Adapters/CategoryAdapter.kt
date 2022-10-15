package com.talaba.zakhirah.Adapters

import android.content.Context
import android.graphics.Color
import android.text.method.Touch
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.talaba.zakhirah.R
import com.talaba.zakhirah.databinding.SampleFununBinding
import com.talaba.zakhirah.models.Kitab

class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    var context: Context? = null
    var Fununs: ArrayList<String>? = null
    constructor(context: Context?, Fununs: ArrayList<String>?) {
        this.context = context
        this.Fununs = Fununs
    }
    class CategoryViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView) {
        lateinit var binding : SampleFununBinding
        init {
            binding = SampleFununBinding.bind(itemView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        var view : View = LayoutInflater.from(context).inflate(R.layout.sample_funun,parent,false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        var funun = Fununs?.get(position)
        holder.binding.fununFun.text = funun
        holder.binding.fununFun.setOnClickListener{
            holder.binding.fununFun.textColors.defaultColor
            holder.binding.fununFun.setBackgroundColor(R.color.bars)
            holder.binding.fununFun.cornerRadius = 40
        }
    }

    override fun getItemCount(): Int {
        var a = Fununs?.size
        return a!!
        TODO("Not yet implemented")
    }
}