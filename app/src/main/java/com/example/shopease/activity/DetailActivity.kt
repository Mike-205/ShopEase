package com.example.shopease.activity

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopease.R
import com.example.shopease.adapter.SizeAdapter
import org.json.JSONArray
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val name = intent.getStringExtra("name")
        val imageResId = intent.getIntExtra("imageResId", 0)
        val price = intent.getDoubleExtra("price", 0.0)
        val rating = intent.getDoubleExtra("rating", 0.0)

        val imageView: ImageView = findViewById(R.id.productImage)
        val nameView: TextView = findViewById(R.id.productName)
        val priceView: TextView = findViewById(R.id.productValue)
        val ratingView: TextView = findViewById(R.id.ratingText)
        val descriptionView: TextView = findViewById(R.id.description)

        imageView.setImageResource(imageResId)
        nameView.text = name
        priceView.text = "$$price"
        ratingView.text = "${rating.toString()} ratings"

        // Load JSON from the raw resource
        val jsonStr = loadJSONFromResource(R.raw.data)
        val jsonArray = JSONArray(jsonStr)

        // Get the sizes and description for the product with the same name
        val productJson = jsonArray.getJSONObject(0)
        val sizesJsonArray = productJson.getJSONArray("sizes")
        val description = productJson.getString("description")

        // Convert the JSON array to a list of strings
        val sizes = List(sizesJsonArray.length()) { i -> sizesJsonArray.getString(i) }

        val sizeListRecyclerView = findViewById<RecyclerView>(R.id.sizeList)
        val sizeAdapter = SizeAdapter(sizes)
        sizeListRecyclerView.adapter = sizeAdapter
        sizeListRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        // Set the description text
        descriptionView.text = description
    }

    // Function to load JSON from a raw resource
    fun loadJSONFromResource(resourceId: Int): String {
        val inputStream: InputStream = resources.openRawResource(resourceId)
        val size: Int = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        return String(buffer, Charset.forName("UTF-8"))
    }
}