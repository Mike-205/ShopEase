package com.example.shopease.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
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
import com.example.shopease.util.SortUtils
import com.example.shopease.viewModel.CategoryViewModel

class SeeAllCategoryActivity : AppCompatActivity(), CategoryAdapter.OnCategoryClickListener,
    FavoritesObserver {
    private lateinit var viewModel: CategoryViewModel
    private lateinit var recyclerView2: RecyclerView
    private lateinit var categoryNotSelected: TextView
    private lateinit var filterButton: ImageView
    private lateinit var sortIcon: ImageView

    private var initialArrangement = true
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_see_all_category)

        categoryNotSelected = findViewById(R.id.categoryNotSelected)
        sortIcon = findViewById(R.id.sort2)
        filterButton = findViewById(R.id.filter2)

        val recyclerView = findViewById<RecyclerView>(R.id.viewCategory)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        recyclerView2 = findViewById(R.id.viewProducts)
        recyclerView2.layoutManager = GridLayoutManager(this, 2)

        // Initially hide the sort and filter icons
        filterButton.visibility = View.GONE
        sortIcon.visibility = View.GONE

        //Nav items
        val explorerItem = findViewById<ImageView>(R.id.explorerNav2)
        val cartItem =findViewById<ImageView>(R.id.cartNav2)
        val profileItem = findViewById<ImageView>(R.id.profileNav2)
        val filterButton: ImageView = findViewById(R.id.filter2)

        filterButton.setOnClickListener {
            // Create a Dialog
            val dialog = Dialog(this)

            // Set the layout of the Dialog to filter_options_viewholder
            dialog.setContentView(R.layout.filter_options_viewholder)

            // Get the Window object of the Dialog
            val window = dialog.window

            // Set the gravity to top and end (right)
            window?.setGravity(Gravity.TOP or Gravity.END)

            // Get the screen dimensions
            val displayMetrics = resources.displayMetrics
            val width = displayMetrics.widthPixels
            val height = displayMetrics.heightPixels

            // Calculate the position of the Dialog
            val xPos = (width * 0.2).toInt()  // 10% from the right
            val yPos = (height * 0.3).toInt()  // 10% from the top

            // Set the position of the Dialog
            window?.attributes?.x = xPos
            window?.attributes?.y = yPos

            // Set the background of the dialog to transparent
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            // Display the Dialog
            dialog.show()
        }
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
        // Show the sort and filter icons when a category is clicked
        filterButton.visibility = View.VISIBLE
        sortIcon.visibility = View.VISIBLE
        initialArrangement = true
        sortIcon.setImageResource(R.drawable.down_arrow)
        viewModel.loadCategoryItems(category.name)
        viewModel.selectedCategoryData.observe(this) { selectedCategoryData ->
            recyclerView2.adapter = RecommendedAdapter(selectedCategoryData, this)
            sortIcon.setOnClickListener{
                Log.d("SeeAllCategoryActivity", "Sort icon clicked")
                // Reverse the sort order for the next click
                initialArrangement = !initialArrangement

                // Change the sort icon
                if (initialArrangement) {
                    recyclerView2.adapter = RecommendedAdapter(selectedCategoryData, this)
                    sortIcon.setImageResource(R.drawable.down_arrow)
                } else {
                    recyclerView2.adapter = RecommendedAdapter(SortUtils.sortRecommendedWithoutFilter(selectedCategoryData), this)
                    sortIcon.setImageResource(R.drawable.up_arrow)
                }
            }
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