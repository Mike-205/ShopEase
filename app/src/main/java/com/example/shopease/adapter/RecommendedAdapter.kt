// Package declaration
package com.example.shopease.adapter

// Import statements
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shopease.MainActivity
import com.example.shopease.activity.DetailActivity
import com.example.shopease.R
import com.example.shopease.model.RecommendedModel

// RecommendedAdapter class that extends RecyclerView.Adapter
class RecommendedAdapter(
    private val recommendedList: List<RecommendedModel>,
    private val context: Context
) : RecyclerView.Adapter<RecommendedAdapter.RecommendedViewHolder>() {

    // ViewHolder class for each item in the RecyclerView
    class RecommendedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.productImage)
        val nameView: TextView = itemView.findViewById(R.id.productName)
        val priceView: TextView = itemView.findViewById(R.id.priceValue)
        val ratingView: TextView = itemView.findViewById(R.id.ratingValue)
    }

    // Function to create a new ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendedViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_recommended, parent, false)
        return RecommendedViewHolder(itemView)
    }

    // Function to bind data to a ViewHolder
    override fun onBindViewHolder(holder: RecommendedViewHolder, position: Int) {
        val currentItem = recommendedList[position]
        holder.imageView.setImageResource(currentItem.imageResId)
        holder.nameView.text = currentItem.name
        holder.priceView.text = "$${currentItem.price}"
        holder.ratingView.text = currentItem.rating.toString()

        // Set the click listener for the item view
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            intent.putExtra("name", currentItem.name)
            intent.putExtra("imageResId", currentItem.imageResId)
            intent.putExtra("price", currentItem.price)
            intent.putExtra("rating", currentItem.rating)
            holder.itemView.context.startActivity(intent)
        }
    }

    // Function to get the number of items in the RecyclerView
    override fun getItemCount() = recommendedList.size
}