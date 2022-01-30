package com.example.barcodeapp.data.service

import com.example.barcodeapp.data.model.category.CategoryResponse
import com.example.barcodeapp.data.model.product.ProductResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers


interface Webservice {
    // 1/17/2022 getBranches
    //    @Headers("Authorization", "Basic ", ApiClient.TOKEN)

    //    @Headers("Authorization: Client-ID ${ApiClient.ClientId}")

    @Headers("Authorization: Basic ${ApiClient.TOKEN}")
    @GET("product/get")
    suspend fun getAllProducts(): Response<ArrayList<ProductResponse>>

    @Headers("Authorization: Basic ${ApiClient.TOKEN}")
    @GET("category/get")
    suspend fun getAllCategories(): Response<List<CategoryResponse>>
}