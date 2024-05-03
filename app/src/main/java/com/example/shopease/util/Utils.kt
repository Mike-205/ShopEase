package com.example.shopease.util

import FavoritesManager
import android.widget.ImageView

object Utils {
    fun setupFavoriteIconToggle(favoriteIcon: ImageView, itemId: Int, defaultIconResId: Int, redIconResId: Int, favoritesManager: FavoritesManager) {
        var isFavorite = favoritesManager.isFavorite(itemId)

        // Set initial icon
        favoriteIcon.setImageResource(if (isFavorite) redIconResId else defaultIconResId)

        favoriteIcon.setOnClickListener {
            isFavorite = !isFavorite
            favoritesManager.setFavorite(itemId, isFavorite)
            favoriteIcon.setImageResource(if (isFavorite) redIconResId else defaultIconResId)
        }
    }
}