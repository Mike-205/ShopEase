// Package declaration
package com.example.shopease.activity

// Import statements
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.shopease.R

// IntroActivity class that extends AppCompatActivity
class IntroActivity : AppCompatActivity() {
    // onCreate function that is called when the activity is first created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the layout for this activity
        setContentView(R.layout.activity_intro)
    }
}