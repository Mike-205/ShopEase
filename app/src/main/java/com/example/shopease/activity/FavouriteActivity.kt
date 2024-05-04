package com.example.shopease.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.shopease.MainActivity
import com.example.shopease.R
import com.example.shopease.adapter.FavoriteAdapter
import com.example.shopease.data.FavoriteData
import com.example.shopease.data.OrderData
import com.example.shopease.model.FavoriteModel

class FavouriteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite)

        // Initialize RecyclerView and Adapter
        val favoriteItems: MutableList<FavoriteModel> = FavoriteData.favoriteItems
        val favoriteAdapter = FavoriteAdapter(favoriteItems)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = favoriteAdapter

        //Nav items
        val explorerItem = findViewById<ImageView>(R.id.explorerNav)
        val cartItem = findViewById<ImageView>(R.id.cartNav)
        val profileItem = findViewById<ImageView>(R.id.profileNav)
        val backBtn = findViewById<ImageView>(R.id.backBtn)

        // on click implementation
        backBtn.setOnClickListener {
            finish()
        }

        explorerItem.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        cartItem.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }

        profileItem.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        val emptyText: TextView = findViewById(R.id.emptyText)
        if (FavoriteData.favoriteItems.isNotEmpty()) {
            emptyText.visibility = View.GONE
        }
    }
}