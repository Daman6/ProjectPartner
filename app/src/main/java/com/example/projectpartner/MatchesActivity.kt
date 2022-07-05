package com.example.projectpartner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectpartner.databinding.ActivityMatchesBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.zip.Inflater
import kotlin.collections.ArrayList

class MatchesActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMatchesBinding
    private lateinit var matchesAdapter : MatchesAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var currentUserUID: String
    lateinit var usersDB : DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMatchesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.shimmer.startShimmer()
        usersDB = FirebaseDatabase.getInstance().getReference("Users")

        currentUserUID = FirebaseAuth.getInstance().currentUser!!.uid
        binding.recyclerView.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        matchesAdapter = MatchesAdapter(getDataSetMatches(),this)
        binding.recyclerView.adapter = matchesAdapter
        val swipeGesture = object :SwipeGesture(this){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                when(direction){
                     ItemTouchHelper.LEFT->{

                         matchesAdapter.deleteItem(viewHolder.adapterPosition)
//                         val dialog = MatchesAdapter.CustomDialogFragment()
//                         dialog.show(supportFragmentManager,"customDailog")

                     }
                }
            }
        }
        val touchHelper = ItemTouchHelper(swipeGesture)
        touchHelper.attachToRecyclerView(binding.recyclerView)

        getUserMatchID()

        updateUserState("online")

        checkFriends()

    }
    private fun checkFriends(){
        val listDB = FirebaseDatabase.getInstance().getReference("Users").child(currentUserUID).child("connections").child("matches")
        listDB.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    binding.nofriendsTv.visibility = View.GONE
                }else{
                    binding.nofriendsTv.visibility = View.VISIBLE
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
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
        usersDB.child(currentUserUID).child("userStatus")
            .updateChildren(map)

    }

    override fun onStart() {
        super.onStart()
        updateUserState("online")
    }

    override fun onPause() {
        super.onPause()
        updateUserState("offline")
    }

    override fun onStop() {
        super.onStop()
        updateUserState("offline")

    }

    override fun onResume() {
        super.onResume()
        updateUserState("online")
    }

    override fun onDestroy() {
        super.onDestroy()
        updateUserState("offline")

    }

    private fun getUserMatchID() {
        binding.shimmer.stopShimmer()
        binding.shimmer.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
        val matchDB = FirebaseDatabase.getInstance().getReference("Users").child(currentUserUID).child("connections").child("matches")
        matchDB.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (match in snapshot.children) {
                        FetchMatchInformation(match.key)
                        updateUserState("online")
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }



    private fun FetchMatchInformation(key: String?) {

        val matchDB = FirebaseDatabase.getInstance().getReference("Users").child(key!!)
        matchDB.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val userUid = snapshot.key
                    var userEmail = ""
                    var firstName = ""
                    var profileImageUrl = ""
                    var type = ""
                    if (snapshot.hasChild("userStatus")){
                        type = snapshot.child("userStatus").child("type").value.toString()
                    }
                    if (snapshot.child("email").getValue() != null){
                        userEmail = snapshot.child("email").value.toString()
                    }
                    if (snapshot.child("firstName").getValue() != null){
                        firstName = snapshot.child("firstName").value.toString()
                    }
                    if (snapshot.child("profileImageUrl").getValue() != null){
                        profileImageUrl = snapshot.child("profileImageUrl").value.toString()
                    }
                    val obj : MatchesObject = MatchesObject(userUid,userEmail,firstName,profileImageUrl,type)
                    resultsMatches.add(obj)
                    matchesAdapter.notifyDataSetChanged()
                    updateUserState("online")
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private val resultsMatches = ArrayList<MatchesObject>()



    private fun getDataSetMatches(): ArrayList<MatchesObject> {
        return resultsMatches
    }
}