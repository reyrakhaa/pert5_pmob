package com.rakha.pert5_pmob

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StoryAdapter(private val stories: List<Story>) :
    RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

    class StoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivStoryProfile: ImageView = itemView.findViewById(R.id.ivStoryProfile)
        val tvStoryUsername: TextView = itemView.findViewById(R.id.tvStoryUsername)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.story, parent, false)
        return StoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = stories[position]
        holder.ivStoryProfile.setImageResource(story.profileImage)
        holder.tvStoryUsername.text = story.username
    }

    override fun getItemCount(): Int = stories.size
}
