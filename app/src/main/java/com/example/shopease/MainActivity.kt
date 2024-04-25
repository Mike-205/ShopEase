package com.example.shopease

import com.example.shopease.adapter.CategoryAdapter
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.GridLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.shopease.adapter.RecommendedAdapter
import com.example.shopease.adapter.SliderAdapter
import com.example.shopease.viewModel.MainViewModel
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        viewModel = ViewModelProvider(this, MainViewModelFactory(this))[MainViewModel::class.java]

        // Find views after setting the content view
        val dotsIndicator = findViewById<DotsIndicator>(R.id.dotsIndicator)
        val viewPager = findViewById<ViewPager2>(R.id.viewpageSlider)
        val recyclerView = findViewById<RecyclerView>(R.id.viewCategory)
        val progressBarCat = findViewById<ProgressBar>(R.id.progressBarCategory)
        val recommendedRecyclerView = findViewById<RecyclerView>(R.id.viewRecommended)
        val progressBarRec = findViewById<ProgressBar>(R.id.progressBarRecommended)

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recommendedRecyclerView.layoutManager = GridLayoutManager(this, 2)


        // Observe the sliderData LiveData
        viewModel.sliderData.observe(this, { sliderData ->
            // Update the adapter when the data changes
            viewPager.adapter = SliderAdapter(this, sliderData)
            dotsIndicator.setViewPager2(viewPager)
        })

        viewModel.categoryData.observe(this) { categoryData ->
            recyclerView.adapter = CategoryAdapter(categoryData)
            progressBarCat.visibility = View.GONE // Hide the progress bar
        }

        viewModel.recommendedData.observe(this) { recommendedData ->
            recommendedRecyclerView.adapter = RecommendedAdapter(recommendedData)
            progressBarRec.visibility = View.GONE
        }
    }
}

class MainViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}