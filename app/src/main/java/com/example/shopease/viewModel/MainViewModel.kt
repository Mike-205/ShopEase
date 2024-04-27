package com.example.shopease.viewModel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopease.R
import com.example.shopease.model.CategoryModel
import com.example.shopease.model.RecommendedModel
import com.example.shopease.model.SliderModel
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

// MainViewModel class that extends ViewModel
// It is responsible for preparing and managing the data for an Activity or a Fragment
class MainViewModel(private val context: Context) : ViewModel() {

    // LiveData for slider data
    val sliderData = MutableLiveData<List<SliderModel>>()

    init {
        // Load slider data on initialization
        loadSliderData()
    }

    // Function to load slider data
    private fun loadSliderData() {
        // Launch a new coroutine in background and continue
        viewModelScope.launch {
            // Read the JSON file
            val inputStream = context.resources.openRawResource(R.raw.banner_images)
            val json = inputStream.bufferedReader().use { it.readText() }
            val jsonObject = JSONObject(json)

            // Create a list of SliderModel from the JSON file
            val sliderList = listOf(
                SliderModel(getImageResId(jsonObject.getString("banner1").removeSuffix(".png"))),
                SliderModel(getImageResId(jsonObject.getString("banner2").removeSuffix(".png")))
            )

            // Post the list to sliderData LiveData
            sliderData.postValue(sliderList)
        }
    }

    // LiveData for category data
    val categoryData = MutableLiveData<List<CategoryModel>>()

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

    // LiveData for recommended data
    val recommendedData = MutableLiveData<List<RecommendedModel>>()

    init {
        // Load recommended data on initialization
        loadRecommendedData()
    }

    // Function to load recommended data
    private fun loadRecommendedData() {
        // Launch a new coroutine in background and continue
        viewModelScope.launch {
            // Read the JSON file
            val inputStream = context.resources.openRawResource(R.raw.data)
            val json = inputStream.bufferedReader().use { it.readText() }
            val jsonArray = JSONArray(json)

            // Create a list of RecommendedModel from the JSON file
            val recommendedList = mutableListOf<RecommendedModel>()
            for (i in 0 until 10) {
                val jsonObject = jsonArray.getJSONObject(i)
                val name = jsonObject.getString("name")
                val image = jsonObject.getString("image")
                val price = jsonObject.getDouble("price")
                val rating = jsonObject.getDouble("rating")
                val imageResId = getImageResId(image.removeSuffix(".jpg"))
                recommendedList.add(RecommendedModel(name, image, price, rating, imageResId))
            }
            // Post the list to recommendedData LiveData
            recommendedData.postValue(recommendedList)
        }
    }

    // LiveData for selected category data
    val selectedCategoryData = MutableLiveData<List<RecommendedModel>>()

    // Function to load items of a specific category
    fun loadCategoryItems(categoryName: String?) {
        // Launch a new coroutine in background and continue
        viewModelScope.launch {
            // Read the JSON file
            val inputStream = context.resources.openRawResource(R.raw.data)
            val json = inputStream.bufferedReader().use { it.readText() }
            val jsonArray = JSONArray(json)

            // Create a list of RecommendedModel from the JSON file
            val categoryItemsList = mutableListOf<RecommendedModel>()
            if (categoryName.isNullOrEmpty()) {
                loadRecommendedData()
            } else {
                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    val name = jsonObject.getString("name")
                    val image = jsonObject.getString("image")
                    val price = jsonObject.getDouble("price")
                    val rating = jsonObject.getDouble("rating")
                    val category = jsonObject.getString("category")
                    val imageResId = getImageResId(image.removeSuffix(".jpg"))
                    if (category == categoryName) {
                        categoryItemsList.add(RecommendedModel(name, image, price, rating, imageResId))
                    }
                }
                // Post the list to selectedCategoryData LiveData
                selectedCategoryData.postValue(categoryItemsList)
            }
        }
    }

    // Function to reset selected category data
    fun resetSelectedCategoryData() {
        loadRecommendedData()
        selectedCategoryData.postValue(recommendedData.value)
    }

    // Function to get image resource ID from resource name
    private fun getImageResId(resName: String): Int {
        return context.resources.getIdentifier(resName, "drawable", context.packageName)
    }
}