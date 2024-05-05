package com.example.shopease.manager

import android.content.Context
import android.content.SharedPreferences
import com.example.shopease.model.FavoriteModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

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

    fun cacheFavoritesList(favorites: List<FavoriteModel>) {
        val favoritesJson = Gson().toJson(favorites)
        sharedPreferences.edit().putString("favorites_list", favoritesJson).apply()
    }

    fun getFavoritesList(): List<FavoriteModel> {
        val favoritesJson = sharedPreferences.getString("favorites_list", null)
        return if (favoritesJson != null) {
            val type = object : TypeToken<List<FavoriteModel>>() {}.type
            Gson().fromJson(favoritesJson, type)
        } else {
            emptyList()
        }
    }


}