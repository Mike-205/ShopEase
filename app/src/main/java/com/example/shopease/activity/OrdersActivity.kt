package com.example.shopease.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopease.MainActivity
import com.example.shopease.R
import com.example.shopease.adapter.OrderAdapter
import com.example.shopease.data.OrderData

class OrdersActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = OrderAdapter(OrderData.itemsOnOrder, this)

        val emptyText: TextView = findViewById(R.id.emptyText)
        if (OrderData.itemsOnOrder.isNotEmpty()) {
            emptyText.visibility = View.GONE
        }

        //Nav items
        val explorerItem = findViewById<ImageView>(R.id.explorerNav)
        val cartItem =findViewById<ImageView>(R.id.cartNav)
        val profileItem = findViewById<ImageView>(R.id.profileNav)

        val backBtn = findViewById<ImageView>(R.id.backBtn)
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

        profileItem.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }
}