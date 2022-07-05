package com.example.projectpartner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.projectpartner.databinding.ActivityRuleBinding
import com.example.projectpartner.databinding.ActivityTellUsMore3Binding

class RuleActivity : AppCompatActivity() {
    lateinit var binding: ActivityRuleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rule)
        binding = ActivityRuleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.continueBtn.setOnClickListener {
            val i = Intent(applicationContext, ProfileActivity::class.java)
            startActivity(i)
            finish()
        }
    }
}