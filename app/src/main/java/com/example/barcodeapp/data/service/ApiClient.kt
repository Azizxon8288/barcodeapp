package com.example.barcodeapp.data.service

import com.mocklets.pluto.PlutoInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    const val BASE_URL = "http://185.196.214.27/PRICE_CHECKER/hs/price/"


//    private fun logging(): HttpLoggingInterceptor {
//        val logging = HttpLoggingInterceptor()
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
//        return logging
//    }

    private fun client(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(PlutoInterceptor())
            .build()
    }

    fun getRetrofit(): Retrofit {
//        HttpLoggingInterceptor().level = HttpLoggingInterceptor.Level.BODY
//        val logging = HttpLoggingInterceptor()
//        logging.level = HttpLoggingInterceptor.Level.BODY
//
//        val client = OkHttpClient.Builder()
//            .addInterceptor(logging)
//            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client())
            .build()
    }

    val webservice: Webservice = getRetrofit().create(Webservice::class.java)
}