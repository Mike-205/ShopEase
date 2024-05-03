package com.example.shopease.model

data class OrderModel (
    var imageResId: Int,
    var productName: String?,
    var subtotal: Double,
    var total: Double,
    var size: String?,
    var quantity: Int
)