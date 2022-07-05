package com.example.projectpartner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.projectpartner.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.lorentzos.flingswipe.SwipeFlingAdapterView
import com.lorentzos.flingswipe.SwipeFlingAdapterView.onFlingListener
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding : ActivityProfileBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var currentUser : String
    private lateinit var arrayAdapter: MyAdapter
    private lateinit var filingAdapterView: SwipeFlingAdapterView
    lateinit var usersDB : DatabaseReference

    var list: ArrayList<User>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        userCurrentSkill = intent.extras!!.getString("currentSkill","React dev")!!

        binding.shimmer.startShimmer()

        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser!!.uid
        usersDB = FirebaseDatabase.getInstance().getReference("Users")
        usersDB.child(currentUser!!).child("connections").child("nope").child(currentUser).setValue(true)
        updateUserState("online")

        checkUserSkill()

        list = ArrayList()
        arrayAdapter = MyAdapter(this,R.layout.usercards,list!!)
        filingAdapterView = findViewById<SwipeFlingAdapterView>(R.id.swipe)
        filingAdapterView.adapter = arrayAdapter
        filingAdapterView.setFlingListener(object : onFlingListener{
            override fun removeFirstObjectInAdapter() {
                list!!.removeAt(0)
                arrayAdapter.notifyDataSetChanged()


            }

            override fun onLeftCardExit(p0: Any?) {
                val obj: User = p0 as User
                val userUid = obj.uid
                usersDB.child(userUid!!).child("connections").child("nope").child(currentUser).setValue(true)
                arrayAdapter.notifyDataSetChanged()

            }

            override fun onRightCardExit(p0: Any?) {
                val obj: User = p0 as User
                val userUid = obj.uid
                isConnectMatch(userUid!!)
                usersDB.child(userUid).child("connections").child("yeps").child(currentUser).setValue(true)
                arrayAdapter.notifyDataSetChanged()

            }

            override fun onAdapterAboutToEmpty(p0: Int) {
            }

            override fun onScroll(p0: Float) {
            }

        })

        binding.dislikeBtn.setOnClickListener {
            filingAdapterView.topCardListener.selectLeft()
            arrayAdapter.notifyDataSetChanged()

        }
        binding.likeBtn.setOnClickListener {
            filingAdapterView.topCardListener.selectRight()
            arrayAdapter.notifyDataSetChanged()

        }

