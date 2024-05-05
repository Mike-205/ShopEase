package com.example.shopease.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.shopease.R
import com.example.shopease.manager.AuthServices

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val authServices = AuthServices(this)

        val emailEditText: EditText = findViewById(R.id.userEmail)
        val passwordEditText: EditText = findViewById(R.id.userPassword)
        val loginButton: Button = findViewById(R.id.signInBtn)
        val signUpText: TextView = findViewById(R.id.signUpText)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            authServices.login(email, password)
        }

        signUpText.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}