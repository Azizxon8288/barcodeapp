package com.example.barcodeapp.repository

import com.example.barcodeapp.data.mapper.category.mapToCategory
import com.example.barcodeapp.data.model.GithubUser
import com.example.barcodeapp.data.model.category.CategoryResponse
import com.example.barcodeapp.data.room.AppDatabase
import com.example.barcodeapp.data.room.entities.CategoryEntity
import com.example.barcodeapp.data.room.entities.ProductEntity
import com.example.barcodeapp.data.service.ApiClient
import com.example.barcodeapp.data.service.Webservice
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class CodeRepository(val appDatabase: AppDatabase, val webservice: Webservice) {
    fun getAllCategory(): Flow<Response<List<CategoryResponse>>> {
        return flow { emit(webservice.getAllCategories()) }
    }

    fun getUsers(): Flow<Response<List<GithubUser>>> {
        return flow { emit(webservice.getUsers()) }
    }

    fun searchByBarCode(barcode: String?): Flow<ProductEntity> {
        return flow { emit(appDatabase.productDao().searchByBarCode(barcode)) }
    }

    fun addDbCategories(list: List<CategoryEntity>) {
        appDatabase.categoryDao().addList(list)
    }

    fun getDBCategories(): Flow<List<CategoryEntity>> {
        return flow { emit(appDatabase.categoryDao().getAllCategory()) }
    }
}