package com.example.shopease.model

data class DbCartProductModel(
    var imageResId: Int,
    var productName: String?,
    var subtotal: Double,
    var total: Double,
    var quantity: Int,
    var size: String?,
    var email: String
)
