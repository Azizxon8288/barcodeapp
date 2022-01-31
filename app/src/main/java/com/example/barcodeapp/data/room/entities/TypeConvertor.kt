package com.example.barcodeapp.data.room.entities

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TypeConvertor {

    @TypeConverter
    fun fromString(str: String): List<String> {
        val typeBarcodes = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(str, typeBarcodes)
    }

    @TypeConverter
    fun fromList(barcodes: List<String>): String {
        return Gson().toJson(barcodes)
    }
}