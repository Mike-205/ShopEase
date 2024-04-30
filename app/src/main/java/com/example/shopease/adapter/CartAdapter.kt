package com.example.shopease.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shopease.R
import com.example.shopease.activity.CartActivity
import com.example.shopease.model.CartModel

class CartAdapter(private val cartItems: MutableList<CartModel>, private val context: Context) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.cartItemImage)
        val titleText: TextView = itemView.findViewById(R.id.titleText)
        val feeEachItem: TextView = itemView.findViewById(R.id.feeEachItem)
        val totalEachItem: TextView = itemView.findViewById(R.id.totaleachItem)
        val plusButton: TextView = itemView.findViewById(R.id.plusButton)
        val minusButton: TextView = itemView.findViewById(R.id.minusButton)
        val numberItems: TextView = itemView.findViewById(R.id.numberItems)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_cart, parent, false)
        return CartViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = cartItems[position]
        holder.titleText.text = cartItem.productName
        holder.feeEachItem.text = "$${cartItem.subtotal}"
        holder.totalEachItem.text = "$${cartItem.total}"
        holder.numberItems.text = cartItem.quantity.toString()
        holder.imageView.setImageResource(cartItem.imageResId)


        holder.plusButton.setOnClickListener {
            cartItem.quantity++
            cartItem.total = cartItem.subtotal * cartItem.quantity
            notifyDataSetChanged()
            (context as CartActivity).updateCartValues()
        }

        holder.minusButton.setOnClickListener {
            if (cartItem.quantity > 1) {
                cartItem.quantity--
                cartItem.total = cartItem.subtotal * cartItem.quantity
            } else {
                cartItems.removeAt(position)
            }
            notifyDataSetChanged()
            (context as CartActivity).updateCartValues()
        }
    }

    fun calculateSubtotal(): Double {
        var subtotal = 0.0
        for (item in cartItems) {
            subtotal += item.total
        }
        return subtotal
    }

    fun calculateDeliveryFee(subtotal: Double): Double {
        return if (subtotal < 50) 10.0 else subtotal * 0.085
    }

    fun calculateTotalTax(subtotal: Double): Double {
        return if (subtotal < 100) 5.0 else subtotal * 0.05
    }

    fun calculateGrandTotal(subtotal: Double, deliveryFee: Double, totalTax: Double): Double {
        return subtotal + deliveryFee + totalTax
    }

    override fun getItemCount() = cartItems.size
}