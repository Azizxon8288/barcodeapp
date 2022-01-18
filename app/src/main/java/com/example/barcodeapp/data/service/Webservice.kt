package com.example.barcodeapp.data.service

import com.example.barcodeapp.data.model.GithubUser
import com.example.barcodeapp.data.model.category.CategoryResponse
import com.example.barcodeapp.data.model.product.ProductResponse
import retrofit2.Response
import retrofit2.http.GET

interface Webservice {
    // 1/17/2022 getBranches


    @GET()
    suspend fun getAllProducts(): Response<List<ProductResponse>>

    @GET()
    suspend fun getAllCategories(): Response<List<CategoryResponse>>

    @GET("users")
    suspend fun getUsers(): Response<List<GithubUser>>
}