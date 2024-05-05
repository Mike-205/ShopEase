package com.example.shopease

import com.example.shopease.adapter.CategoryAdapter
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.shopease.activity.CartActivity
import com.example.shopease.activity.ProfileActivity
import com.example.shopease.activity.SeeAllCategoryActivity
import com.example.shopease.adapter.RecommendedAdapter
import com.example.shopease.adapter.SliderAdapter
import com.example.shopease.manager.FavoritesManager
import com.example.shopease.manager.FavoritesObserver
import com.example.shopease.model.CategoryModel
import com.example.shopease.viewModel.MainViewModel
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator

class MainActivity : AppCompatActivity(), CategoryAdapter.OnCategoryClickListener,
    FavoritesObserver {
    private lateinit var viewModel: MainViewModel
    private lateinit var recommendedRecyclerView: RecyclerView
    private lateinit var progressBarRec: ProgressBar

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize FavoritesManager
        FavoritesManager.initialize(this)
        viewModel = ViewModelProvider(this, MainViewModelFactory(this))[MainViewModel::class.java]

        val dotsIndicator = findViewById<DotsIndicator>(R.id.dotsIndicator)
        val viewPager = findViewById<ViewPager2>(R.id.viewpageSlider)
        val recyclerView = findViewById<RecyclerView>(R.id.viewCategory)
        val progressBarCat = findViewById<ProgressBar>(R.id.progressBarCategory)
        val bottomNavCart: ImageView = findViewById(R.id.cartNav)
        val bottomNavProfile: ImageView = findViewById(R.id.profileNav)

        recommendedRecyclerView = findViewById(R.id.viewRecommended)
        progressBarRec = findViewById(R.id.progressBarRecommended)

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recommendedRecyclerView.layoutManager = GridLayoutManager(this, 2)


        viewModel.sliderData.observe(this) { sliderData ->
            viewPager.adapter = SliderAdapter(this, sliderData)
            dotsIndicator.setViewPager2(viewPager)
        }

        viewModel.categoryData.observe(this) { categoryData ->
            val firstFiveCategories = categoryData.take(5)
            recyclerView.adapter = CategoryAdapter(firstFiveCategories, this, this)
            progressBarCat.visibility = View.GONE
        }

        viewModel.recommendedData.observe(this) { recommendedData ->
            recommendedRecyclerView.adapter = RecommendedAdapter(recommendedData, this)
            progressBarRec.visibility = View.GONE
        }

        viewModel.selectedCategoryData.observe(this) { selectedCategoryData ->
            recommendedRecyclerView.adapter = RecommendedAdapter(selectedCategoryData, this)
            progressBarRec.visibility = View.GONE
        }

        bottomNavCart.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }

        bottomNavProfile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        val seeAllText = findViewById<TextView>(R.id.textView4)
        seeAllText.setOnClickListener {
            val intent = Intent(this@MainActivity, SeeAllCategoryActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCategoryClick(category: CategoryModel) {
        viewModel.loadCategoryItems(category.name)
    }

    override fun onSelectedCategoryClicked() {
        viewModel.resetSelectedCategoryData()
    }
    override fun onResume() {
        super.onResume()
        FavoritesManager.addObserver(this)
        onFavoritesUpdated()
    }

    override fun onPause() {
        super.onPause()
        FavoritesManager.removeObserver(this)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onFavoritesUpdated() {
        // Get the current adapter
        val adapter = recommendedRecyclerView.adapter

        // If the adapter is a RecommendedAdapter, notify it that the data set has changed
        if (adapter is RecommendedAdapter) {
            adapter.notifyDataSetChanged()
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