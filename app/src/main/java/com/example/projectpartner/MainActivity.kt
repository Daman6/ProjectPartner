package com.example.projectpartner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.projectpartner.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    lateinit var  binding : ActivityMainBinding
    lateinit var  mAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth =  FirebaseAuth.getInstance()
        val user = mAuth.currentUser

        supportActionBar?.hide()

        binding.logo.animate().apply {

            duration = 1000
            rotationYBy(360f)
            translationYBy(100f)
        }.start()

        Handler().postDelayed({
            if(user != null){
                val intent = Intent(this@MainActivity,ProfileActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                val intent = Intent(this@MainActivity,HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        },2000)


    }
}