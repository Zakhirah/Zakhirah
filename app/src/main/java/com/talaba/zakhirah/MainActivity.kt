package com.talaba.zakhirah

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.talaba.zakhirah.databinding.ActivityMainBinding
import com.talaba.zakhirah.models.Kitab

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var database: FirebaseDatabase
    lateinit var kitabs : ArrayList<Kitab>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = FirebaseDatabase.getInstance()
        var main_adapter : KitabAdapter
        kitabs = ArrayList()
        main_adapter = KitabAdapter(this, kitabs)
        binding.mainKitab.adapter = main_adapter
        database.reference.child("kitab")
            .addValueEventListener( object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    kitabs.clear()
                    if (snapshot.exists()) {
                        for (snapshot1 in snapshot.children) {
                            var kitab : Kitab = snapshot1.getValue(Kitab::class.java)!!
                            kitabs.add(kitab)
                        }
                        main_adapter.notifyDataSetChanged()
                    }
                    else{
                        Toast.makeText(applicationContext,"No Kitab available...",Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            }
                )

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.menu_upload ->
                startActivity(Intent(this,UploadKitabActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onBackPressed() {
        finishAffinity()
    }
}