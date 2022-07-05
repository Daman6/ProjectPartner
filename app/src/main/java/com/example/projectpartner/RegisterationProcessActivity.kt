package com.example.projectpartner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.projectpartner.adapters.ViewPagerAdapter
import com.example.projectpartner.databinding.ActivityRegisterationProcessBinding
import com.google.android.material.tabs.TabLayoutMediator

class RegisterationProcessActivity : AppCompatActivity() {

    lateinit var binding :ActivityRegisterationProcessBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterationProcessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val vadapter = ViewPagerAdapter(supportFragmentManager,lifecycle)

        binding.viewpager2.adapter = vadapter
        binding.viewpager2.isUserInputEnabled = false

        TabLayoutMediator(binding.tablayout,binding.viewpager2){tab,position->
            when(position){
                0->{
                    tab.text = "Get"
                }
                1->{
                    tab.text = "Set"
                }
                2->{
                    tab.text = "Go"
                }
            }
        }.attach()


    }
}