package com.example.projectpartner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projectpartner.databinding.ActivityBigProfileBinding
import com.example.projectpartner.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class BigProfileActivity : AppCompatActivity() {
    private lateinit var binding : ActivityBigProfileBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var currentUser : String
    lateinit var usersDB : DatabaseReference
    private lateinit var matchId: String
    private lateinit var feedArrayList: ArrayList<feedbackObject>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBigProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        matchId = intent.extras!!.getString("matchId")!!
        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser!!.uid
        usersDB = FirebaseDatabase.getInstance().getReference("Users")

        binding.recycler.layoutManager = LinearLayoutManager(this)
        binding.recycler.setHasFixedSize(true)

        feedArrayList = arrayListOf<feedbackObject>()
        getuserData()
        getfeedback()
    }

    private fun getfeedback() {
//        usersDB.child(matchId).child("userfeedback").get().addOnSuccessListener {
//            if (it.exists()) {
//                var type = ""
//                var cuid = ""
//                type = it.child("type").value.toString()
//                cuid = it.child("userName").value.toString()
//                feedArrayList.add(feedbackObject(type, cuid))
//            }
//            binding.recycler.adapter = FeedbackAdapter(feedArrayList)
//        }
        usersDB.child(matchId).child("userfeedback").addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot.exists()) {
//                    arrayAdapter.notifyDataSetChanged()
//                    val uid = snapshot.child("userName").value.toString()
                    val type = snapshot.child("type").value.toString()
                    Log.d("type",type)
                    feedArrayList!!.add(feedbackObject(type))
                }
               binding.recycler.adapter = FeedbackAdapter(feedArrayList)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
        binding.recycler.adapter = FeedbackAdapter(feedArrayList)


    }

    private fun getuserData() {
        usersDB.child(matchId).get().addOnSuccessListener {
            if (it.exists()){
                val name = it.child("firstName").value
                val bio = it.child("userBio").value
                val level = it.child("userLevel").value
                val skill = it.child("userCurrentSkill").value
                val course = it.child("userClass").value
                val college = it.child("userCollege").value
                val year = it.child("userYear").value
                val st = it.child("userState").value
                val dob = it.child("userDob").value
                val img = it.child("profileImageUrl").value

                binding.userSkill.text = skill.toString()
                binding.userName.text= name.toString()
                binding.userBio.text = bio.toString()
                binding.userCol.text = college.toString()
                binding.userCl.text = course.toString()
                binding.userLev.text = level.toString()
                binding.userSt.text = st.toString()
                binding.userdob.text = dob.toString()
                binding.userY.text = year.toString()

                Glide.with(this).load(img).into(binding.userImage)



            }
        }
    }
}