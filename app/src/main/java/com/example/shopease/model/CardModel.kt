package com.example.shopease.model

data class CartModel (
    var imageResId: Int,
    var productName: String?,
    var subtotal: Double,
    var total: Double,
    var quantity: Int
)