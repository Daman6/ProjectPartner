package com.example.projectpartner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.projectpartner.databinding.ActivitySettingBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SettingActivity : AppCompatActivity() {
    lateinit var binding : ActivitySettingBinding
    lateinit var usersDB : DatabaseReference
    private lateinit var currentUser : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentUser =  FirebaseAuth.getInstance().currentUser!!.uid
        usersDB = FirebaseDatabase.getInstance().getReference("Users")
        getinfo()

        binding.updateBtn.setOnClickListener {
           val uname = binding.nameeditlayout.text.toString()
           val ucollegename = binding.collegeNameeditlayout.text.toString()
           val ucoursename = binding.courseNameeditlayout.text.toString()
           val uyear = binding.collegeYeareditlayout.text.toString()
            updateFirebase(uname,ucollegename,ucoursename,uyear)
        }
    }

    private fun updateFirebase(uname: String, ucollegename: String, ucoursename: String, uyear: String) {
        val usermap = mapOf<String,String>(
            "firstName" to uname,
            "userCollege" to ucollegename,
            "userClass" to ucoursename,
            "userYear" to uyear,
        )

        usersDB.child(currentUser).updateChildren(usermap).addOnSuccessListener {
            Toast.makeText(this,"Updated", Toast.LENGTH_SHORT).show()

        }.addOnFailureListener {
            Toast.makeText(this,"Fail", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getinfo() {
        usersDB.child(currentUser).get().addOnSuccessListener {
            if (it.exists()){
                val name = it.child("firstName").value
                val college = it.child("userCollege").value
                val course = it.child("userClass").value
                val year = it.child("userYear").value
                val img = it.child("profileImageUrl").value

                binding.nameeditlayout.setText(name.toString())
                binding.collegeNameeditlayout.setText(college.toString())
                binding.courseNameeditlayout.setText(course.toString())
                binding.collegeYeareditlayout.setText(year.toString())

                Glide.with(this).load(img).into(binding.userProfile)


            }
        }
    }
}