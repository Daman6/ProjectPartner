package com.example.projectpartner

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.DatePicker
import android.widget.Toast
import com.example.projectpartner.databinding.ActivityTellUsMoreBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class TellUsMoreActivity : AppCompatActivity() , DatePickerDialog.OnDateSetListener{

    lateinit var binding: ActivityTellUsMoreBinding
    lateinit var usersDB : DatabaseReference
    lateinit var auth : FirebaseAuth
    var userDob:String = ""
    private lateinit var currentUser : String

    var day = 0
    var month = 0
    var year = 0
    var savedDay = 0
    var savedMonth = 0
    var savedYear = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTellUsMoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser!!.uid
        usersDB = FirebaseDatabase.getInstance().getReference("Users")

        pickDate()

        binding.continueBtn.setOnClickListener {
            if(userDob.isEmpty()){
                Toast.makeText(applicationContext,"Please Select Your Birthday!!",Toast.LENGTH_SHORT).show()
            }else{
                addDataToFirebase(userDob)
            }
        }
    }

    fun getDateCalenedar(){
        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
    }

    fun pickDate(){
        binding.datebtn.setOnClickListener {
            getDateCalenedar()
            DatePickerDialog(this, this, year, month, day).show()
        }
    }


    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        savedDay= p3
        savedMonth= p2
        savedYear= p1
        userDob = "$savedDay/$savedMonth/$savedYear"
        binding.bdayTv.text = userDob
    }

    private fun addDataToFirebase(userDob:String) {
        val usermap = mapOf<String,String>(
            "userDob" to userDob,
        )

        usersDB.child(currentUser).updateChildren(usermap).addOnSuccessListener {
            usersDB.child(currentUser).child("connections").child("nope").child(currentUser).setValue(true)
                .addOnSuccessListener {
                    val i = Intent(this, UserBioActivity::class.java)
                    startActivity(i)
                    finish()
                    Toast.makeText(this,"Done",Toast.LENGTH_SHORT).show()
                }
        }.addOnFailureListener {
            Toast.makeText(this,"Fail",Toast.LENGTH_SHORT).show()
        }
    }
}
