package com.example.shopease.model

data class DbOrderModel(
    var imageResId: Int,
    var productName: String?,
    var subtotal: Double,
    var total: Double,
    var size: String?,
    var quantity: Int,
    var email: String
)