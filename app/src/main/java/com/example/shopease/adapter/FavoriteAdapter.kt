package com.example.shopease.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shopease.R
import com.example.shopease.manager.FavoritesManager
import com.example.shopease.model.FavoriteModel

class FavoriteAdapter(private val favoriteItems: MutableList<FavoriteModel>) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    inner class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.productImage)
        val titleText: TextView = itemView.findViewById(R.id.productName)
        val priceText: TextView = itemView.findViewById(R.id.priceValue)
        val ratingText: TextView = itemView.findViewById(R.id.ratingValue)
        val favIcon: ImageView = itemView.findViewById(R.id.favouriteIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_favourites, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val favoriteItem = favoriteItems[position]
        holder.titleText.text = favoriteItem.name
        holder.priceText.text = "$${favoriteItem.price}"
        holder.ratingText.text = favoriteItem.rating.toString()
        holder.imageView.setImageResource(favoriteItem.imageResId)

        holder.favIcon.setOnClickListener {
            // Remove the item from the favorites list and the cache
            FavoritesManager.removeFavorite(favoriteItem.imageResId)

            // Remove the item from the list
            favoriteItems.removeAt(position)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount() = favoriteItems.size
}