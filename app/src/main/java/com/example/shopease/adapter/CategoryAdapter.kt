package com.example.shopease.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import androidx.recyclerview.widget.RecyclerView
import com.example.shopease.MainActivity
import com.example.shopease.R
import com.example.shopease.model.CategoryModel

// CategoryAdapter class that extends RecyclerView.Adapter
class CategoryAdapter(
    private val categoryList: List<CategoryModel>,
    private val context: Context,
    private val listener: OnCategoryClickListener // Add this line
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    // Interface for click listeners
    interface OnCategoryClickListener {
        fun onCategoryClick(category: CategoryModel)
        fun onSelectedCategoryClicked()
    }

    // Variable to keep track of the selected position
    private var selectedPosition = RecyclerView.NO_POSITION

    // ViewHolder class for each item in the RecyclerView
    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.pic)
        val textView: TextView = itemView.findViewById(R.id.title1)
        val mainLayout: LinearLayout = itemView.findViewById(R.id.mainLayout)

        // Initialize the ViewHolder
        init {
            itemView.setOnClickListener {
                if (layoutPosition == selectedPosition) {
                    notifyItemChanged(selectedPosition)
                    selectedPosition = RecyclerView.NO_POSITION
                    notifyItemChanged(selectedPosition)
                    listener.onSelectedCategoryClicked()
                } else {
                    notifyItemChanged(selectedPosition)
                    selectedPosition = layoutPosition
                    notifyItemChanged(selectedPosition)
                    listener.onCategoryClick(categoryList[selectedPosition])
                }
            }
        }
    }

    // Function to create a new ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_category, parent, false)
        return CategoryViewHolder(itemView)
    }

    // Function to bind data to a ViewHolder
    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val currentItem = categoryList[position]
        Glide.with(holder.itemView.context)
            .load(currentItem.imageResId)
            .fitCenter() // or use .centerCrop() if you want to fill the ImageView
            .into(holder.imageView)

        // Change the appearance based on whether the item is selected
        if (position == selectedPosition) {
            holder.textView.text = currentItem.name
            holder.textView.visibility = View.VISIBLE
            holder.mainLayout.setBackgroundResource(R.drawable.purple_bg) // Set the background to purple_bg
            holder.imageView.setBackgroundResource(R.drawable.purple_bg) // Set the ImageView background to purple_bg
        } else {
            holder.textView.visibility = View.GONE
            holder.mainLayout.setBackgroundResource(R.drawable.grey_bg) // Set the background to grey_bg
            holder.imageView.setBackgroundResource(R.drawable.grey_bg) // Set the ImageView background to grey_bg
        }
    }

    // Function to get the number of items in the RecyclerView
    override fun getItemCount() = categoryList.size
}