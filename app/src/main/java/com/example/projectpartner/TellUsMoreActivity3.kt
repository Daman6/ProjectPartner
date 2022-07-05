package com.example.projectpartner

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.projectpartner.databinding.ActivityTellUsMore3Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class TellUsMoreActivity3 : AppCompatActivity() {
    lateinit var binding: ActivityTellUsMore3Binding
    lateinit var usersDB : DatabaseReference
    lateinit var auth : FirebaseAuth
    var userLevel:String = ""
    private lateinit var currentUser : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTellUsMore3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser!!.uid
        usersDB = FirebaseDatabase.getInstance().getReference("Users")

        val classes = resources.getStringArray(R.array.Classes)
        val college = resources.getStringArray(R.array.College)
        val state = resources.getStringArray(R.array.State)
        val year = resources.getStringArray(R.array.Year)
        val arrayAdapter = ArrayAdapter(this ,R.layout.dropdownmenu,classes)
        val arrayAdapter2 = ArrayAdapter(this ,R.layout.dropdownmenu,college)
        val arrayAdapter3 = ArrayAdapter(this ,R.layout.dropdownmenu,state)
        val arrayAdapter4 = ArrayAdapter(this ,R.layout.dropdownmenu,year)
        binding.autoComplete.setAdapter(arrayAdapter)
        binding.autoCompleteCollege.setAdapter(arrayAdapter2)
        binding.autoCompleteState.setAdapter(arrayAdapter3)
        binding.autoCompleteYear.setAdapter(arrayAdapter4)


        binding.autoComplete.setOnItemClickListener{parent,view,position,id->
            val selectedItem = parent.getItemAtPosition(position).toString()
        }
        binding.autoCompleteCollege.setOnItemClickListener{parent,view,position,id->
            val selectedItem = parent.getItemAtPosition(position).toString()
        }
        binding.autoCompleteYear.setOnItemClickListener{parent,view,position,id->
            val selectedItem = parent.getItemAtPosition(position).toString()
        }
        binding.autoCompleteState.setOnItemClickListener{parent,view,position,id->
            val selectedItem = parent.getItemAtPosition(position).toString()
        }

        binding.userLevel.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked){
                when(checkedId){
                    R.id.rookiebtn -> {
                        userLevel = "Rookie"
                    }
                    R.id.mediumbtn -> {
                        userLevel = "Medium"
                    }
                    R.id.probtn -> {
                        userLevel = "Pro"
                    }
                }
            }else{
                if (group.checkedButtonId== View.NO_ID){
                    userLevel = ""
                    Toast.makeText(applicationContext,"skill level not selected!!",Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.continueBtn.setOnClickListener {
//            val userCollege = binding.userCollege.text.toString()
//            val userClass = binding.userClass.text.toString()
//            val userState = binding.userState.text.toString()
//            val userYear = binding.userYear.text.toString()
//            if (userClass.isEmpty()&&userCollege.isEmpty()&&userState.isEmpty()&&userYear.isEmpty()&&userLevel.isEmpty()){
//                Toast.makeText(this,"Your left something above",Toast.LENGTH_SHORT).show()
//            }else{
//                addDataToFirebase(userCollege,userClass,userState,userYear,userLevel)
//            }
            if (binding.autoComplete.text.isEmpty()) {
                Toast.makeText(this, "Please select course", Toast.LENGTH_SHORT).show();
            }
            else if(binding.autoCompleteCollege.text.isEmpty()) {
                Toast.makeText(this, "Please select college", Toast.LENGTH_SHORT).show();
            }
            else if (binding.autoCompleteYear.text.isEmpty()) {
                Toast.makeText(this, "Please select year", android.widget.Toast.LENGTH_SHORT).show();
            }
            else if (binding.autoCompleteState.text.isEmpty()) {
                Toast.makeText(this, "Please select state", Toast.LENGTH_SHORT).show();
            }
            else {
                val userCollege = binding.autoCompleteCollege.text.toString()
            val userClass = binding.autoComplete.text.toString()
            val userState = binding.autoCompleteState.text.toString()
            val userYear = binding.autoCompleteYear.text.toString()
                addDataToFirebase(userCollege,userClass,userState,userYear,userLevel)
            }
        }



    }

    private fun addDataToFirebase(userCollege:String,userClass:String,userState:String,userYear:String,userLevel:String) {
        val usermap = mapOf<String,String>(
            "userCollege" to userCollege,
            "userClass" to userClass,
            "userState" to userState,
            "userYear" to userYear,
            "userLevel" to userLevel
        )

        usersDB.child(currentUser).updateChildren(usermap).addOnSuccessListener {
            val i = Intent(applicationContext, RuleActivity::class.java)
            startActivity(i)
        }.addOnFailureListener {
        }
    }
}