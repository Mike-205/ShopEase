package com.example.shopease.activity


import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.shopease.R
import com.example.shopease.manager.AuthServices

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)

        val authServices = AuthServices(this)
        val signUpButton: Button = findViewById(R.id.signUpBtn)


        signUpButton.setOnClickListener {
            val username = findViewById<EditText>(R.id.userName).text.toString()
            val email = findViewById<EditText>(R.id.userEmail).text.toString()
            val password = findViewById<EditText>(R.id.userPassword).text.toString()
            val confirmPassword = findViewById<EditText>(R.id.confirmPassword).text.toString()

            authServices.signup(username, email, password, confirmPassword)
        }
    }
}