package com.example.shopease.activity

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopease.AppData.cartItems
import com.example.shopease.R
import com.example.shopease.adapter.CartAdapter

class CartActivity : AppCompatActivity() {
    // Define adapter and TextViews as properties of the class
    private lateinit var adapter: CartAdapter
    private lateinit var subtotalTextView: TextView
    private lateinit var deliveryFeeTextView: TextView
    private lateinit var totalTaxTextView: TextView
    private lateinit var grandTotalTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        // Initialize your RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        // Initialize your empty cart TextView
        val emptyCartTextView = findViewById<TextView>(R.id.emptyText)

        // Initialize your subtotal, delivery fee, and total tax TextViews
        subtotalTextView = findViewById(R.id.subtotal)
        deliveryFeeTextView = findViewById(R.id.deliveryFee)
        totalTaxTextView = findViewById(R.id.taxText)
        grandTotalTextView = findViewById(R.id.grandTotal)

        // Set the layout manager for your RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Create an instance of CartAdapter
        adapter = CartAdapter(cartItems, this)

        // Set the adapter for your RecyclerView
        recyclerView.adapter = adapter

        // Update cart values
        updateCartValues()

        // Check if the cart is empty
        if (adapter.itemCount == 0) {
            // If the cart is empty, show the empty cart message
            emptyCartTextView.visibility = View.VISIBLE
        } else {
            // If the cart is not empty, hide the empty cart message
            emptyCartTextView.visibility = View.GONE
        }

        val backBtn: ImageView = findViewById(R.id.backBtn)

        backBtn.setOnClickListener {
            finish()
        }
    }

    fun updateCartValues() {
        val subtotal = adapter.calculateSubtotal()
        val deliveryFee = adapter.calculateDeliveryFee(subtotal)
        val totalTax = adapter.calculateTotalTax(subtotal)
        val grandTotal = adapter.calculateGrandTotal(subtotal, deliveryFee, totalTax)

        subtotalTextView.text = "$%.2f".format(subtotal)
        deliveryFeeTextView.text = "$%.2f".format(deliveryFee)
        totalTaxTextView.text = "$%.2f".format(totalTax)
        grandTotalTextView.text = "$%.2f".format(grandTotal)
    }
}