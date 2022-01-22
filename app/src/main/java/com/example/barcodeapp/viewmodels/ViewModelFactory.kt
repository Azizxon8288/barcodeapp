package com.example.barcodeapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.barcodeapp.functions.NetworkHelper
import com.example.barcodeapp.repository.CodeRepository
import java.lang.Exception

class ViewModelFactory(
    private val repository: CodeRepository,
    private val networkHelper: NetworkHelper
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoryViewModel::class.java)) {
            return CategoryViewModel(repository, networkHelper) as T
        } else if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
            return ProductViewModel(repository, networkHelper) as T
        }
        throw Exception("Error")
    }

}