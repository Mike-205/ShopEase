package com.example.shopease.viewModel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopease.DatabaseHelper
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

    private val dbHelper = DatabaseHelper(context)
    private val recommendedDataNew: MutableList<RecommendedModel> = dbHelper.getRecommendedProducts()
    private val  recommendedDataNewSet = recommendedDataNew.toSet()

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

    // LiveData for selected category data
    val selectedCategoryData = MutableLiveData<List<RecommendedModel>>()

    init {
        // Load recommended data on initialization
        loadRecommendedData()
    }

    // Function to load recommended data
    private fun loadRecommendedData() {
        // Launch a new coroutine in background and continue
        viewModelScope.launch {
            // Create a list of RecommendedModel from the JSON file
            val recommendedList = dbHelper.getRecommendedProducts()
            // Post the list to recommendedData LiveData
            recommendedData.postValue(recommendedList)
            selectedCategoryData.postValue(recommendedList)
        }
    }

    // Function to load items of a specific category
    // Function to load items of a specific category
    fun loadCategoryItems(categoryName: String?) {
        // Launch a new coroutine in background and continue
        viewModelScope.launch {
            if (categoryName.isNullOrEmpty()) {
                loadRecommendedData()
            } else {
                var categoryItemsList = mutableListOf<RecommendedModel>()
                categoryItemsList = dbHelper.getProductsByCategory(categoryName)
                // Post the list to selectedCategoryData LiveData
                selectedCategoryData.postValue(categoryItemsList)
            }
        }

    }

    // Function to reset selected category data
    fun resetSelectedCategoryData() {
        loadRecommendedData()
        selectedCategoryData.postValue(recommendedDataNew)
    }

    // Function to get image resource ID from resource name
    private fun getImageResId(resName: String): Int {
        return context.resources.getIdentifier(resName, "drawable", context.packageName)
    }

    fun onPriceFilterClicked() {
        val selectedCategoryDataSet = selectedCategoryData.value!!.toSet()
        if (selectedCategoryDataSet == recommendedDataNewSet || selectedCategoryData.value == null) {
            val sortedProducts = dbHelper.getRecommendedProductsAndSortByPrice()
            // Now sortedProducts contains the products sorted by price, updating the UI.
            recommendedData.value = sortedProducts
            selectedCategoryData.value = sortedProducts
        } else {
            val category = dbHelper.getProductCategory(selectedCategoryData.value!![0].name, selectedCategoryData.value!![0].image, selectedCategoryData.value!![0].price, selectedCategoryData.value!![0].rating, selectedCategoryData.value!![0].imageResId)
            val sortedProducts = dbHelper.getProductsByCategoryAndSortByPrice(category)
            // Now sortedProducts contains the products sorted by price, updating the UI.
            recommendedData.value = sortedProducts
            selectedCategoryData.value = sortedProducts
        }
    }
    fun onNameFilterClicked() {
        println(selectedCategoryData.value)
        val selectedCategoryDataSet = selectedCategoryData.value!!.toSet()
        if (selectedCategoryDataSet == recommendedDataNewSet || selectedCategoryData.value == null) {
            val sortedProducts = dbHelper.getRecommendedProductsAndSortByName()
            // Now sortedProducts contains the products sorted by price, updating the UI.
            recommendedData.value = sortedProducts
            selectedCategoryData.value = sortedProducts
        } else {
            val category = dbHelper.getProductCategory(selectedCategoryData.value!![0].name, selectedCategoryData.value!![0].image, selectedCategoryData.value!![0].price, selectedCategoryData.value!![0].rating, selectedCategoryData.value!![0].imageResId)
            val sortedProducts = dbHelper.getProductsByCategoryAndSortByName(category)
            // Now sortedProducts contains the products sorted by price, updating the UI.
            recommendedData.value = sortedProducts
            selectedCategoryData.value = sortedProducts
        }
    }

    fun onPopularFilterClicked() {
        val selectedCategoryDataSet = selectedCategoryData.value!!.toSet()
        if (selectedCategoryDataSet == recommendedDataNewSet || selectedCategoryData.value == null) {
            resetSelectedCategoryData()
        } else {
            val category = dbHelper.getProductCategory(selectedCategoryData.value!![0].name, selectedCategoryData.value!![0].image, selectedCategoryData.value!![0].price, selectedCategoryData.value!![0].rating, selectedCategoryData.value!![0].imageResId)
            val sortedProducts = dbHelper.getProductsByCategory(category)
            // Now sortedProducts contains the products sorted by price, updating the UI.
            recommendedData.value = sortedProducts
            selectedCategoryData.value = sortedProducts
        }
    }

    fun onRatingFilterClicked() {
        val selectedCategoryDataSet = selectedCategoryData.value!!.toSet()
        if (selectedCategoryDataSet == recommendedDataNewSet || selectedCategoryData.value == null) {
            val sortedProducts = dbHelper.getRecommendedProductsAndSortByRating()
            // Now sortedProducts contains the products sorted by price, updating the UI.
            recommendedData.value = sortedProducts
            selectedCategoryData.value = sortedProducts
        } else {
            val category = dbHelper.getProductCategory(selectedCategoryData.value!![0].name, selectedCategoryData.value!![0].image, selectedCategoryData.value!![0].price, selectedCategoryData.value!![0].rating, selectedCategoryData.value!![0].imageResId)
            val sortedProducts = dbHelper.getProductsByCategoryAndSortByRating(category)
            // Now sortedProducts contains the products sorted by price, updating the UI.
            recommendedData.value = sortedProducts
            selectedCategoryData.value = sortedProducts
        }
    }
}