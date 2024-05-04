// Package declaration
package com.example.shopease.activity

// Import statements
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.shopease.R
import com.example.shopease.manager.FavoritesManager

// IntroActivity class that extends AppCompatActivity
class IntroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        // Initialize FavoritesManager
        FavoritesManager.initialize(this)
    }
}
