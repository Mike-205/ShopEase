package com.example.shopease.manager

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.example.shopease.DatabaseHelper
import com.example.shopease.MainActivity
import com.example.shopease.model.UserModel
import com.google.gson.Gson


class AuthServices(private val context: Context) {

    companion object {
        lateinit var userEmail: String
    }

    var sharedPreferences: SharedPreferences = context.getSharedPreferences("AuthPreferences", Context.MODE_PRIVATE)
    var gson = Gson()

    private val dbHelper = DatabaseHelper(context)

    fun login(email: String, password: String): String {

        // Check if all EditTexts contain data
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(context, "All fields must be filled", Toast.LENGTH_SHORT).show()
            return "All fields must be filled"
        }

        // Check if the email format is correct
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(context, "Invalid email format", Toast.LENGTH_SHORT).show()
            return "Invalid email"
        }

        // Check if the user already exists in the database with the provided email and password
        if (dbHelper.getUserByEmailAndPassword(email, password) == null) {
            Toast.makeText(context, "Incorrect email/password", Toast.LENGTH_SHORT).show()
            return "Incorrect email/password"
        }

        // Check if the user exists in SharedPreferences
        if (!sharedPreferences.contains(email)) {
            Toast.makeText(context, "User does not exist", Toast.LENGTH_SHORT).show()
            return "User does not exist"
        }

        // If the user exists, check if the password matches
        val userJson = sharedPreferences.getString(email, null)
        val user = gson.fromJson(userJson, UserModel::class.java)
        if (user.password != password) {
            Toast.makeText(context, "Incorrect password", Toast.LENGTH_SHORT).show()
            return "Incorrect password"
        }

        // If all conditions are met, return a success message
        Toast.makeText(context, "Successfully logged in", Toast.LENGTH_SHORT).show()
        userEmail = user.email
        // Start MainActivity
        val intent = Intent(context, MainActivity::class.java)
        context.startActivity(intent)
        return "Successfully logged in"
    }

    fun signup(username: String, email: String, password: String, confirmPassword: String): String {
        // Log the email input for debugging
        Log.d("AuthServices", "Email input: $email")

        // Check if all EditTexts contain data
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(context, "All fields must be filled", Toast.LENGTH_SHORT).show()
            return "All fields must be filled"
        }

        // Check if the email format is correct
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(context, "Invalid email format", Toast.LENGTH_SHORT).show()
            return "Invalid email"
        }

        // Check if password and confirm password match
        if (password != confirmPassword) {
            Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return "Passwords do not match"
        }

        // If all conditions are met, insert the new user into the database
        val newRowId = dbHelper.insertUser(username, email, password)
        if (newRowId == -1L) {
            Toast.makeText(context, "Email is already in use", Toast.LENGTH_SHORT).show()
            return "Email is already in use"
        }

        // Check if the user already exists in SharedPreferences
        if (sharedPreferences.contains(email)) {
            Toast.makeText(context, "User already exists", Toast.LENGTH_SHORT).show()
            return "User already exists"
        }

        // If all conditions are met, create a new user and store it in SharedPreferences
        val user = UserModel(username, email, password, mutableListOf())
        val editor = sharedPreferences.edit()
        val userJson = gson.toJson(user)
        Log.d("Auth", "userJson: $userJson")
        editor.putString(email, userJson)
        editor.apply()

        Toast.makeText(context, "successfully signed up", Toast.LENGTH_SHORT).show()

        userEmail = email
        // Start MainActivity
        val intent = Intent(context, MainActivity::class.java)
        context.startActivity(intent)
        return "User created successfully"
    }
}