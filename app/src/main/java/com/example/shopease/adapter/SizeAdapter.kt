package com.example.shopease.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.shopease.R

class SizeAdapter(private val sizes: List<String>) : RecyclerView.Adapter<SizeAdapter.ViewHolder>() {
    var selectedSize: String? = null
    private var lastSelectedPosition = -1

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val sizeTextView: TextView = view.findViewById(R.id.sizeText)
        val sizeLayout: ConstraintLayout = view.findViewById(R.id.sizeLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_size, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.sizeTextView.text = sizes[position]
        holder.sizeTextView.setOnClickListener {
            lastSelectedPosition = position
            selectedSize = sizes[position]
            notifyDataSetChanged()
        }

        if (lastSelectedPosition == position) {
            holder.sizeLayout.setBackgroundResource(R.drawable.purple_border)
        } else {
            holder.sizeLayout.setBackgroundResource(R.drawable.cart_icon)
        }
    }

    override fun getItemCount() = sizes.size
}