package com.example.shopease.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import androidx.recyclerview.widget.RecyclerView
import com.example.shopease.R
import com.example.shopease.model.CategoryModel



class CategoryAdapter(private val categoryList: List<CategoryModel>, private val listener: OnCategoryClickListener) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    interface OnCategoryClickListener {
        fun onCategoryClick(category: CategoryModel)
    }

    private var selectedPosition = RecyclerView.NO_POSITION

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.pic)
        val textView: TextView = itemView.findViewById(R.id.title1)
        val mainLayout: LinearLayout = itemView.findViewById(R.id.mainLayout)

        init {
            itemView.setOnClickListener {
                notifyItemChanged(selectedPosition)
                selectedPosition = layoutPosition
                notifyItemChanged(selectedPosition)
                listener.onCategoryClick(categoryList[selectedPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_category, parent, false)
        return CategoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val currentItem = categoryList[position]
        Glide.with(holder.itemView.context)
            .load(currentItem.imageResId)
            .fitCenter() // or use .centerCrop() if you want to fill the ImageView
            .into(holder.imageView)

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

    override fun getItemCount() = categoryList.size
}