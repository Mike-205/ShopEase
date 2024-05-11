package com.example.shopease.model

data class DbRecommendedModel(
    // name property is a String that represents the name of the recommended item
    val productName: String,

    // image property is a String that could be a URL or a file path to the item's image
    val image: String,

    // price property is a Double that represents the price of the recommended item
    val price: Double,

    // rating property is a Double that represents the rating of the recommended item
    val rating: Double,

    // imageResId is an Int that represents the resource ID of the image associated with the recommended item
    val imageResId: Int,
    val category: String
)
