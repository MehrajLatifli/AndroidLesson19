package com.example.platzi.models.get


import com.google.gson.annotations.SerializedName

data class ProductResponse(
    @SerializedName("category")
    val category: Category?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("images")
    val images: List<String>?,
    @SerializedName("price")
    val price: Int?,
    @SerializedName("title")
    val title: String?
)