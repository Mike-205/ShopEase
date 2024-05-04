package com.example.shopease.util

import com.example.shopease.manager.FavoritesManager
import com.example.shopease.model.FavoriteModel
import android.widget.ImageView
import com.example.shopease.data.FavoriteData

object Utils {
    fun setupFavoriteIconToggle(
        favoriteIcon: ImageView,
        itemId: Int,
        defaultIconResId: Int,
        redIconResId: Int,
        favoritesManager: FavoritesManager,
        name: String?,
        price: Double,
        rating: Double,
    ) {
        var isFavorite = favoritesManager.isFavorite(itemId)

        // Set initial icon
        favoriteIcon.setImageResource(if (isFavorite) redIconResId else defaultIconResId)
        favoriteIcon.setOnClickListener {
            isFavorite = !isFavorite
            favoritesManager.setFavorite(itemId, isFavorite)
            favoriteIcon.setImageResource(if (isFavorite) redIconResId else defaultIconResId)

            // Create FavoriteModel when item is marked as favorite
            if (isFavorite) {
                val favoriteItem = FavoriteModel(name, price, rating, itemId)
                FavoriteData.favoriteItems.add(favoriteItem)
            } else {
                val itemToRemove = FavoriteData.favoriteItems.find { it.name == name && it.price == price && it.rating == rating && it.imageResId == itemId }
                itemToRemove?.let { FavoriteData.favoriteItems.remove(it) }
            }
        }
    }
}