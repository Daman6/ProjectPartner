package com.example.projectpartner

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.projectpartner.chats.ChatActivity
import com.example.projectpartner.ui.UserActivity
import de.hdodenhof.circleimageview.CircleImageView

class MyAdapter(var mCtx:Context,var resources:Int,var items:ArrayList<User>) :ArrayAdapter<User>(mCtx,resources,items) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val layoutInflater:LayoutInflater = LayoutInflater.from(mCtx)
        val view:View = layoutInflater.inflate(resources,parent, false)

        val userImage:CircleImageView = view.findViewById(R.id.userImage)
        val userSkill:TextView = view.findViewById(R.id.userSkill)
        val userName:TextView = view.findViewById(R.id.userName)
        val userClass:TextView = view.findViewById(R.id.userClass)
        val userYear:TextView = view.findViewById(R.id.userYear)
        val userState:TextView = view.findViewById(R.id.userState)
        val userLevel:TextView = view.findViewById(R.id.userLevel)
        val btn:TextView = view.findViewById(R.id.knowMoreBtn)


        val mItem: User = items[position]
        userName.text = mItem.firstName
        userSkill.text = mItem.userCurrentSkill
        userState.text = mItem.userState
        userYear.text = mItem.userYear
        userClass.text = mItem.userClass
        userLevel.text = mItem.userLevel
        Glide.with(context).load(mItem.profileImageUrl).into(userImage)

        btn.setOnClickListener {
//            val i = Intent(view.context, BigProfileActivity::class.java)
//            val b = Bundle()
//            b.putString("matchId", mItem.uid.toString())
//            i.putExtras(b)
//            view.context.startActivity(i)
            val i = Intent(view.context, UserActivity::class.java)
            i.putExtra("STRING_I_NEED", "daman6")
            view.context.startActivity(i)
        }
        return view
    }

}