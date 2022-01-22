package com.example.barcodeapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.barcodeapp.data.mapper.category.mapToCategory
import com.example.barcodeapp.data.room.entities.CategoryEntity
import com.example.barcodeapp.data.room.entities.ProductEntity
import com.example.barcodeapp.functions.NetworkHelper
import com.example.barcodeapp.repository.CodeRepository
import com.example.barcodeapp.resource.CategoryResource
import com.example.barcodeapp.resource.SearchResource
import com.example.barcodeapp.resource.UsersResource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.newCoroutineContext

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
                Log.d("TAG", "searchByBarCode: ${it.barcode}")
                stateFlow.value = SearchResource.Success(it)
            }
        }
        return stateFlow
    }

    fun getAllCategories(): StateFlow<CategoryResource> {
        val stateFlow = MutableStateFlow<CategoryResource>(CategoryResource.Loading)

        viewModelScope.launch {
            if (networkHelper.isNetworkConnected()) {
                repository.getAllCategory().catch {
                    stateFlow.value = CategoryResource.Error(it.message ?: "")
                }.collect { response ->
                    if (response.isSuccessful) {
                        val list = response.body()
                        val list1 = ArrayList<CategoryEntity>()
                        list?.forEach {
                            list1.add(CategoryEntity(it.id, it.productCount, it.name, it.imageUrl))
                        }
//                        list?.mapToCategory()
                        repository.addDbCategories(list1)
//                        stateFlow.value = CategoryResource.Success(repository.getDBCategories())
                        stateFlow.value = CategoryResource.Success(list1)
                        stateFlow.emit(CategoryResource.Success(list1))
                    } else {
                        when {
                            response.code() in 400..499 -> {
                                stateFlow.value = CategoryResource.Error("Client Error")
                            }
                            response.code() in 500..599 -> {
                                stateFlow.value = CategoryResource.Error("Server Error")
                            }
                            else -> {
                                stateFlow.value = CategoryResource.Error("Other Error")
                            }
                        }
                    }
                }
            } else {
                // db yoziladi
                repository.getDBCategories().catch {
                    stateFlow.value = CategoryResource.Error("Bazada malumot yuq")
                }.collect {
                    stateFlow.value = CategoryResource.Success(it)
                }
            }
        }

        return stateFlow
    }

    fun getUsers(): StateFlow<UsersResource> {
        val stateFlow = MutableStateFlow<UsersResource>(UsersResource.Loading)

        viewModelScope.launch {
            if (networkHelper.isNetworkConnected()) {
                repository.getUsers().catch {
                    stateFlow.value = UsersResource.Error(it.message ?: "")
                }.collect { response ->
                    if (response.isSuccessful) {
                        stateFlow.value = UsersResource.Success(response.body())
//                        stateFlow.emit(UsersResource.Success(response.body()))
                    } else {
                        when {
                            response.code() in 400..499 -> {
                                stateFlow.value = UsersResource.Error("Client Error")
                            }
                            response.code() in 500..599 -> {
                                stateFlow.value = UsersResource.Error("Server Error")
                            }
                            else -> {
                                stateFlow.value = UsersResource.Error("Other Error")
                            }
                        }
                    }
                }
            } else {
                // db yoziladi
            }
        }

        return stateFlow
    }


}