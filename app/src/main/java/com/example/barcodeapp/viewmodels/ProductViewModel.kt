package com.example.barcodeapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.barcodeapp.data.mapper.product.mapToProductList
import com.example.barcodeapp.functions.NetworkHelper
import com.example.barcodeapp.repository.CodeRepository
import com.example.barcodeapp.resource.ProductResource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ProductViewModel(
    private val repository: CodeRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {

    fun getProducts(): StateFlow<ProductResource> {
        val stateFlow = MutableStateFlow<ProductResource>(ProductResource.Loading)
        viewModelScope.launch {
            if (networkHelper.isNetworkConnected()) {
                repository.getAllProduct().catch {
                    stateFlow.value = ProductResource.Error(it.message)
                }.collect {
                    if (it.isSuccessful) {
                        val body = it.body()
//                        val list2 = ArrayList<ProductEntity>()

//                        body?.forEach { it1 ->
//                            list2.add(
//                                ProductEntity(
//                                    it1.id,
//                                    it1.barcodes ?: emptyList(),
//                                    it1.code ?: 0,
//                                    it1.measurement ?: "",
//                                    it1.salesPrice ?: 0.0,
//                                    it1.name ?: it1.id,
//                                    it1.description ?: "",
//                                    it1.imageUrl ?: "",
//                                    it1.category.id
//                                )
//                            )
//                        }
                        repository.addDbProducts(body?.mapToProductList()?:emptyList())
                        stateFlow.value = ProductResource.Success(body?.mapToProductList()?:emptyList())
                        stateFlow.emit(ProductResource.Success(body?.mapToProductList()?:emptyList()))
                    } else {
                        when {
                            it.code() in 400..499 -> {
                                stateFlow.value = ProductResource.Error("Client Error")
                            }
                            it.code() in 500..599 -> {
                                stateFlow.value = ProductResource.Error("Server Error")
                            }
                            else -> {
                                stateFlow.value = ProductResource.Error("Other Error")
                            }
                        }
                    }
                }
            } else {
                repository.getDBProducts().catch {
                    stateFlow.value = ProductResource.Error("Bazada malumot yuq")
                }.collect {
                    stateFlow.value = ProductResource.Success(it)
                }
            }

        }
        return stateFlow
    }

}