package com.example.shopease.model

data class DbFavoriteModel(
    // name property is a String that represents the name of the recommended item
    val productName: String?,

    // price property is a Double that represents the price of the recommended item
    val price: Double,

    // rating property is a Double that represents the rating of the recommended item
    val rating: Double,

    // imageResId is an Int that represents the resource ID of the image associated with the recommended item
    val imageResId: Int,
    val email: String
)
