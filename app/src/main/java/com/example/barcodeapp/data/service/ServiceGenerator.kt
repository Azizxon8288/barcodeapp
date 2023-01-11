package com.example.barcodeapp.data.service

import android.content.Context
import com.example.barcodeapp.functions.Constants
import com.example.barcodeapp.functions.getIpAddress
import com.mocklets.pluto.PlutoInterceptor
import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object ServiceGenerator {
    private val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
    private val builder = Retrofit.Builder()
    private lateinit var retrofit: Retrofit


    fun <S> createService(serviceClass: Class<S>, context: Context): S {
        val username: String = Constants.auth_username
        val password: String = Constants.auth_password
        builder.addConverterFactory(GsonConverterFactory.create())
        builder.baseUrl(getIpAddress(context).toString())
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
                    val httpLoggingInterceptor = HttpLoggingInterceptor()
                    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                    httpClient.addInterceptor(httpLoggingInterceptor)
                    httpClient.addInterceptor(interceptor)
                    httpClient.addInterceptor(PlutoInterceptor())
                    httpClient.connectTimeout(60, TimeUnit.SECONDS)
                    httpClient.readTimeout(60, TimeUnit.SECONDS)
                    httpClient.writeTimeout(60, TimeUnit.SECONDS)
//                    builder.client(client())
                    builder.client(httpClient.build())
                    retrofit = builder.build()
                }
            }
        }
        return retrofit.create(serviceClass)
    }

}