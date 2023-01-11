package com.example.barcodeapp.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.barcodeapp.data.room.AppDatabase
import com.example.barcodeapp.functions.NetworkHelper
import com.example.barcodeapp.repository.CodeRepository

class ViewModelFactory(
    private val repository: CodeRepository,
    private val networkHelper: NetworkHelper,
    private val context: Context,
    private val appDatabase: AppDatabase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoryViewModel::class.java)) {
            return CategoryViewModel(repository, networkHelper, appDatabase, context) as T
        } else if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
            return ProductViewModel(repository, networkHelper) as T
        }
        throw Exception("Error")
    }

}