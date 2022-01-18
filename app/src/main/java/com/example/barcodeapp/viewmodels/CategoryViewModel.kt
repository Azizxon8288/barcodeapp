package com.example.barcodeapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.barcodeapp.data.mapper.category.mapToCategory
import com.example.barcodeapp.data.room.entities.CategoryEntity
import com.example.barcodeapp.functions.NetworkHelper
import com.example.barcodeapp.repository.CodeRepository
import com.example.barcodeapp.resource.CategoryResource
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
                        repository.addDbCategories(list1)
                        stateFlow.value = CategoryResource.Success(repository.appDatabase.categoryDao().getAllCategory())
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
                        val list = response.body()
                        stateFlow.value = UsersResource.Success(response.body())
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
            }
        }

        return stateFlow
    }


}