package com.talaba.zakhirah

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.shockwave.pdfium.PdfDocument
import com.shockwave.pdfium.PdfiumCore
import com.talaba.zakhirah.Adapters.CategoryAdapter
import com.talaba.zakhirah.Adapters.KitabAdapter
import com.talaba.zakhirah.databinding.ActivityMainBinding
import com.talaba.zakhirah.databinding.SampleFununBinding
import com.talaba.zakhirah.models.Kitab


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
        database.reference.child("kitab")
            .addValueEventListener( object : ValueEventListener{
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
                    }
                    else{
                        binding.progressBar.visibility =View.GONE
                        binding.mainKitab.visibility = View.GONE
                        binding.mainKitabAvailable.visibility = View.VISIBLE
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            }
                )
        binding.mainProfile.setOnClickListener{
            startActivity(Intent(this,ProfileActivity::class.java))
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
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
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onBackPressed() {
        finishAffinity()
    }
}