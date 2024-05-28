package com.example.platzi.repositories

import com.example.platzi.api.IApiManager
import com.example.platzi.models.get.Category
import com.example.platzi.models.get.ProductResponse
import com.example.platzi.models.post.NewCategory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class ProductRepositories @Inject constructor(private val api: IApiManager) {



    suspend fun getAllProducts(): Response<List<ProductResponse>> {
        return api.getAllProducts()
    }

    suspend fun getProductById(productId: Int): Response<ProductResponse> {
        return api.getProductById(productId)
    }

    suspend fun getProductsByTitle(title: String): Response<ProductResponse> {
        return api.getProductsByTitle(title)
    }

    suspend fun getProductsByPrice(price: Int): Response<ProductResponse> {
        return api.getProductsByPrice(price)
    }

    suspend fun getProductsByPriceRange(minPrice: Int, maxPrice: Int): Response<ProductResponse> {
        return api.getProductsByPriceRange(minPrice,maxPrice)
    }

    suspend fun getProductsByCategory(categoryId: Int): Response<ProductResponse> {
        return api.getProductsByCategory(categoryId)
    }

    suspend fun getCategories(): Response<List<Category>> {
        return api.getCategories()
    }

    suspend fun createCategory(request: NewCategory): Response<NewCategory> {
        return api.createCategory(request)
    }

    suspend fun createProduct(
        title: RequestBody,
        price: RequestBody,
        description: RequestBody,
        categoryId: RequestBody,
        images: List<MultipartBody.Part>
    ): Response<ProductResponse> {
        return api.createProduct(title, price, description, categoryId, images)
    }



}