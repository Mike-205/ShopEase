package com.example.shopease.util

import com.example.shopease.model.RecommendedModel

object SortUtils {
    fun sortRecommendedWithoutFilter(items: List<RecommendedModel>): List<RecommendedModel> {
        return items.asReversed()
    }
}