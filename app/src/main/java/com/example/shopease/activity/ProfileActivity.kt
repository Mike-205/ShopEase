package com.example.shopease.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.shopease.MainActivity
import com.example.shopease.R
import com.example.shopease.manager.AuthServices
import com.example.shopease.model.UserModel

class ProfileActivity : AppCompatActivity() {
    private lateinit var authservices: AuthServices

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        authservices = AuthServices(this)

        val explorerItem = findViewById<ImageView>(R.id.explorerNav)
        val cartItem =findViewById<ImageView>(R.id.cartNav)
        val backBtn = findViewById<ImageView>(R.id.imageView)

        val myOrderBtn = findViewById<ImageView>(R.id.forwardBtn2)
        val myFavBtn = findViewById<ImageView>(R.id.forwardBtn1)

        val fullUserName = findViewById<TextView>(R.id.userName)


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

        // In your ProfileActivity.kt
        val logoutBtn = findViewById<ImageView>(R.id.forwardBtn3)

        logoutBtn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            // Clear the activity stack to prevent the user from going back
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        //ProfileName update

        val userJson = authservices.sharedPreferences.getString(AuthServices.userEmail, null)
        val user = authservices.gson.fromJson(userJson, UserModel::class.java)
        fullUserName.text = user.username
    }
}