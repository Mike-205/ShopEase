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

class MainViewModel(private val context: Context) : ViewModel() {

    val sliderData = MutableLiveData<List<SliderModel>>()

    init {
        loadSliderData()
    }

    private fun loadSliderData() {
        viewModelScope.launch {
            val inputStream = context.resources.openRawResource(R.raw.banner_images)
            val json = inputStream.bufferedReader().use { it.readText() }
            val jsonObject = JSONObject(json)

            val sliderList = listOf(
                SliderModel(getImageResId(jsonObject.getString("banner1").removeSuffix(".png"))),
                SliderModel(getImageResId(jsonObject.getString("banner2").removeSuffix(".png")))
            )

            sliderData.postValue(sliderList)
        }
    }

    val categoryData = MutableLiveData<List<CategoryModel>>()

    init {
        loadCategoryData()
    }

    private fun loadCategoryData() {
        viewModelScope.launch {
            val inputStream = context.resources.openRawResource(R.raw.categories)
            val json = inputStream.bufferedReader().use { it.readText() }
            val jsonObject = JSONObject(json)

            val categoryList = jsonObject.keys().asSequence().map { key ->
                val imageName = jsonObject.getString(key).removeSuffix(".jpg")
                val imageResId = context.resources.getIdentifier(imageName, "drawable", context.packageName)
                CategoryModel(key, imageResId)
            }.toList()

            categoryData.postValue(categoryList)
        }
    }

    val recommendedData = MutableLiveData<List<RecommendedModel>>()

    init {
        loadRecommendedData()
    }

    private fun loadRecommendedData() {
        viewModelScope.launch {
            val inputStream = context.resources.openRawResource(R.raw.data)
            val json = inputStream.bufferedReader().use { it.readText() }
            val jsonArray = JSONArray(json)

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
            recommendedData.postValue(recommendedList)
        }
    }

    private fun getImageResId(resName: String): Int {
        return context.resources.getIdentifier(resName, "drawable", context.packageName)
    }
}
