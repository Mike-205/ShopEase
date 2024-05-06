package com.example.shopease.model

data class UserModel(
    var username: String,
    var email: String,
    var password: String,
    var favList: MutableList<FavoriteModel>
)