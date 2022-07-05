package com.example.projectpartner.chats

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projectpartner.R
import com.example.projectpartner.chats.ChatActivity

class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
    var mMessage: TextView
    var mContainer: LinearLayout
    override fun onClick(view: View) {
    }

    init {
        itemView.setOnClickListener(this)
        mMessage = itemView.findViewById(R.id.message)
        mContainer = itemView.findViewById(R.id.container)
    }
}