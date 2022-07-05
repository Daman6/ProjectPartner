package com.example.projectpartner

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projectpartner.chats.ChatActivity
import de.hdodenhof.circleimageview.CircleImageView

class MatchesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
    var mMatchId: TextView
    var mMatchEmail: TextView
    var mMatchName: TextView
    var mMatchImage: CircleImageView
    var mMatchUserStatus: ImageView

    override fun onClick(view: View) {

        val i = Intent(view.context, ChatActivity::class.java)
        val b = Bundle()
        b.putString("matchId", mMatchId.text.toString())
        i.putExtras(b)
        view.context.startActivity(i)
    }

    init {
        itemView.setOnClickListener(this)
        mMatchId = itemView.findViewById<View>(R.id.matchId) as TextView
        mMatchEmail = itemView.findViewById<View>(R.id.matchEmail) as TextView
        mMatchName = itemView.findViewById<View>(R.id.matchName) as TextView
        mMatchImage = itemView.findViewById<View>(R.id.userImage) as CircleImageView
        mMatchUserStatus = itemView.findViewById<View>(R.id.userStatus) as ImageView
    }
}