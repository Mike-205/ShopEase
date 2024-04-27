// Package declaration
package com.example.shopease.adapter

// Import statements
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shopease.R

// SizeAdapter class that extends RecyclerView.Adapter
class SizeAdapter(private val sizeList: List<String>) : RecyclerView.Adapter<SizeAdapter.SizeViewHolder>() {

    // ViewHolder class for each item in the RecyclerView
    inner class SizeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val sizeText: TextView = view.findViewById(R.id.sizeText)
    }

    // Function to create a new ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SizeViewHolder {
        // Inflate the view for each item
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_size, parent, false)
        return SizeViewHolder(view)
    }

    // Function to bind data to a ViewHolder
    override fun onBindViewHolder(holder: SizeViewHolder, position: Int) {
        // Get the current size
        val size = sizeList[position]
        // Set the text of the sizeText TextView to the current size
        holder.sizeText.text = size
    }

    // Function to get the number of items in the RecyclerView
    override fun getItemCount() = sizeList.size
}