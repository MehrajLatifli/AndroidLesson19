package com.example.platzi.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.platzi.models.get.ProductResponse
import com.example.platzi.api.repositories.ProductRepositories
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val repo: ProductRepositories): ViewModel() {



    private val _item = MutableLiveData<ProductResponse>()
    val item: LiveData<ProductResponse> = _item

    val isLoading = MutableLiveData<Boolean>()
    val error = MutableLiveData<String?>()
    val loading = MutableLiveData<Boolean>()

    fun getProductById(id: Int) {
        isLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repo.getProductById(id)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val product = response.body()
                        if (product != null) {
                            _item.value = product!!
                        } else {
                            error.value = "Product not found"
                        }
                    } else {
                        error.value = "Failed to fetch product: ${response.message()}"
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
}
