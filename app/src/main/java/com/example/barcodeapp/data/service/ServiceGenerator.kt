package com.example.barcodeapp.data.service

import com.example.barcodeapp.functions.Constants
import okhttp3.Credentials
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object  ServiceGenerator {
    private val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
    private val builder = Retrofit.Builder()
    private lateinit var retrofit: Retrofit


    fun <S> createService(serviceClass: Class<S>): S {
        val username: String = Constants.auth_username
        val password: String = Constants.auth_password
        builder.addConverterFactory(GsonConverterFactory.create())
        builder.baseUrl(Constants.ip)
        retrofit = builder.build()
        if (username.isNotEmpty() && password.isNotEmpty()) {
            val authToken: String = Credentials.basic(username, password)
            return createService(serviceClass, authToken)
        }
        return createService(serviceClass, null as String?)
    }

//    private fun client(): OkHttpClient {
//        return OkHttpClient.Builder()
//            .addInterceptor(PlutoInterceptor())
//            .build()
//    }

    private fun <S> createService(serviceClass: Class<S>, authToken: String?): S {
        if (authToken != null) {
            if (authToken.isNotEmpty()) {
                val interceptor = AuthenticationInterceptor(authToken)
                if (!httpClient.interceptors().contains(interceptor)) {
                    httpClient.addInterceptor(interceptor)
//                    builder.client(client())
                    builder.client(httpClient.build())
                    retrofit = builder.build()
                }
            }
        }
        return retrofit.create(serviceClass)
    }

}