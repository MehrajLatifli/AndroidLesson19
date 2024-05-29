package com.example.platzi.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.example.platzi.models.get.Category
import com.example.platzi.models.get.ProductResponse
import com.example.platzi.models.post.NewProduct
import com.example.platzi.api.repositories.ProductRepositories
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repo: ProductRepositories) : ViewModel() {



    private val _products = MutableLiveData<List<ProductResponse>>()
    val products: LiveData<List<ProductResponse>> = _products

    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> = _categories

    private var originalCategoryList = listOf<Category>()
    private var originalProductList = listOf<ProductResponse>()

    val loading = MutableLiveData<Boolean>()
    val error = MutableLiveData<String?>()

    fun getAllCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repo.getCategories()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val categories = response.body()
                        if (categories != null && categories.isNotEmpty()) {
                            _categories.value = categories!!
                            originalCategoryList = categories
                        } else {
                            error.value = "No categories found"
                            _categories.value = emptyList()
                        }
                    } else {
                        error.value = "Failed to fetch categories: ${response.message()}"
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    error.value = e.localizedMessage ?: "An error occurred"
                }
            } finally {
                withContext(Dispatchers.Main) {
                    loading.value = false
                }
            }
        }
    }


    fun getAllProducts() {
        loading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repo.getAllProducts()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val products = response.body()
                        if (products != null && products.isNotEmpty()) {
                            _products.value = products!!
                            originalProductList = products
                        } else {
                            error.value = "No products found"
                            _products.value = emptyList()
                        }
                    } else {
                        error.value = "Failed to fetch products: ${response.message()}"
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    error.value = e.localizedMessage ?: "An error occurred"
                }
            } finally {
                withContext(Dispatchers.Main) {
                    loading.value = false
                }
            }
        }
    }

    fun createProduct(newProduct: NewProduct, images: List<MultipartBody.Part>) {
        loading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val titleRequestBody = newProduct.title.toRequestBody("text/plain".toMediaTypeOrNull())
                val priceRequestBody = newProduct.price.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                val descriptionRequestBody = newProduct.description.toRequestBody("text/plain".toMediaTypeOrNull())
                val categoryIdRequestBody = newProduct.categoryId.toString().toRequestBody("text/plain".toMediaTypeOrNull())


                val response = repo.createProduct(
                    titleRequestBody,
                    priceRequestBody,
                    descriptionRequestBody,
                    categoryIdRequestBody,
                    images
                )
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val createdProduct = response.body()
                        if (createdProduct != null) {
                            // Handle success
                        } else {
                            error.value = "Failed to create product: Response body is null"
                            Log.e("responseError", error.value.toString())
                            error.value=null
                        }
                    } else {
                        error.value = "Failed to create product: ${response.message()}"
                        Log.e("responseError", error.value.toString())
                        error.value=null
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    error.value = e.localizedMessage ?: "An error occurred"
                    Log.e("responseError", e.localizedMessage!!)
                    error.value=null
                }
            } finally {
                withContext(Dispatchers.Main) {
                    loading.value = false
                }
            }
        }
    }

    fun searchProducts(query: String) {
        if (query.isBlank()) {
            _products.value = originalProductList
            return
        }

        val filteredProducts = originalProductList.filter { product ->
            product.title!!.contains(query, ignoreCase = true)
        }
        _products.value = filteredProducts
    }
}
