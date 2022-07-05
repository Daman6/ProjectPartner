package com.example.projectpartner.chats

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.projectpartner.MatchesAdapter
import com.example.projectpartner.MatchesObject
import com.example.projectpartner.R
import com.example.projectpartner.databinding.ActivityChatBinding
import com.example.projectpartner.databinding.ActivityMatchesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChatActivity : AppCompatActivity() {

    private lateinit var binding : ActivityChatBinding
    private lateinit var chatAdapter : ChatAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var currentUserUID: String
    private lateinit var matchId: String

    private lateinit var chatId: String
    var mDatabaseUser: DatabaseReference? = null
    var mDatabaseChat: DatabaseReference? = null
    lateinit var usersDB : DatabaseReference



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        currentUserUID = FirebaseAuth.getInstance().currentUser!!.uid
        usersDB = FirebaseDatabase.getInstance().getReference("Users")

        matchId = intent.extras!!.getString("matchId")!!

        mDatabaseUser = FirebaseDatabase.getInstance().reference.child("Users").child(currentUserUID).child("connections").child("matches").child(matchId).child("ChatId")
        mDatabaseChat = FirebaseDatabase.getInstance().reference.child("Chat")

        getChatId()

        binding.recyclerView.setHasFixedSize(false)
        layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        chatAdapter = ChatAdapter(getDataSetChats(),this)
        binding.recyclerView.adapter = chatAdapter

        binding.send.setOnClickListener {
            sendMessage()
        }
        updateUserState("online")

        getUserProfile()
        setSupportActionBar(binding.mytoolbar)

    }

    private fun getUserProfile() {
        usersDB.child(matchId).get().addOnSuccessListener {
            if(it.exists()){
                val name = it.child("firstName").value
                val img = it.child("profileImageUrl").value
                binding.userName.text = name.toString()
                Glide.with(this).load(img).into(binding.userImage)
            }
        }
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

    private fun sendMessage() {
        val messageText = binding.message.text.toString()

        if (!messageText.isEmpty()){
            val newMessageDB : DatabaseReference = mDatabaseChat!!.push()

            val chatMap = mapOf<String,String>(
                "createdByUser" to currentUserUID,
                "text"  to messageText
            )
            newMessageDB.setValue(chatMap)
        }
        binding.message.text = null

    }

    private fun getChatId() {
        mDatabaseUser!!.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    chatId = snapshot.value.toString()
                    mDatabaseChat = mDatabaseChat!!.child(chatId)
                    getChatMessage()
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun getChatMessage() {
        mDatabaseChat!!.addChildEventListener(object : ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot.exists()) {
                    var message: String? = null
                    var createdByUser: String? = null
                    if (snapshot.child("text").value != null) {
                        message = snapshot.child("text").value.toString()
                    }
                    if (snapshot.child("createdByUser").value != null) {
                        createdByUser = snapshot.child("createdByUser").value.toString()
                    }
                    if (message != null && createdByUser != null) {
                        var currentUserBoolean = false
                        if (createdByUser.equals(currentUserUID)) {
                            currentUserBoolean = true
                        }
                        val newMessage = ChatObject(message, currentUserBoolean)
                        resultsChats.add(newMessage)
                        chatAdapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        });
    }

    private val resultsChats = ArrayList<ChatObject>()
    private fun getDataSetChats(): List<ChatObject> {
        return resultsChats
    }
}