//        getUser()

        binding.logoutBtn.setOnClickListener {
            logoutUser()
        }
        binding.matchesBtn.setOnClickListener {
            startActivity(Intent(this,MatchesActivity::class.java))
        }
        binding.settingBtn.setOnClickListener {
            startActivity(Intent(this,SettingActivity::class.java))

        }



    }

    private fun isConnectMatch(userId:String){
        val currentUserConnectionDB:DatabaseReference = usersDB.child(currentUser).child("connections").child("yeps").child(userId)
        currentUserConnectionDB.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    Toast.makeText(applicationContext,"Connection made",Toast.LENGTH_LONG).show()
                    val key = FirebaseDatabase.getInstance().getReference("Chat").push().key
                    usersDB.child(snapshot.key!!).child("connections").child("matches").child(currentUser).child("ChatId").setValue(key)
                    usersDB.child(currentUser).child("connections").child("matches").child(snapshot.key!!).child("ChatId").setValue(key)

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }

    private fun logoutUser() {
        updateUserState("offline")
        auth = FirebaseAuth.getInstance()
        auth.signOut()
        startActivity(Intent(this@ProfileActivity,HomeActivity::class.java))
        finish()
    }



    private fun getUser() {
////        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
//        database = FirebaseDatabase.getInstance().getReference("Users")
//
//        database.child(currentUser.toString()).get().addOnSuccessListener {
//            if (it.exists()) {
//                val firstName = it.child("firstName").value
//                val email = it.child("email").value
//
//                binding.textView.text =
//                    firstName.toString()  + email.toString()
//            } else {
//                Toast.makeText(this, "Data not Avaiable", Toast.LENGTH_SHORT).show()
//            }
//        }
    }

    var userSkill: String? = null
    var skillUserLookingFor: String? = null

    private fun checkUserSkill() {
//        val currentUser = FirebaseAuth.getInstance().currentUser
        val userDb = usersDB.child(currentUser)
        userDb.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    if (snapshot.child("userCurrentSkill").value != null) {
                        userSkill =  snapshot.child("userCurrentSkill").value.toString()
                        when(userSkill){
                            "Android dev" ->  {skillUserLookingFor = "Android dev" }
                            "Web dev" -> {skillUserLookingFor = "Web dev" }
                            "React dev" -> {skillUserLookingFor = "React dev" }

                        }
                            getOppositeUserSkill()
                            arrayAdapter.notifyDataSetChanged()

//                        if(list!!.size == 0){
//                            binding.noUserImg.visibility = View.VISIBLE
//                            binding.textView2.visibility = View.VISIBLE
//                            Toast.makeText(applicationContext,"NO MORE USERS",Toast.LENGTH_LONG).show()
//                        }else{
//                            binding.noUserImg.visibility = View.GONE
//                            binding.textView2.visibility = View.GONE
//                        }

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }



    private fun getOppositeUserSkill(){

        binding.shimmer.stopShimmer()
        binding.shimmer.visibility = View.GONE
        binding.swipe.visibility  = View.VISIBLE
        arrayAdapter.notifyDataSetChanged()
        usersDB.addChildEventListener(object:ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {


                    if (snapshot.exists() && !snapshot.child("connections").child("nope").hasChild(currentUser) && !snapshot.child("connections").child("yeps").hasChild(currentUser) && snapshot.child("userCurrentSkill").getValue().toString().equals(skillUserLookingFor)){

                        val uid = snapshot.child("uid").value.toString()
                        val fname = snapshot.child("firstName").value.toString()
                        val em = snapshot.child("email").value.toString()
                        val pic = snapshot.child("profileImageUrl").value.toString()
                        val uclass = snapshot.child("userClass").value.toString()
                        val year = snapshot.child("userYear").value.toString()
                        val state = snapshot.child("userState").value.toString()
                        val skill = snapshot.child("userCurrentSkill").value.toString()
                        val level = snapshot.child("userLevel").value.toString()
                        list!!.add(User(uid,fname,em,pic,skill,uclass,state,year,level))
                        arrayAdapter.notifyDataSetChanged()
                        updateUserState("online")


                }

            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

                if (snapshot.exists() && !snapshot.child("connections").child("nope").hasChild(currentUser) && !snapshot.child("connections").child("yeps").hasChild(currentUser) && snapshot.child("userCurrentSkill").getValue().toString().equals(skillUserLookingFor)){
//                    arrayAdapter.notifyDataSetChanged()
                    val uid = snapshot.child("uid").value.toString()
                    val fname = snapshot.child("firstName").value.toString()
                    val em = snapshot.child("email").value.toString()
                    val pic = snapshot.child("profileImageUrl").value.toString()
                    val uclass = snapshot.child("userClass").value.toString()
                    val year = snapshot.child("userYear").value.toString()
                    val state = snapshot.child("userState").value.toString()
                    val skill = snapshot.child("userCurrentSkill").value.toString()
                    val level = snapshot.child("userLevel").value.toString()
                    list!!.add(User(uid,fname,em,pic,skill,uclass,state,year,level))
                    arrayAdapter.notifyDataSetChanged()
                }
            }
            override fun onChildRemoved(snapshot: DataSnapshot) {
                if (snapshot.exists() && !snapshot.child("connections").child("nope").hasChild(currentUser) && !snapshot.child("connections").child("yeps").hasChild(currentUser) && snapshot.child("userCurrentSkill").getValue().toString().equals(skillUserLookingFor)){

//                    arrayAdapter.notifyDataSetChanged()
                    val uid = snapshot.child("uid").value.toString()
                    val fname = snapshot.child("firstName").value.toString()
                    val em = snapshot.child("email").value.toString()
                    val pic = snapshot.child("profileImageUrl").value.toString()
                    val uclass = snapshot.child("userClass").value.toString()
                    val year = snapshot.child("userYear").value.toString()
                    val state = snapshot.child("userState").value.toString()
                    val skill = snapshot.child("userCurrentSkill").value.toString()
                    val level = snapshot.child("userLevel").value.toString()
                    list!!.add(User(uid,fname,em,pic,skill,uclass,state,year,level))
                    arrayAdapter.notifyDataSetChanged()
                }
            }
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun updateUserState(state: String){

        val saveCurrentDate: String
        val saveCurrentTime :String

        val calForDate = Calendar.getInstance()
        val currentDate = SimpleDateFormat("MM dd, yyyy")
        saveCurrentDate = currentDate.format(calForDate.time)

        val calForTime = Calendar.getInstance()
        val currentTime = SimpleDateFormat("hh:mm a")
        saveCurrentTime = currentTime.format(calForTime.time)

        val map = mapOf<String,String>(
            "time" to saveCurrentTime,
            "date" to saveCurrentDate,
            "type" to state)
        usersDB.child(currentUser).child("userStatus")
            .updateChildren(map)

    }

    override fun onStart() {
        super.onStart()
        updateUserState("online")
    }

    override fun onPause() {
        super.onPause()
        updateUserState("online")
    }

    override fun onResume() {
        super.onResume()
        updateUserState("online")
    }

    override fun onDestroy() {
        super.onDestroy()
        updateUserState("offline")

    }

    override fun onStop() {
        super.onStop()
        updateUserState("offline")

    }
}