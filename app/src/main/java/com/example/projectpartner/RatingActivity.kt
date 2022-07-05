package com.example.projectpartner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.projectpartner.databinding.ActivityRatingBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RatingActivity : AppCompatActivity() {
    lateinit var binding : ActivityRatingBinding
    private lateinit var currentUserUID: String
    lateinit var usersDB : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRatingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val matchuserid = intent.extras!!.getString("matchId")!!
        Log.d("main",matchuserid)

        usersDB = FirebaseDatabase.getInstance().getReference("Users")
        currentUserUID = FirebaseAuth.getInstance().currentUser!!.uid

        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.edittext_layout, null)
        val eidtext = dialogLayout.findViewById<EditText>(R.id.editText)

        with(builder){
            setTitle("FeedBack Form!")
            setPositiveButton("Submit"){dialog,which ->
                val result = eidtext.text.toString()
                val usermap = mapOf<String,String>(
                    "type" to result,
                    "userName" to currentUserUID
                )
                usersDB.child(matchuserid).child("userfeedback").child(currentUserUID).updateChildren(usermap).addOnSuccessListener {
                    val i = Intent(applicationContext, ProfileActivity::class.java)
                    startActivity(i)
                    finish()
                    Toast.makeText(applicationContext,"Done", Toast.LENGTH_SHORT).show()
                }
            }
            setView(dialogLayout)
            show()
        }
    }
}