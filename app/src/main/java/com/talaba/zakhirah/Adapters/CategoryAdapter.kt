package com.talaba.zakhirah.Adapters

import android.content.Context
import android.content.res.Configuration
import android.view.*
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.talaba.zakhirah.R
import com.talaba.zakhirah.databinding.SampleFununBinding
import com.talaba.zakhirah.models.Kitab
import java.util.*
import kotlin.collections.ArrayList

class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    var context: Context? = null
    var Fununs: ArrayList<String>? = null
    lateinit var kitabs: ArrayList<Kitab>
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
        var inflater = context
            ?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var popupWindow = PopupWindow(
            inflater.inflate(R.layout.category_sorted_kitabs, null, false),
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )
            holder.binding.fununFun.setOnClickListener {
                kitabs = ArrayList()
                var main_adapter = KitabAdapter(context, kitabs)
                popupWindow.showAtLocation(holder.binding.fununFun, Gravity.CENTER, 0, 0)
                popupWindow.contentView.findViewById<RecyclerView>(R.id.sorted_kitabs).adapter =
                    main_adapter
                var ori = context?.resources?.configuration?.orientation
                if (ori == Configuration.ORIENTATION_LANDSCAPE) {
                    popupWindow.contentView.findViewById<RecyclerView>(R.id.sorted_kitabs).layoutManager =
                        GridLayoutManager(context, 4)
                } else
                    popupWindow.contentView.findViewById<RecyclerView>(R.id.sorted_kitabs).layoutManager =
                        GridLayoutManager(context, 2)
                popupWindow.contentView.findViewById<androidx.appcompat.widget.SearchView>(R.id.category_search_kitab).setOnSearchClickListener(){
                    popupWindow.contentView.findViewById<TextView>(R.id.category_sorted_kitabs_kitab_name).visibility = View.GONE
                }
                popupWindow.contentView.findViewById<androidx.appcompat.widget.SearchView>(R.id.category_search_kitab).setOnCloseListener {
                    popupWindow.contentView.findViewById<TextView>(R.id.category_sorted_kitabs_kitab_name).visibility =
                        View.VISIBLE
                    return@setOnCloseListener false
                }
                if (FirebaseDatabase.getInstance().reference!= null || popupWindow.contentView.findViewById<androidx.appcompat.widget.SearchView>(R.id.category_search_kitab) == null) {
                    FirebaseDatabase.getInstance().reference.child("kitab")
                        .addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.exists()) {
                                    for (snapshot1 in snapshot.children) {
                                        var kitab = snapshot1.getValue(Kitab::class.java)
                                        if (kitab?.kitab_category == (holder.binding.fununFun.text)) {
                                            kitab?.kitab_id = snapshot1.key.toString()
                                            if (kitab != null) {
                                                kitabs.add(kitab)
                                            }
                                        }
                                    }
                                    main_adapter.notifyDataSetChanged()
                                    popupWindow.contentView.findViewById<androidx.appcompat.widget.SearchView>(R.id.category_search_kitab).queryHint = "Search Among " +kitabs.size +" kitabs"
                                }
                                if (kitabs.isEmpty()) {
                                    popupWindow.contentView.findViewById<TextView>(R.id.category_kitab_available).visibility =
                                        View.VISIBLE
                                    popupWindow.contentView.findViewById<RecyclerView>(R.id.sorted_kitabs).visibility =
                                        View.GONE
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                            }

                        })
                    popupWindow.contentView.findViewById<TextView>(R.id.category_sorted_kitabs_kitab_name).text =
                        holder.binding.fununFun.text
                }
                if (popupWindow.contentView.findViewById<androidx.appcompat.widget.SearchView>(R.id.category_search_kitab)
                    != null){
                    popupWindow.contentView.findViewById<androidx.appcompat.widget.SearchView>(R.id.category_search_kitab).setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(query: String): Boolean {
                            return false
                        }

                        override fun onQueryTextChange(newText: String): Boolean {
                            val searched_kitab: ArrayList<Kitab> = ArrayList<Kitab>()
                            for (`object` in kitabs) {
                                if (`object`.kitab_name.lowercase().contains(newText.lowercase(Locale.getDefault()))) {
                                    searched_kitab.add(`object`)
                                    popupWindow.contentView.findViewById<TextView>(R.id.category_kitab_available).visibility =
                                        View.GONE
                                    popupWindow.contentView.findViewById<RecyclerView>(R.id.sorted_kitabs).visibility =
                                        View.VISIBLE
                                }
                            }
                            if (searched_kitab.isEmpty()){
                                popupWindow.contentView.findViewById<TextView>(R.id.category_kitab_available).visibility =
                                    View.VISIBLE
                                popupWindow.contentView.findViewById<RecyclerView>(R.id.sorted_kitabs).visibility =
                                    View.GONE
                            }
                            val adapter = KitabAdapter(context, searched_kitab)
                            adapter.notifyDataSetChanged()
                            popupWindow.contentView.findViewById<RecyclerView>(R.id.sorted_kitabs).adapter = adapter
                            return false
                        }
                    })
                }
                popupWindow.contentView.findViewById<ImageView>(R.id.back_arrow)
                    .setOnClickListener {
                        popupWindow.dismiss()
                    }
        }
    }

    override fun getItemCount(): Int {
        var a = Fununs?.size
        return a!!
    }

}