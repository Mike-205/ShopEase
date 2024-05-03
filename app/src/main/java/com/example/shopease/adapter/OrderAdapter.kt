package com.example.shopease.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shopease.R
import com.example.shopease.activity.CartActivity
import com.example.shopease.model.OrderModel

class OrderAdapter(private val orderItems: MutableList<OrderModel>, private val context: Context) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.orderItemImage)
        val titleText: TextView = itemView.findViewById(R.id.titleText)
        val itemCost: TextView = itemView.findViewById(R.id.feeEachItem)
        val numberItems: TextView = itemView.findViewById(R.id.numberItems)
        val cancelButton: TextView = itemView.findViewById(R.id.cancelBtn)
        val totalCost: TextView = itemView.findViewById(R.id.grandTotal)
        val itemSize: TextView = itemView.findViewById(R.id.sizeText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_orders, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val orderItem = orderItems[position]

        holder.itemSize.text = orderItem.size
        holder.titleText.text = orderItem.productName
        holder.itemCost.text = "$${orderItem.subtotal}"
        holder.totalCost.text = "$${orderItem.total}"
        holder.numberItems.text = orderItem.quantity.toString()
        holder.imageView.setImageResource(orderItem.imageResId)

        holder.cancelButton.setOnClickListener {
            orderItems.removeAt(position)
            notifyDataSetChanged()
            // You can add more actions here when an order is cancelled
        }
    }

    override fun getItemCount() = orderItems.size
}