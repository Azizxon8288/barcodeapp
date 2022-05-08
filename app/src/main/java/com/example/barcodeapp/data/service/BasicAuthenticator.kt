package com.example.barcodeapp.data.service

import okhttp3.*

class BasicAuthenticator : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        return response.request
            .newBuilder()
            .header("Authorization", Credentials.basic("Админстратор",""))
            .build()
    }
}