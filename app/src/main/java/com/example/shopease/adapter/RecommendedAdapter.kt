package com.example.shopease.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shopease.R
import com.example.shopease.model.RecommendedModel

class RecommendedAdapter(private val recommendedList: List<RecommendedModel>) : RecyclerView.Adapter<RecommendedAdapter.RecommendedViewHolder>() {

    class RecommendedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.productImage)
        val nameView: TextView = itemView.findViewById(R.id.productName)
        val priceView: TextView = itemView.findViewById(R.id.priceValue)
        val ratingView: TextView = itemView.findViewById(R.id.ratingValue)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendedViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_recommended, parent, false)
        return RecommendedViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecommendedViewHolder, position: Int) {
        val currentItem = recommendedList[position]
        holder.imageView.setImageResource(currentItem.imageResId)
        holder.nameView.text = currentItem.name
        holder.priceView.text = "$${currentItem.price}"
        holder.ratingView.text = currentItem.rating.toString()
    }

    override fun getItemCount() = recommendedList.size
}