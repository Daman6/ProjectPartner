package com.example.projectpartner.chats

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projectpartner.R

class ChatAdapter(private val chatList: List<ChatObject>, private val context: Context) : RecyclerView.Adapter<ChatViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val layoutView = LayoutInflater.from(parent.context).inflate(R.layout.item_chat, null, false)
        val lp = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutView.layoutParams = lp
        return ChatViewHolder(layoutView)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.mMessage.text = chatList[position].message
        if (chatList[position].currentUser) {
            holder.mMessage.gravity = Gravity.END
            holder.mMessage.setTextColor(Color.parseColor("#404040"))
            holder.mContainer.setBackgroundColor(Color.parseColor("#F4F4F4"))
        } else {
            holder.mMessage.gravity = Gravity.START
            holder.mMessage.setTextColor(Color.parseColor("#FFFFFF"))
            holder.mContainer.setBackgroundColor(Color.parseColor("#2DB4C8"))
            holder.mContainer.setBackgroundColor(Color.parseColor("#2DB4C8"))
        }
    }

    override fun getItemCount(): Int {
        return chatList.size
    }
}