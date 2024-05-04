package com.example.shopease.model

// Data class for RecommendedModel
// It has five properties: name, image, price, rating, and imageResId

data class FavoriteModel (
    // name property is a String that represents the name of the recommended item
    val name: String?,

    // price property is a Double that represents the price of the recommended item
    val price: Double,

    // rating property is a Double that represents the rating of the recommended item
    val rating: Double,

    // imageResId is an Int that represents the resource ID of the image associated with the recommended item
    val imageResId: Int
)