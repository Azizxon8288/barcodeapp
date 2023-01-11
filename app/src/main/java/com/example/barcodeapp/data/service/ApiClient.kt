package com.example.barcodeapp.data.service

import com.example.barcodeapp.functions.Constants.ip
import com.mocklets.pluto.PlutoInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
        const val BASE_URL = "http://185.196.214.27/PRICE_CHECKER/hs/price/"
//    const val BASE_URL = "https://jsonplaceholder.typicode.com/"
    const val TOKEN = "0JDQtNC80LjQvdGB0YLRgNCw0YLQvtGAOg=="
//    const val TOKEN="Basic 0JDQtNC80LjQvdGB0YLRgNCw0YLQvtGAOg=="

//    private fun logging(): HttpLoggingInterceptor {
//        val logging = HttpLoggingInterceptor()
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
//        return logging
//    }

//        .addInterceptor(object : Interceptor {
//            override fun intercept(chain: Interceptor.Chain): Response {
//
//                val request = Request.Builder()
//                    .addHeader("Authorization: Basic ${ApiClient.TOKEN}", "dsad")
//                    .build()
//                return chain.proceed(request)
//            }
//
//        })


    private fun client(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(PlutoInterceptor())
            .build()
    }


    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ip)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client())
            .build()
    }

    val webservice: Webservice = getRetrofit().create(Webservice::class.java)
}