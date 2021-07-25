package com.example.fragmenttest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProjectsAdapter(val flowerList: Array<String>) :
    RecyclerView.Adapter<ProjectsAdapter.FlowerViewHolder>() {

    // Describes an item view and its place within the RecyclerView
    class FlowerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val flowerTextView: TextView = itemView.findViewById(R.id.flower_text)

        fun bind(word: String) {
            flowerTextView.text = word
        }
    }

    // Returns a new ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlowerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclelist_item, parent, false)

        return FlowerViewHolder(view)
    }

    // Returns size of data list
    override fun getItemCount(): Int {
        return flowerList.size
    }

    // Displays data at a certain position
    override fun onBindViewHolder(holder: FlowerViewHolder, position: Int) {
        holder.bind(flowerList[position])
    }
}