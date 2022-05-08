package com.example.barcodeapp.repository

import com.example.barcodeapp.data.model.category.CategoryResponse
import com.example.barcodeapp.data.model.product.ProductResponse
import com.example.barcodeapp.data.room.AppDatabase
import com.example.barcodeapp.data.room.entities.CategoryEntity
import com.example.barcodeapp.data.room.entities.ProductEntity
import com.example.barcodeapp.data.service.ServiceGenerator
import com.example.barcodeapp.data.service.Webservice
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class CodeRepository(private val appDatabase: AppDatabase, private val webservice: Webservice) {
    private val request = ServiceGenerator.createService(Webservice::class.java)

    // Product
    suspend fun getAllProduct(): Flow<Response<List<ProductResponse>>> {
        return flow { emit(request.getAllProducts()) }
    }

    suspend fun addDbProducts(list: List<ProductEntity>) {
        appDatabase.productDao().addList(list)
    }

    fun nameAndCodeSearchList(query: String,categoryId: String): List<ProductEntity> {
        return appDatabase.productDao().productByCodeSearch(query,categoryId)
    }

    fun getProductByProductId(productId: String): Flow<ProductEntity> {
        return flow { appDatabase.productDao().getProductByProductId(productId) }
    }

    suspend fun getDBProducts() = flow { emit(appDatabase.productDao().getAllProduct()) }

    //categoryId buyicha
    suspend fun getDbProductByCategoryId(categoryId: String) =
        flow { emit(appDatabase.productDao().getProductsByCategoryId(categoryId)) }

    //  Search
    suspend fun searchByBarCode(barcode: String?): Flow<ProductEntity> {
        return flow { emit(appDatabase.productDao().searchByBarCode(barcode)) }
    }


    // Category
        suspend fun addDbCategories(list: List<CategoryEntity>) {
        appDatabase.categoryDao().addList(list)
    }

    fun getDBCategories() = flow { emit(appDatabase.categoryDao().getAllCategory()) }


    suspend fun getAllCategory(): Flow<Response<List<CategoryResponse>>> {
        return flow { emit(request.getAllCategories()) }
    }
}