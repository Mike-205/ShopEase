// Package declaration
package com.example.shopease.adapter

// Import statements
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopease.R
import com.example.shopease.model.SliderModel

// SliderAdapter class that extends RecyclerView.Adapter
class SliderAdapter(private val context: Context, private val sliderData: List<SliderModel>) : RecyclerView.Adapter<SliderAdapter.SliderViewHolder>() {

    // ViewHolder class for each item in the RecyclerView
    inner class SliderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageSlider)
    }

    // Function to create a new ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        // Inflate the view for each item
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.slider_item_container, parent, false)
        return SliderViewHolder(view)
    }

    // Function to bind data to a ViewHolder
    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        // Use Glide to load the image into the ImageView
        Glide.with(context).load(sliderData[position].imageResId).into(holder.imageView)
    }

    // Function to get the number of items in the RecyclerView
    override fun getItemCount(): Int {
        return sliderData.size
    }
}