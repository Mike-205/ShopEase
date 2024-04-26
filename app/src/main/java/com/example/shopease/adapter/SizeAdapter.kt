package com.example.shopease.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shopease.R


class SizeAdapter(private val sizeList: List<String>) : RecyclerView.Adapter<SizeAdapter.SizeViewHolder>() {

    inner class SizeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val sizeText: TextView = view.findViewById(R.id.sizeText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SizeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_size, parent, false)
        return SizeViewHolder(view)
    }

    override fun onBindViewHolder(holder: SizeViewHolder, position: Int) {
        val size = sizeList[position]
        holder.sizeText.text = size
    }

    override fun getItemCount() = sizeList.size
}