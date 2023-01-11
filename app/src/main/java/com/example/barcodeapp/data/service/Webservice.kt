package com.example.barcodeapp.data.service

import com.example.barcodeapp.data.model.category.CategoryResponse
import com.example.barcodeapp.data.model.product.Data
import com.example.barcodeapp.data.model.product.ProductPagerResponse
import com.example.barcodeapp.data.model.product.ProductResponse
import com.example.barcodeapp.functions.Constants.basicAuth
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query


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

    @GET("product/get/{barcode}")
    suspend fun getOneProduct(
        @Header("Authorization") authorization: String = basicAuth,
        @Path("barcode") barcode: String
    ): Response<List<ProductResponse>>

    @GET("product/get")
    suspend fun getProduct(
        @Header("Authorization") authorization: String = basicAuth,
        @Query("page") page: Int,
        @Query("limit") limit: Int = 500
    ): Response<ProductPagerResponse>


    @GET("product/changes/{time}")
    suspend fun getProductsChanges(
        @Header("Authorization") authorization: String = basicAuth,
        @Path("time") time: Int
    ) :Response<List<Data>>
}