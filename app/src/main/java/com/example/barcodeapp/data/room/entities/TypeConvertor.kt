package com.example.barcodeapp.data.room.entities

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TypeConvertor {

    @TypeConverter
    fun fromString(str: String): ArrayList<String> {
        val typeBarcodes = object : TypeToken<ArrayList<String>>() {}.type
        return Gson().fromJson(str, typeBarcodes)
    }

    @TypeConverter
    fun fromArrayList(barcodes: ArrayList<String>): String {
        return Gson().toJson(barcodes)
    }
}