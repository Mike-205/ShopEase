package com.example.shopease

import com.example.shopease.adapter.CategoryAdapter
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
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
import com.example.shopease.manager.AuthServices
import com.example.shopease.manager.FavoritesManager
import com.example.shopease.manager.FavoritesObserver
import com.example.shopease.model.CategoryModel
import com.example.shopease.model.UserModel
import com.example.shopease.util.SortUtils
import com.example.shopease.viewModel.MainViewModel
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator

class MainActivity : AppCompatActivity(), CategoryAdapter.OnCategoryClickListener,
    FavoritesObserver {
    private lateinit var authServices: AuthServices
    private lateinit var viewModel: MainViewModel
    private lateinit var recommendedRecyclerView: RecyclerView
    private lateinit var progressBarRec: ProgressBar
    private lateinit var sharesPrefs: SharedPreferences
    private lateinit var sortIcon: ImageView

    private var initialArrangement = true
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize FavoritesManager & AuthServices
        FavoritesManager.initialize(this)
        authServices = AuthServices(this)
        viewModel = ViewModelProvider(this, MainViewModelFactory(this))[MainViewModel::class.java]

        val dotsIndicator = findViewById<DotsIndicator>(R.id.dotsIndicator)
        val viewPager = findViewById<ViewPager2>(R.id.viewpageSlider)
        val recyclerView = findViewById<RecyclerView>(R.id.viewCategory)
        val progressBarCat = findViewById<ProgressBar>(R.id.progressBarCategory)
        val bottomNavCart: ImageView = findViewById(R.id.cartNav)
        val bottomNavProfile: ImageView = findViewById(R.id.profileNav)
        val firstUserName: TextView = findViewById(R.id.textView)
        sortIcon = findViewById(R.id.sort2)
        val filterButton: ImageView = findViewById(R.id.filter2)

        filterButton.setOnClickListener {
            // Create a Dialog
            val dialog = Dialog(this)

            // Set the layout of the Dialog to filter_options_viewholder
            dialog.setContentView(R.layout.filter_options_viewholder)

            // Get the Window object of the Dialog
            val window = dialog.window

            // Set the gravity to top and end (right)
            window?.setGravity(Gravity.CENTER or Gravity.END)

            // Get the screen dimensions
            val displayMetrics = resources.displayMetrics
            val width = displayMetrics.widthPixels
            val height = displayMetrics.heightPixels

            // Calculate the position of the Dialog
            val xPos = (width * 0.2).toInt()  // 10% from the right
            val yPos = (height * 0.05).toInt()  // 10% from the top

            // Set the position of the Dialog
            window?.attributes?.x = xPos
            window?.attributes?.y = yPos

            // Set the background of the dialog to transparent
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            // Display the Dialog
            dialog.show()
        }

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

            sortIcon.setOnClickListener{

                // Reverse the sort order for the next click
                initialArrangement = !initialArrangement

                // Change the sort icon
                if (initialArrangement) {
                    recommendedRecyclerView.adapter = RecommendedAdapter(recommendedData, this)
                    sortIcon.setImageResource(R.drawable.up_arrow)
                } else {
                    recommendedRecyclerView.adapter = RecommendedAdapter(SortUtils.sortRecommendedWithoutFilter(recommendedData), this)
                    sortIcon.setImageResource(R.drawable.down_arrow)
                }
            }
        }

        viewModel.selectedCategoryData.observe(this) { selectedCategoryData ->
            recommendedRecyclerView.adapter = RecommendedAdapter(selectedCategoryData, this)
            progressBarRec.visibility = View.GONE
            sortIcon.setOnClickListener{

                // Reverse the sort order for the next click
                initialArrangement = !initialArrangement

                // Change the sort icon
                if (initialArrangement) {
                    recommendedRecyclerView.adapter = RecommendedAdapter(selectedCategoryData, this)
                    sortIcon.setImageResource(R.drawable.down_arrow)
                } else {
                    recommendedRecyclerView.adapter = RecommendedAdapter(SortUtils.sortRecommendedWithoutFilter(selectedCategoryData), this)
                    sortIcon.setImageResource(R.drawable.up_arrow)
                }
            }
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

        //Update the userName
        sharesPrefs = authServices.sharedPreferences
        val userJson = sharesPrefs.getString(AuthServices.userEmail, null)
        val user = authServices.gson.fromJson(userJson, UserModel::class.java)
        val fullUserName = user.username
        val firstName = fullUserName.split(" ")[0]

        firstUserName.text =firstName
    }

    override fun onCategoryClick(category: CategoryModel) {
        // Reset the initial arrangement
        initialArrangement = true

        // Change the sort icon to up_arrow
        sortIcon.setImageResource(R.drawable.down_arrow)
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
    override fun onBackPressed() {
        super.onBackPressed()
        // This will close the app
        finishAffinity()
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