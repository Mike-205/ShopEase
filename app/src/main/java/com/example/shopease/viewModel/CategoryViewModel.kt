package com.example.shopease.viewModel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopease.DatabaseHelper
import com.example.shopease.R
import com.example.shopease.model.CategoryModel
import com.example.shopease.model.RecommendedModel
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class CategoryViewModel(private val context: Context) : ViewModel() {
    // LiveData for category data
    val categoryData = MutableLiveData<List<CategoryModel>>()

    // LiveData for product data
    val productData = MutableLiveData<List<RecommendedModel>>()
    private val dbHelper = DatabaseHelper(context)

    // LiveData for the currently selected category
    val selectedCategoryData = MutableLiveData<List<RecommendedModel>>()

    init {
        // Load category data on initialization
        loadCategoryData()
    }

    // Function to load category data
    private fun loadCategoryData() {
        // Launch a new coroutine in background and continue
        viewModelScope.launch {
            // Read the JSON file
            val inputStream = context.resources.openRawResource(R.raw.categories)
            val json = inputStream.bufferedReader().use { it.readText() }
            val jsonObject = JSONObject(json)

            // Create a list of CategoryModel from the JSON file
            val categoryList = jsonObject.keys().asSequence().map { key ->
                val imageName = jsonObject.getString(key).removeSuffix(".jpg")
                val imageResId = context.resources.getIdentifier(imageName, "drawable", context.packageName)
                CategoryModel(key, imageResId)
            }.toList()

            // Post the list to categoryData LiveData
            categoryData.postValue(categoryList)
        }
    }

    // Function to load items of a specific category
    // Function to load items of a specific category
    fun loadCategoryItems(categoryName: String) {
        // Launch a new coroutine in background and continue
        viewModelScope.launch {
            // Create a list of RecommendedModel from the JSON file
            val products = dbHelper.getProductsByCategory(categoryName)
            // Post the list to recommendedData LiveData
            selectedCategoryData.postValue(products)
        }
    }

    // Function to reset selected category data
    fun resetSelectedCategoryData() {
        selectedCategoryData.value = emptyList()
    }

    fun onPriceFilterClicked() {
        if (!selectedCategoryData.value.isNullOrEmpty()) {
            val category = dbHelper.getProductCategory(
                selectedCategoryData.value!![0].name,
                selectedCategoryData.value!![0].image,
                selectedCategoryData.value!![0].price,
                selectedCategoryData.value!![0].rating,
                selectedCategoryData.value!![0].imageResId
            )
            val sortedProducts = dbHelper.getProductsByCategoryAndSortByPrice(category)
            // Now sortedProducts contains the products sorted by price, updating the UI.
            selectedCategoryData.value = sortedProducts
        }
    }
    fun onNameFilterClicked() {
        if (!selectedCategoryData.value.isNullOrEmpty()) {
            val category = dbHelper.getProductCategory(
                selectedCategoryData.value!![0].name,
                selectedCategoryData.value!![0].image,
                selectedCategoryData.value!![0].price,
                selectedCategoryData.value!![0].rating,
                selectedCategoryData.value!![0].imageResId
            )
            val sortedProducts = dbHelper.getProductsByCategoryAndSortByName(category)
            // Now sortedProducts contains the products sorted by price, updating the UI.
            selectedCategoryData.value = sortedProducts
        }
    }

    fun onPopularFilterClicked() {
        if (!selectedCategoryData.value.isNullOrEmpty()) {
            val category = dbHelper.getProductCategory(
                selectedCategoryData.value!![0].name,
                selectedCategoryData.value!![0].image,
                selectedCategoryData.value!![0].price,
                selectedCategoryData.value!![0].rating,
                selectedCategoryData.value!![0].imageResId
            )
            val sortedProducts = dbHelper.getProductsByCategory(category)
            // Now sortedProducts contains the products sorted by price, updating the UI.
            selectedCategoryData.value = sortedProducts
        }
    }

    fun onRatingFilterClicked() {
        if (!selectedCategoryData.value.isNullOrEmpty()) {
            val category = dbHelper.getProductCategory(
                selectedCategoryData.value!![0].name,
                selectedCategoryData.value!![0].image,
                selectedCategoryData.value!![0].price,
                selectedCategoryData.value!![0].rating,
                selectedCategoryData.value!![0].imageResId
            )
            val sortedProducts = dbHelper.getProductsByCategoryAndSortByRating(category)
            // Now sortedProducts contains the products sorted by price, updating the UI.
            selectedCategoryData.value = sortedProducts
        }
    }
}