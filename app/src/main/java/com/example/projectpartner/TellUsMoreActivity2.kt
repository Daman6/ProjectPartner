package com.example.projectpartner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.projectpartner.databinding.ActivityTellUsMore2Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class TellUsMoreActivity2 : AppCompatActivity() {

    lateinit var binding: ActivityTellUsMore2Binding
    lateinit var usersDB : DatabaseReference
    lateinit var auth : FirebaseAuth
    var userCurrentSkill:String = ""
    private lateinit var currentUser : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTellUsMore2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser!!.uid
        usersDB = FirebaseDatabase.getInstance().getReference("Users")


        binding.toggleGrp.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked){
                when(checkedId){
                    R.id.androidbtn -> {
                        userCurrentSkill = "Android dev"
                        Toast.makeText(applicationContext,"Android",Toast.LENGTH_SHORT).show()
                    }
                    R.id.webbtn -> {
                        userCurrentSkill = "Web dev"
                        Toast.makeText(applicationContext,"web",Toast.LENGTH_SHORT).show()
                    }
                    R.id.reactbtn -> {
                        userCurrentSkill = "React dev"
                        Toast.makeText(applicationContext,"react",Toast.LENGTH_LONG).show()
                    }
                }
            }else{
                if (group.checkedButtonId==View.NO_ID){
                    userCurrentSkill = ""
                    Toast.makeText(applicationContext,"Nothing Selected,Please Select To \nMove On Next",Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.continueBtn.setOnClickListener {
            if(userCurrentSkill.isEmpty()){
                Toast.makeText(applicationContext,"Nothing Selected,Please Select To \nMove On Next",Toast.LENGTH_SHORT).show()
            }else{
                addDataToFirebase(userCurrentSkill)
            }
        }
    }

    private fun addDataToFirebase(userCurrentSkill : String) {
        val usermap = mapOf<String,String>(
            "userCurrentSkill" to userCurrentSkill
        )

        usersDB.child(currentUser).updateChildren(usermap).addOnSuccessListener {
            val i = Intent(applicationContext, TellUsMoreActivity3::class.java)
            startActivity(i)
            finish()
            Toast.makeText(applicationContext,"Done",Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(applicationContext,"Fail",Toast.LENGTH_SHORT).show()
        }
    }


}