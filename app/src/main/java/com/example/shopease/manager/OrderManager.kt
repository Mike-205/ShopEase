package com.example.shopease.manager

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.content.Context
import android.content.SharedPreferences
import com.example.shopease.model.OrderModel

class OrderManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("OrderPrefs", Context.MODE_PRIVATE)

    fun removeOrder(order: OrderModel) {
        // Get the current list of orders
        val orders = getOrdersList().toMutableList()

        // Remove the specified order
        orders.remove(order)

        // Save the updated list back to the shared preferences
        cacheOrdersList(orders)
    }
    fun cacheOrdersList(orders: List<OrderModel>) {
        val ordersJson = Gson().toJson(orders)
        sharedPreferences.edit().putString("orders_list", ordersJson).apply()
    }

    fun getOrdersList(): List<OrderModel> {
        val ordersJson = sharedPreferences.getString("orders_list", null)
        return if (ordersJson != null) {
            val type = object : TypeToken<List<OrderModel>>() {}.type
            Gson().fromJson(ordersJson, type)
        } else {
            emptyList()
        }
    }
}