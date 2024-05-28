package com.example.platzi.models.post

import okhttp3.MultipartBody

data class NewProduct(
    val title: String,
    val price: Int,
    val description: String,
    val categoryId: Int,
    val images: List<String>
)

