package com.example.shopease.manager

import android.content.Context
import android.content.SharedPreferences

interface FavoritesObserver {
    fun onFavoritesUpdated()
}

object FavoritesManager {
    private lateinit var sharedPreferences: SharedPreferences
    private val observers = mutableListOf<FavoritesObserver>()

    fun initialize(context: Context) {
        sharedPreferences = context.getSharedPreferences("favorites", Context.MODE_PRIVATE)
    }

    fun isFavorite(itemId: Int): Boolean {
        return sharedPreferences.getBoolean(itemId.toString(), false)
    }

    fun setFavorite(itemId: Int, isFavorite: Boolean) {
        sharedPreferences.edit().putBoolean(itemId.toString(), isFavorite).apply()
        notifyObservers()
    }

    fun addObserver(observer: FavoritesObserver) {
        observers.add(observer)
    }

    fun removeObserver(observer: FavoritesObserver) {
        observers.remove(observer)
    }

    private fun notifyObservers() {
        for (observer in observers) {
            observer.onFavoritesUpdated()
        }
    }

    fun removeFavorite(itemId: Int) {
        val editor = sharedPreferences.edit()
        editor.remove(itemId.toString())
        editor.apply()
    }
}