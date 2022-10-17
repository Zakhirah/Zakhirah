package com.talaba.zakhirah

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.talaba.zakhirah.Adapters.KitabAdapter
import com.talaba.zakhirah.databinding.ActivityKitabViewBinding
import com.talaba.zakhirah.databinding.ActivityProfileBinding
import com.talaba.zakhirah.models.Kitab
import com.talaba.zakhirah.models.User

class ProfileActivity : AppCompatActivity() {
    lateinit var binding: ActivityProfileBinding
    lateinit var uploadedkitabs:ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.progressBarSaved.visibility = View.VISIBLE
        binding.progressBarUpload.visibility = View.VISIBLE
        FirebaseDatabase.getInstance().reference
            .child("users")
            .child(FirebaseAuth.getInstance().uid.toString())
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        var user: User = snapshot.getValue(User::class.java)!!
                        binding.profileName.text = "Name : "+ user.user_username
                        binding.profileEmail.text = "Email : "   + user.user_email
                        binding.profilePhone.text = "Phone no. : "+user.user_phone.toString()
                        binding.profilePassword.text = "Password : "+user.user_password
                        binding.profileState.text =   "Current State : "+ user.user_state
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        var kitabsSaved = ArrayList<Kitab>()
        var adapter = KitabAdapter(this,kitabsSaved)
        binding.profileSaved.adapter = adapter
        FirebaseDatabase.getInstance().reference
            .child("users")
            .child(FirebaseAuth.getInstance().uid.toString())
            .child("saved")
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    kitabsSaved.clear()
                    if (snapshot.exists()){
                        binding.progressBarSaved.visibility = View.GONE
                        for (snapshot1 in snapshot.children) {
                            FirebaseDatabase.getInstance().reference
                                .child("kitab")
                                .child(snapshot1.key.toString())
                                .addValueEventListener(object :ValueEventListener{
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        if (snapshot.exists()){
                                                var kitab = Kitab()
                                                kitab.kitab_name = snapshot.child("kitab_name").value.toString()
                                                kitab.kitab_url = snapshot.child("kitab_url").value.toString()
                                                kitab.kitab_id = snapshot1.key.toString()
                                            if (kitab != null) {
                                                    kitabsSaved.add(kitab)
                                                }
                                            adapter.notifyDataSetChanged()
                                        }
                                    }
                                    override fun onCancelled(error: DatabaseError) {
                                        TODO("Not yet implemented")
                                    }
                                })
                        }
                    }
                    else{
                        binding.profileSaved.visibility = View.GONE
                        binding.profileSavedText.visibility = View.VISIBLE
                        binding.progressBarSaved.visibility = View.GONE
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        var kitabsUpload = ArrayList<Kitab>()
        var adapterup = KitabAdapter(this,kitabsUpload)
        binding.profileUploaded.adapter = adapterup
        FirebaseDatabase.getInstance().reference
            .child("kitab")
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        binding.progressBarUpload.visibility = View.GONE
                        for (snapshot1 in snapshot.children){
                            if (snapshot1.child("kitab_author").value.toString() == FirebaseAuth.getInstance().uid){
                                var kitab = Kitab()
                                kitab.kitab_name = snapshot1.child("kitab_name").value.toString()
                                kitab.kitab_url = snapshot1.child("kitab_url").value.toString()
                                kitab.kitab_id = snapshot1.key.toString()
                                if (kitab != null) {
                                    kitabsUpload.add(kitab)
                                }
                            }
                        }
                        adapterup.notifyDataSetChanged()
                    }
                    else{
                        binding.profileUploaded.visibility = View.GONE
                        binding.profileUploadText.visibility = View.VISIBLE
                        binding.progressBarUpload.visibility = View.GONE
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }
}