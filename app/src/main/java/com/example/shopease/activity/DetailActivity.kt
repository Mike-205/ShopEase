package com.example.shopease.activity

import FavoritesManager
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopease.data.CartData.cartItems
import com.example.shopease.R
import com.example.shopease.adapter.SizeAdapter
import com.example.shopease.model.CartModel
import com.example.shopease.util.Utils
import org.json.JSONArray
import java.io.InputStream
import java.nio.charset.Charset

class DetailActivity : AppCompatActivity() {
    private lateinit var favoritesManager: FavoritesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        favoritesManager = FavoritesManager(this)

        val itemId = intent.getIntExtra("imageResId", 0)
        val favoriteIcon: ImageView = findViewById(R.id.favouriteButton)

        Utils.setupFavoriteIconToggle(favoriteIcon, itemId, R.drawable.btn_3, R.drawable.btn_3_2, favoritesManager)

        val name = intent.getStringExtra("name")
        val imageResId = intent.getIntExtra("imageResId", 0)
        val price = intent.getDoubleExtra("price", 0.0)
        val rating = intent.getDoubleExtra("rating", 0.0)

        val imageView: ImageView = findViewById(R.id.productImage)
        val nameView: TextView = findViewById(R.id.productName)
        val priceView: TextView = findViewById(R.id.productValue)
        val ratingView: TextView = findViewById(R.id.ratingText)
        val descriptionView: TextView = findViewById(R.id.description)

        val backButton = findViewById<ImageView>(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }

        val imageViewCart: ImageView = findViewById(R.id.cart)
        imageViewCart.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }

        val addToCartButton = findViewById<Button>(R.id.addToCart)

        imageView.setImageResource(imageResId)
        nameView.text = name
        priceView.text = "$$price"
        ratingView.text = "${rating.toString()} ratings"

        val jsonStr = loadJSONFromResource(R.raw.data)
        val jsonArray = JSONArray(jsonStr)

        for (i in 0 until jsonArray.length()) {
            val productJson = jsonArray.getJSONObject(i)
            if (productJson.getString("name") == name) {
                val sizesJsonArray = productJson.getJSONArray("sizes")
                val description = productJson.getString("description")

                val sizes = List(sizesJsonArray.length()) { j -> sizesJsonArray.getString(j) }

                val sizeListRecyclerView = findViewById<RecyclerView>(R.id.sizeList)
                val sizeAdapter = SizeAdapter(sizes)
                sizeListRecyclerView.adapter = sizeAdapter
                sizeListRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

                descriptionView.text = description

                addToCartButton.setOnClickListener {
                    val selectedSize = sizeAdapter.selectedSize

                    if (selectedSize == null) {
                        Toast.makeText(this, "Please choose a size to proceed", Toast.LENGTH_SHORT).show()
                    } else {
                        val cartItem = CartModel(imageResId, name, price, price, 1, selectedSize)

                        // Check if the item already exists in the cart
                        val itemExistsInCart = cartItems.any { it.productName == cartItem.productName && it.size == cartItem.size }

                        if (itemExistsInCart) {
                            // If the item already exists, display a toast message
                            Toast.makeText(this, "Item is already added to cart", Toast.LENGTH_SHORT).show()
                        } else {
                            // If the item does not exist, add it to the cart and display a toast message
                            cartItems.add(cartItem)
                            Toast.makeText(this, "Item added to cart", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                break
            }
        }
    }

    private fun loadJSONFromResource(resourceId: Int): String {
        val inputStream: InputStream = resources.openRawResource(resourceId)
        val size: Int = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        return String(buffer, Charset.forName("UTF-8"))
    }
}