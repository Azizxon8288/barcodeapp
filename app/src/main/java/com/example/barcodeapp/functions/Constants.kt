package com.example.barcodeapp.functions

import android.util.Base64
import com.example.barcodeapp.data.room.entities.CategoryEntity
import com.example.barcodeapp.data.room.entities.ProductEntity

object Constants {
    var auth_username:String="Админстратор"
    var auth_password:String=""
    var ip: String = "http://185.196.214.27/PRICE_CHECKER/hs/price/"

    var basicAuth = "Basic " +
            Base64.encodeToString(String.format("%s:%s",
                auth_username, auth_password).toByteArray(), Base64.NO_WRAP)


    lateinit var productEntity: ProductEntity
    lateinit var categoryEntity: CategoryEntity

}