package com.example.projectpartner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.projectpartner.databinding.ActivityTellUsMoreBinding
import com.example.projectpartner.databinding.ActivityUserBioBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UserBioActivity : AppCompatActivity() {
    lateinit var binding: ActivityUserBioBinding
    lateinit var usersDB : DatabaseReference
    lateinit var auth : FirebaseAuth
    private lateinit var currentUser : String
    var userBio:String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserBioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser!!.uid
        usersDB = FirebaseDatabase.getInstance().getReference("Users")

        binding.continueBtn.setOnClickListener {
            userBio = binding.userBio.text.toString()
            if(userBio.isEmpty()){
                Toast.makeText(applicationContext,"Please Select Your Birthday!!", Toast.LENGTH_SHORT).show()
            }else{
                addDataToFirebase(userBio)
            }
        }

    }

    private fun addDataToFirebase(userBio: String) {
        val usermap = mapOf<String,String>(
            "userBio" to userBio,
        )

        usersDB.child(currentUser).updateChildren(usermap).addOnSuccessListener {

            val i = Intent(this, TellUsMoreActivity2::class.java)
            startActivity(i)
            finish()
            Toast.makeText(this,"Done",Toast.LENGTH_SHORT).show()

        }.addOnFailureListener {
            Toast.makeText(this,"Fail",Toast.LENGTH_SHORT).show()
        }

    }
}