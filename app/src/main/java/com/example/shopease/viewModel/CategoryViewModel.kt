package com.example.shopease.viewModel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
            // Read the JSON file
            val inputStream = context.resources.openRawResource(R.raw.data)
            val json = inputStream.bufferedReader().use { it.readText() }
            val jsonArray = JSONArray(json)

            // Create a list of RecommendedModel from the JSON file
            val categoryItemsList = mutableListOf<RecommendedModel>()
            var lastImageResId = -1
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val category = jsonObject.getString("category")
                if (category == categoryName) {
                    val name = jsonObject.getString("name")
                    val image = jsonObject.getString("image")
                    val price = jsonObject.getDouble("price")
                    val rating = jsonObject.getDouble("rating")
                    val imageResId = getImageResId(image.removeSuffix(".jpg"))
                    if (imageResId != lastImageResId) {
                        categoryItemsList.add(RecommendedModel(name, image, price, rating, imageResId))
                        lastImageResId = imageResId
                    }
                }
            }
            // Post the list to selectedCategoryData LiveData
            selectedCategoryData.postValue(categoryItemsList)
        }
    }

    // Function to reset selected category data
    fun resetSelectedCategoryData() {
        selectedCategoryData.postValue(emptyList())
    }

    // Function to get image resource ID from resource name
    private fun getImageResId(resName: String): Int {
        return context.resources.getIdentifier(resName, "drawable", context.packageName)
    }
}