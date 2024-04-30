package com.example.shopease

import com.example.shopease.adapter.CategoryAdapter
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.shopease.activity.CartActivity
import com.example.shopease.adapter.RecommendedAdapter
import com.example.shopease.adapter.SliderAdapter
import com.example.shopease.model.CategoryModel
import com.example.shopease.viewModel.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator

// MainActivity class that extends AppCompatActivity and implements CategoryAdapter.OnCategoryClickListener
class MainActivity : AppCompatActivity(), CategoryAdapter.OnCategoryClickListener {
    // Declare MainViewModel and RecyclerView for recommended items
    private lateinit var viewModel: MainViewModel
    private lateinit var recommendedRecyclerView: RecyclerView
    private lateinit var progressBarRec: ProgressBar

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        // Initialize MainViewModel
        viewModel = ViewModelProvider(this, MainViewModelFactory(this))[MainViewModel::class.java]

        // Find views after setting the content view
        val dotsIndicator = findViewById<DotsIndicator>(R.id.dotsIndicator)
        val viewPager = findViewById<ViewPager2>(R.id.viewpageSlider)
        val recyclerView = findViewById<RecyclerView>(R.id.viewCategory)
        val progressBarCat = findViewById<ProgressBar>(R.id.progressBarCategory)
        val imageViewCart: ImageView = findViewById(R.id.navBtn2)
        recommendedRecyclerView = findViewById(R.id.viewRecommended)
        progressBarRec = findViewById(R.id.progressBarRecommended)

        // Set layout managers for RecyclerViews
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recommendedRecyclerView.layoutManager = GridLayoutManager(this, 2)

        // Observe the sliderData LiveData
        viewModel.sliderData.observe(this) { sliderData ->
            // Update the adapter when the data changes
            viewPager.adapter = SliderAdapter(this, sliderData)
            dotsIndicator.setViewPager2(viewPager)
        }

        // Observe the categoryData LiveData
        viewModel.categoryData.observe(this) { categoryData ->
            // Update the adapter when the data changes
            recyclerView.adapter = CategoryAdapter(categoryData, this)
            progressBarCat.visibility = View.GONE // Hide the progress bar
        }

        // Observe the recommendedData LiveData
        viewModel.recommendedData.observe(this) { recommendedData ->
            // Update the adapter when the data changes
            recommendedRecyclerView.adapter = RecommendedAdapter(recommendedData)
            progressBarRec.visibility = View.GONE
        }
        // Set an OnClickListener for the cart image view
        imageViewCart.setOnClickListener {
            // Start the CartActivity
            println("Clicked")
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }
    }

    // Override onCategoryClick function from CategoryAdapter.OnCategoryClickListener
    override fun onCategoryClick(category: CategoryModel) {
        // Load items of the selected category
        viewModel.loadCategoryItems(category.name)
        viewModel.selectedCategoryData.observe(this) { selectedCategoryData ->
            // Update the adapter when the data changes
            recommendedRecyclerView.adapter = RecommendedAdapter(selectedCategoryData)
            progressBarRec.visibility = View.GONE
        }
    }

    // Override onSelectedCategoryClicked function from CategoryAdapter.OnCategoryClickListener
    override fun onSelectedCategoryClicked() {
        // Reset selected category data
        viewModel.resetSelectedCategoryData()
    }

}

// MainViewModelFactory class that implements ViewModelProvider.Factory
class MainViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            // Return an instance of MainViewModel
            return MainViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}