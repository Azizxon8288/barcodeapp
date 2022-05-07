package com.example.barcodeapp.data.service

import com.example.barcodeapp.data.model.category.CategoryResponse
import com.example.barcodeapp.data.model.product.ProductResponse
import com.example.barcodeapp.functions.Constants.basicAuth
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header


interface Webservice {
    // 1/17/2022 getBranches
    //    @Headers("Authorization", "Basic ", ApiClient.TOKEN)

    //    @Headers("Authorization: Client-ID ${ApiClient.ClientId}")

    //    @Headers("Authorization: Basic ${ApiClient.TOKEN}")
    @GET("product/get")
    suspend fun getAllProducts(@Header("Authorization") authorization: String = basicAuth): Response<List<ProductResponse>>

    //    @Headers("Authorization: Basic ${ApiClient.TOKEN}")
    @GET("category/get")
    suspend fun getAllCategories(@Header("Authorization") authorization: String = basicAuth): Response<List<CategoryResponse>>
}