package com.example.projectpartner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FeedbackAdapter(private val feedbacklist:ArrayList<feedbackObject>) :
    RecyclerView.Adapter<FeedbackAdapter.MyviewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyviewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.feebback_item,parent,false)
        return MyviewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyviewHolder, position: Int) {
        val currentFeed = feedbacklist[position]
        holder.feed.text = currentFeed.type
    }

    override fun getItemCount(): Int {
        return feedbacklist.size
    }

    class MyviewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val feed :TextView = itemView.findViewById(R.id.userfeedback)
    }

}