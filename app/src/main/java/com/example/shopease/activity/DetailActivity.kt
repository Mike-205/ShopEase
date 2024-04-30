package com.example.shopease.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopease.AppData.cartItems
import com.example.shopease.R
import com.example.shopease.adapter.SizeAdapter
import com.example.shopease.model.CartModel
import org.json.JSONArray
import java.io.InputStream
import java.nio.charset.Charset

// DetailActivity class that extends AppCompatActivity
class DetailActivity : AppCompatActivity() {

    // onCreate function that is called when the activity is first created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // Get the intent extras
        val name = intent.getStringExtra("name")
        val imageResId = intent.getIntExtra("imageResId", 0)
        val price = intent.getDoubleExtra("price", 0.0)
        val rating = intent.getDoubleExtra("rating", 0.0)

        // Find the views by their ids
        val imageView: ImageView = findViewById(R.id.productImage)
        val nameView: TextView = findViewById(R.id.productName)
        val priceView: TextView = findViewById(R.id.productValue)
        val ratingView: TextView = findViewById(R.id.ratingText)
        val descriptionView: TextView = findViewById(R.id.description)

        // Set the back button click listener
        val backButton = findViewById<ImageView>(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }

        val imageViewCart: ImageView = findViewById(R.id.cart)

        // Set an OnClickListener for the cart image view
        imageViewCart.setOnClickListener {
            // Start the CartActivity
            println("Clicked")
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }

        // Find the "Add to Cart" button by its id
        val addToCartButton = findViewById<Button>(R.id.addToCart)

        // Set an OnClickListener for the button
        addToCartButton.setOnClickListener {
            // Create a new CartItem instance with the product details
            val cartItem = CartModel(imageResId, name, price, price, 1)

            // Check if the item already exists in the cart
            val itemExistsInCart = cartItems.any { it.productName == cartItem.productName }

            if (itemExistsInCart) {
                // If the item already exists, display a toast message
                Toast.makeText(this, "Item is already added to cart", Toast.LENGTH_SHORT).show()
            } else {
                // If the item does not exist, add it to the cart and display a toast message
                cartItems.add(cartItem)
                Toast.makeText(this, "Item added to cart", Toast.LENGTH_SHORT).show()
            }
        }

        // Set the image resource, name, price and rating
        imageView.setImageResource(imageResId)
        nameView.text = name
        priceView.text = "$$price"
        ratingView.text = "${rating.toString()} ratings"

        // Load JSON from the raw resource
        val jsonStr = loadJSONFromResource(R.raw.data)
        val jsonArray = JSONArray(jsonStr)

        // Iterate over the JSON array to find the product with the same name
        for (i in 0 until jsonArray.length()) {
            val productJson = jsonArray.getJSONObject(i)
            if (productJson.getString("name") == name) {
                // Get the sizes and description for the product
                val sizesJsonArray = productJson.getJSONArray("sizes")
                val description = productJson.getString("description")

                // Convert the JSON array to a list of strings
                val sizes = List(sizesJsonArray.length()) { j -> sizesJsonArray.getString(j) }

                // Set the adapter and layout manager for the RecyclerView
                val sizeListRecyclerView = findViewById<RecyclerView>(R.id.sizeList)
                val sizeAdapter = SizeAdapter(sizes)
                sizeListRecyclerView.adapter = sizeAdapter
                sizeListRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

                // Set the description text
                descriptionView.text = description

                // Break the loop as we've found the matching product
                break
            }
        }
    }

    // Function to load JSON from a raw resource
    private fun loadJSONFromResource(resourceId: Int): String {
        val inputStream: InputStream = resources.openRawResource(resourceId)
        val size: Int = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        return String(buffer, Charset.forName("UTF-8"))
    }
}