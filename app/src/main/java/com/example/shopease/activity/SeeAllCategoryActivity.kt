package com.example.shopease.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopease.MainActivity
import com.example.shopease.R
import com.example.shopease.adapter.CategoryAdapter
import com.example.shopease.adapter.RecommendedAdapter
import com.example.shopease.manager.FavoritesManager
import com.example.shopease.manager.FavoritesObserver
import com.example.shopease.model.CategoryModel
import com.example.shopease.viewModel.CategoryViewModel

class SeeAllCategoryActivity : AppCompatActivity(), CategoryAdapter.OnCategoryClickListener,
    FavoritesObserver {
    private lateinit var viewModel: CategoryViewModel
    private lateinit var recyclerView2: RecyclerView
    private lateinit var categoryNotSelected: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_see_all_category)

        categoryNotSelected = findViewById(R.id.categoryNotSelected)

        val recyclerView = findViewById<RecyclerView>(R.id.viewCategory)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        recyclerView2 = findViewById(R.id.viewProducts)
        recyclerView2.layoutManager = GridLayoutManager(this, 2)

        //Nav items
        val explorerItem = findViewById<ImageView>(R.id.explorerNav2)
        val cartItem =findViewById<ImageView>(R.id.cartNav2)
        val profileItem = findViewById<ImageView>(R.id.profileNav2)


        viewModel = ViewModelProvider(this, CategoryViewModelFactory(this))[CategoryViewModel::class.java]

        // Observe the categoryData LiveData
        viewModel.categoryData.observe(this) { categoryData ->
            // Update the adapter with the category data
            recyclerView.adapter = CategoryAdapter(categoryData, this, this)
        }

        // Observe the productData LiveData
        viewModel.productData.observe(this) { productData ->
            // Update the adapter with the product data
            recyclerView2.adapter = RecommendedAdapter(productData, this)
        }

        val backBtn = findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener {
            finish()
        }

        explorerItem.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        cartItem.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }

        profileItem.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCategoryClick(category: CategoryModel) {
        categoryNotSelected.visibility = View.GONE
        viewModel.loadCategoryItems(category.name)
        viewModel.selectedCategoryData.observe(this) { selectedCategoryData ->
            recyclerView2.adapter = RecommendedAdapter(selectedCategoryData, this)

        }
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

    override fun onFavoritesUpdated() {
        // Get the current adapter
        val adapter = recyclerView2.adapter

        // If the adapter is a RecommendedAdapter, notify it that the data set has changed
        if (adapter is RecommendedAdapter) {
            adapter.notifyDataSetChanged()
        }
    }
}
class CategoryViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CategoryViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}