package com.example.barcodeapp.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.barcodeapp.data.mapper.category.mapToCategory
import com.example.barcodeapp.data.mapper.product.mapToProductList
import com.example.barcodeapp.functions.NetworkHelper
import com.example.barcodeapp.repository.CodeRepository
import com.example.barcodeapp.resource.CategoryResource
import com.example.barcodeapp.resource.ProductResource
import com.example.barcodeapp.resource.SearchResource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class CategoryViewModel(
    private val repository: CodeRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {


    fun searchByBarCode(barcode: String?): StateFlow<SearchResource> {
        val stateFlow = MutableStateFlow<SearchResource>(SearchResource.Loading)

        viewModelScope.launch {
            repository.searchByBarCode(barcode).catch {
                stateFlow.value = SearchResource.Error(it.message.toString())
            }.collect {
                Log.d("TAG", "searchByBarCode: ${it.barcodes}")
                stateFlow.value = SearchResource.Success(it)
            }
        }
        return stateFlow
    }

    fun getProducts(): StateFlow<ProductResource> {
        val stateFlow = MutableStateFlow<ProductResource>(ProductResource.Loading)
        viewModelScope.launch {
            if (networkHelper.isNetworkConnected()) {
                repository.getAllProduct().catch {
                    stateFlow.value = ProductResource.Error(it.message)
                }.collect {
                    if (it.isSuccessful) {
                        val body = it.body()
//                        val list2 = ArrayList<ProductEntity>()

//                        body?.forEach { it1 ->
//                            list2.add(
//                                ProductEntity(
//                                    it1.id,
//                                    it1.barcodes ?: emptyList(),
//                                    it1.code ?: 0,
//                                    it1.measurement ?: "",
//                                    it1.salesPrice ?: 0.0,
//                                    it1.name ?: it1.id,
//                                    it1.description ?: "",
//                                    it1.imageUrl ?: "",
//                                    it1.category.id
//                                )
//                            )
//                        }
                        repository.addDbProducts(body?.mapToProductList()?:emptyList())
//                        repository.addDbProducts(list2)
//                        stateFlow.value = ProductResource.Success(list2)
//                        stateFlow.emit(ProductResource.Success(list2))
                        stateFlow.value = ProductResource.Success(body?.mapToProductList()?:emptyList())
//                        stateFlow.emit(ProductResource.Success(body?.mapToProductList()?:emptyList()))
                    } else {
                        when {
                            it.code() in 400..499 -> {
                                stateFlow.value = ProductResource.Error("Client Error")
                            }
                            it.code() in 500..599 -> {
                                stateFlow.value = ProductResource.Error("Server Error")
                            }
                            else -> {
                                stateFlow.value = ProductResource.Error("Other Error")
                            }
                        }
                    }
                }
            } else {
                repository.getDBProducts().catch {
                    stateFlow.value = ProductResource.Error("Bazada malumot yuq")
                }.collect {
                    stateFlow.value = ProductResource.Success(it)
                }
            }

        }
        return stateFlow
    }

    fun getAllCategories(): StateFlow<CategoryResource> {
        val stateFlow = MutableStateFlow<CategoryResource>(CategoryResource.Loading)

        viewModelScope.launch {
            if (networkHelper.isNetworkConnected()) {
                repository.getAllCategory().catch {

                    repository.getDBCategories().catch {
                        stateFlow.value = CategoryResource.Error("Bazada malumot yuq")
                    }.collect {
                        stateFlow.value = CategoryResource.Success(it)
                    }
//                    stateFlow.value = CategoryResource.Error(it.message ?: "")
                }.collect { response ->
                    if (response.isSuccessful) {
                        val list = response.body()
//                        val list1 = ArrayList<CategoryEntity>()
//                        list?.forEach {
//                            list1.add(CategoryEntity(it.id, it.productCount, it.name, it.imageUrl))
//                        }

//                        list?.mapToCategory()
                        repository.addDbCategories(list?.mapToCategory()?: emptyList())
//                        stateFlow.value = CategoryResource.Success(repository.getDBCategories())

                        stateFlow.value = CategoryResource.Success(list?.mapToCategory()?: emptyList())
//                        stateFlow.emit(CategoryResource.Success(list?.mapToCategory()?: emptyList()))
                    } else {
//                        when {
//                            response.code() in 400..499 -> {
//                                stateFlow.value = CategoryResource.Error("Client Error")
//                            }
//                            response.code() in 500..599 -> {
//                                stateFlow.value = CategoryResource.Error("Server Error")
//                            }
//                            else -> {
//                                stateFlow.value = CategoryResource.Error("Other Error")
//                            }
//                        }
                        repository.getDBCategories().catch {
                            stateFlow.value = CategoryResource.Error("Bazada malumot yuq")
                        }.collect {
                            stateFlow.value = CategoryResource.Success(it)
                        }
                    }
                }
            } else {
                repository.getDBCategories().catch {
                    stateFlow.value = CategoryResource.Error("Bazada malumot yuq")
                }.collect {
                    stateFlow.value = CategoryResource.Success(it)
                }
            }
        }
        return stateFlow
    }
}