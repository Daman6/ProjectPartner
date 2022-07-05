package com.example.projectpartner

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MatchesAdapter(val matchesList: ArrayList<MatchesObject>, private val context: Context) : RecyclerView.Adapter<MatchesViewHolder>() {
    lateinit var usersDB : DatabaseReference
    private lateinit var currentUserUID: String

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchesViewHolder {
        val layoutView = LayoutInflater.from(parent.context).inflate(R.layout.item_matches, null, false)
        val lp = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutView.layoutParams = lp
        return MatchesViewHolder(layoutView)
    }

    override fun onBindViewHolder(holder: MatchesViewHolder, position: Int) {
        holder.mMatchId.text = matchesList[position].uid
        holder.mMatchEmail.text = matchesList[position].email
        holder.mMatchName.text = matchesList[position].name
        if (matchesList[position].type.equals("online")){
            holder.mMatchUserStatus.visibility =View.VISIBLE
        }else{
            holder.mMatchUserStatus.visibility =View.INVISIBLE

        }
        Glide.with(context).load(matchesList[position].imageURL).into(holder.mMatchImage)
    }

    override fun getItemCount(): Int {
        return matchesList.size
    }

    fun deleteItem(i : Int){
        usersDB = FirebaseDatabase.getInstance().getReference("Users")
        currentUserUID = FirebaseAuth.getInstance().currentUser!!.uid

        val matchuid = matchesList[i].uid!!
        usersDB.child(matchuid).child("connections").child("matches").child(currentUserUID).removeValue().addOnCompleteListener {
        }.addOnFailureListener {
            Toast.makeText(context,"Not Deleted",Toast.LENGTH_LONG).show()
        }
        usersDB.child(matchuid).child("connections").child("yeps").child(currentUserUID).removeValue().addOnCompleteListener {
        }.addOnFailureListener {
            Toast.makeText(context,"Not Deleted",Toast.LENGTH_LONG).show()
        }

        usersDB.child(currentUserUID).child("connections").child("matches").child(matchuid).removeValue().addOnCompleteListener {
        }.addOnFailureListener {
            Toast.makeText(context,"Not Deleted",Toast.LENGTH_LONG).show()
        }
        usersDB.child(currentUserUID).child("connections").child("yeps").child(matchuid).removeValue().addOnCompleteListener {
            val intent = Intent(context,RatingActivity::class.java)
            val b = Bundle()
            b.putString("matchId", matchuid)
            intent.putExtras(b)
            context.startActivity(intent)
        }.addOnFailureListener {
            Toast.makeText(context,"Not Deleted",Toast.LENGTH_LONG).show()
        }


        Toast.makeText(context,"Deleted",Toast.LENGTH_LONG).show()

        matchesList.removeAt(i)

        notifyDataSetChanged()
    }
//    class CustomDialogFragment() : DialogFragment() {
//        override fun onCreateView(
//            inflater: LayoutInflater,
//            container: ViewGroup?,
//            savedInstanceState: Bundle?
//        ): View? {
//            val rootView: View = inflater.inflate(R.layout.custom_dialog_fragment,container,false)
//            val sb = rootView.findViewById<Button>(R.id.submitBtn)
//            val ratingGrp = rootView.findViewById<RadioGroup>(R.id.radiogrp)
//
//            sb.setOnClickListener {
//                val selectedID = ratingGrp.checkedRadioButtonId
//                val radio =rootView.findViewById<RadioButton>(selectedID)
//
//                val ratingResult = radio.text.toString()
//                dismiss()
//            }
//            return rootView
//
//
//        }
//    }

}