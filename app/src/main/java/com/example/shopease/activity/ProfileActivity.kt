package com.example.shopease.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.shopease.MainActivity
import com.example.shopease.R

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val explorerItem = findViewById<ImageView>(R.id.explorerNav)
        val cartItem =findViewById<ImageView>(R.id.cartNav)
        val backBtn = findViewById<ImageView>(R.id.imageView)

        val myOrderBtn = findViewById<ImageView>(R.id.forwardBtn2)
        val myFavBtn = findViewById<ImageView>(R.id.forwardBtn1)

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

        myOrderBtn.setOnClickListener {
            val intent = Intent(this, OrdersActivity::class.java)
            startActivity(intent)
        }

        myFavBtn.setOnClickListener {
            val intent = Intent(this, FavouriteActivity::class.java)
            startActivity(intent)
        }
    }
}