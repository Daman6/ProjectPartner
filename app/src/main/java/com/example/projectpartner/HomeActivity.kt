package com.example.projectpartner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.projectpartner.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    lateinit var binding :ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(savedInstanceState==null){
            setFragment(LoginFragment())
        }


        binding.registerBtn.setOnClickListener {
            setFragment(RegisterFragment())
        }

        binding.loginBtn.setOnClickListener {
            setFragment(LoginFragment())
        }
    }

    private fun setFragment(fragment: Fragment) {
        val fragmentmanager = supportFragmentManager
        val fragmentTransaction = fragmentmanager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer,fragment)
        fragmentTransaction.commit()
    }

}