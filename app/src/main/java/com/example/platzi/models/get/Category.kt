package com.example.platzi.models.get


import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("name")
    val name: String?
)