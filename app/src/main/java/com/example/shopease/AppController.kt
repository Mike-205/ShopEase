package com.example.shopease

import android.app.Application
import org.json.JSONArray

class AppController : Application() {

    override fun onCreate() {
        super.onCreate()
        // Check if the database is empty
        val dbHelper = DatabaseHelper(this)
        if (dbHelper.getRecommendedProducts().isEmpty()) {
            // If the database is empty, populate it
            println("Populating DB with products...")
            // Read the JSON file
            val inputStream = resources.openRawResource(R.raw.data)
            val json = inputStream.bufferedReader().use { it.readText() }
            val jsonArray = JSONArray(json)

            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val name = jsonObject.getString("name")
                val image = jsonObject.getString("image")
                val price = jsonObject.getDouble("price")
                val rating = jsonObject.getDouble("rating")
                val imageResId =
                    resources.getIdentifier(image.removeSuffix(".jpg"), "drawable", packageName)
                val categoryName = jsonObject.getString("category")
                dbHelper.insertProduct(name, image, price, rating, imageResId, categoryName)
            }
            println("DB populated with products...")
            println("Getting all products from DB...")
            if (dbHelper.getRecommendedProducts().isNotEmpty()) {
                println("Got all products from DB...")
            } else {
            println("No products found in DB...")
            }
        }
    }
}