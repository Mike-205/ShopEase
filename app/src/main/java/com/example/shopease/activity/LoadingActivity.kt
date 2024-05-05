package com.example.shopease.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.shopease.R

class LoadingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, GetStartedActivity::class.java)
            startActivity(intent)
            finish()
        }, 5000) // 3000 milliseconds = 3 seconds
    }
}