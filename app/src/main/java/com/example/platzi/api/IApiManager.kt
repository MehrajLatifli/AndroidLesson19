package com.example.platzi.api

import com.example.platzi.models.get.Category
import com.example.platzi.models.get.ProductResponse
import com.example.platzi.models.post.NewCategory
import com.example.platzi.models.post.NewProduct
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface IApiManager {

    @Headers("Content-Type: application/json")


    @GET("/api/v1/products")
    suspend fun getAllProducts(): Response<List<ProductResponse>>


    @GET("/api/v1/products/{productId}")
    suspend fun getProductById(
        @Path("productId") productId: Int
    ): Response<ProductResponse>

    @GET("/api/v1/products/")
    suspend fun getProductsByTitle(
        @Query("title") title: String
    ): Response<ProductResponse>


    @GET("/api/v1/products/")
    suspend fun getProductsByPrice(
        @Query("price") price: Int
    ): Response<ProductResponse>


    @GET("/api/v1/products/")
    suspend fun getProductsByPriceRange(
        @Query("price_min") minPrice: Int,
        @Query("price_max") maxPrice: Int
    ): Response<ProductResponse>


    @GET("/api/v1/products/")
    suspend fun getProductsByCategory(
        @Query("categoryId") categoryId: Int
    ): Response<ProductResponse>


    @GET("/api/v1/categories")
    suspend fun getCategories(): Response<List<Category>>

    @GET("/api/v1/categories/{categoryId}")
    suspend fun getCategoryById(
        @Path("categoryId") categoryId: Int
    ): Response<Category>


    @POST("/api/v1/categories")
    suspend fun createCategory(@Body request: NewCategory): Response<NewCategory>


    @Multipart
    @POST("/api/v1/products")
    suspend fun createProduct(
        @Part("title") title: RequestBody,
        @Part("price") price: RequestBody,
        @Part("description") description: RequestBody,
        @Part("categoryId") categoryId: RequestBody,
        @Part images: List<MultipartBody.Part>
    ): Response<ProductResponse>
}