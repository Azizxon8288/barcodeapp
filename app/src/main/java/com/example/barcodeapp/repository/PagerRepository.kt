package com.example.barcodeapp.repository

import android.content.Context
import com.example.barcodeapp.data.room.AppDatabase
import com.example.barcodeapp.data.room.entities.ProductEntity
import com.example.barcodeapp.data.service.ServiceGenerator
import com.example.barcodeapp.data.service.Webservice
import kotlinx.coroutines.flow.flow

class PagerRepository(
    context: Context,
    private val appDatabase: AppDatabase
) {

    private val request = ServiceGenerator.createService(Webservice::class.java, context)

    suspend fun getProducts(page: Int) = flow { emit(request.getProduct(page = page)) }

    suspend fun addDbProducts(list: List<ProductEntity>) {
        appDatabase.productDao().addList(list)
    }
}