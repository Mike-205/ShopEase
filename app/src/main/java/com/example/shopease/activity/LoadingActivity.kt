package com.example.shopease.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.shopease.R
import com.example.shopease.manager.AuthServices

class LoadingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        val authServices = AuthServices(this)
        val sharedPreferences = authServices.sharedPreferences

        Handler(Looper.getMainLooper()).postDelayed({
            // Check if AuthPreferences is empty
            if (sharedPreferences.all.isEmpty()) {
                // If it is, redirect to GetStartedActivity
                val intent = Intent(this, GetStartedActivity::class.java)
                startActivity(intent)
            } else {
                intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }, 5000) // 3000 milliseconds = 3 seconds
    }
}