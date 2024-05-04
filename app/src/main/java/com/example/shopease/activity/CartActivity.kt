package com.example.shopease.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopease.data.CartData.cartItems
import com.example.shopease.R
import com.example.shopease.adapter.CartAdapter
import com.example.shopease.data.OrderData
import com.example.shopease.manager.OrderManager
import com.example.shopease.model.OrderModel

class CartActivity : AppCompatActivity() {
    // Define adapter and TextViews as properties of the class
    private lateinit var adapter: CartAdapter
    private lateinit var subtotalTextView: TextView
    private lateinit var deliveryFeeTextView: TextView
    private lateinit var totalTaxTextView: TextView
    private lateinit var grandTotalTextView: TextView
    private lateinit var orderButton: Button

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

        // Initialize your order button
        orderButton = findViewById(R.id.button2)
        orderButton.isEnabled = false // disable the button initially

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
            // If the cart is empty, show the empty cart message and disable the order button
            emptyCartTextView.visibility = View.VISIBLE
            orderButton.isEnabled = false
        } else {
            // If the cart is not empty, hide the empty cart message and enable the order button
            emptyCartTextView.visibility = View.GONE
            orderButton.isEnabled = true
        }

        val backBtn: ImageView = findViewById(R.id.backBtn)

        backBtn.setOnClickListener {
            finish()
        }

        orderButton.setOnClickListener {
            OrderData.itemsOnOrder.clear()
            for (item in cartItems) {
                OrderData.itemsOnOrder.add(
                    OrderModel(
                        item.imageResId,
                        item.productName,
                        item.subtotal,
                        item.total,
                        item.size,
                        item.quantity
                    )
                )
            }
            // Display a toast message
            Toast.makeText(this, "Order successful", Toast.LENGTH_SHORT).show()

            // Convert CartModel objects to OrderModel objects and cache the list
            val orders = cartItems.map { cartItem ->
                OrderModel(
                    cartItem.imageResId,
                    cartItem.productName,
                    cartItem.subtotal,
                    cartItem.total,
                    cartItem.size,
                    cartItem.quantity
                )
            }
            val orderManager = OrderManager(this)
            orderManager.cacheOrdersList(orders)

            // Navigate to OrdersActivity
            val intent = Intent(this, OrdersActivity::class.java)
            startActivity(intent)
            // Clear the cart items
            cartItems.clear()
            // Update the cart values and the RecyclerView
            updateCartValues()
            adapter.notifyDataSetChanged()
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