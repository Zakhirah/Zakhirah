package com.talaba.zakhirah

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.talaba.zakhirah.Adapters.CategoryAdapter
import com.talaba.zakhirah.Adapters.KitabAdapter
import com.talaba.zakhirah.databinding.ActivityMainBinding
import com.talaba.zakhirah.models.Kitab
import java.util.*


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var database: FirebaseDatabase
    lateinit var kitabs : ArrayList<Kitab>
    lateinit var fununs : ArrayList<String>
    lateinit var main_adapter : KitabAdapter
    var cat :String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = FirebaseDatabase.getInstance()
        kitabs = ArrayList()
        var main_adapter_category : CategoryAdapter
        fununs = ArrayList()
        main_adapter_category = CategoryAdapter(this, fununs)
        main_adapter = KitabAdapter(this, kitabs)
        binding.mainKitab.adapter = main_adapter
        var ori = resources.configuration.orientation
        if (ori == Configuration.ORIENTATION_LANDSCAPE) {
                binding.mainKitab.layoutManager = GridLayoutManager(this, 4)
            } else
                binding.mainKitab.layoutManager = GridLayoutManager(this, 2)
        binding.mainCategory.adapter = main_adapter_category
        database.reference.child("category")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    fununs.clear()
                    if (snapshot.exists()){
                        for (snapshot1 in snapshot.children){
                            fununs.add(snapshot1.key.toString())
                        }
                            main_adapter_category.notifyDataSetChanged()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
        binding.progressBar.visibility = View.VISIBLE
//        database.reference.child("kitab")
//            .addValueEventListener( object : ValueEventListener{
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    kitabs.clear()
//                    if (snapshot.exists()) {
//                        binding.progressBar.visibility = View.GONE
//                        for (snapshot1 in snapshot.children) {
//                            var kitab : Kitab = snapshot1.getValue(Kitab::class.java)!!
//                            kitab.kitab_id = snapshot1.key.toString()
//                            kitabs.add(kitab)
//                        }
//                        main_adapter.notifyDataSetChanged()
//                    }
//                    else{
//                        binding.progressBar.visibility =View.GONE
//                        binding.mainKitab.visibility = View.GONE
//                        binding.mainKitabAvailable.visibility = View.VISIBLE
//                    }
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//
//                }
//            }
//                )
        binding.mainProfile.setOnClickListener{
            startActivity(Intent(this,ProfileActivity::class.java))
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        val item = menu!!.findItem(R.id.menu_search)
        val searchBar = item.actionView as androidx.appcompat.widget.SearchView
        if (database.reference != null) {
            database.reference.child("kitab").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    kitabs.clear()
                    if (snapshot.exists()) {
                        binding.progressBar.visibility = View.GONE
                        for (snapshot1 in snapshot.children) {
                            var kitab : Kitab = snapshot1.getValue(Kitab::class.java)!!
                            kitab.kitab_id = snapshot1.key.toString()
                            kitabs.add(kitab)
                        }
                        main_adapter.notifyDataSetChanged()
                        searchBar.queryHint = "Search among " + kitabs.size +" kitabs"
                    }
                    else{
                        binding.progressBar.visibility =View.GONE
                        binding.mainKitab.visibility = View.GONE
                        binding.mainKitabAvailable.visibility = View.VISIBLE
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }
        if (searchBar != null) {
            searchBar.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    search(newText)
                    return false
                }
            })
        }
        return super.onCreateOptionsMenu(menu)
    }
    private fun search(newText: String) {
        val searched_kitab: ArrayList<Kitab> = ArrayList<Kitab>()
        for (`object` in kitabs) {
            if (`object`.kitab_name.lowercase().contains(newText.lowercase(Locale.getDefault()))) {
                searched_kitab.add(`object`)
                binding.mainKitabAvailable.visibility = View.GONE
                binding.mainKitab.visibility = View.VISIBLE
            }
        }
        if (searched_kitab.isEmpty()){
            binding.mainKitabAvailable.visibility = View.VISIBLE
            binding.mainKitab.visibility = View.GONE
        }
        val adapter = KitabAdapter(this, searched_kitab)
        adapter.notifyDataSetChanged()
        binding.mainKitab.adapter = adapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.menu_upload ->
                startActivity(Intent(this,UploadKitabActivity::class.java))
            R.id.menu_category ->
                startActivity(Intent(this,UploadCategoryActivity::class.java))
            R.id.menu_signout ->{
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this,LoginActivity::class.java))
            }
            R.id.menu_search ->{

            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onBackPressed() {
        finishAffinity()
    }
}