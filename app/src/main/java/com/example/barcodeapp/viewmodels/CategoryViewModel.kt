package com.example.barcodeapp.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.barcodeapp.data.mapper.category.mapToCategory
import com.example.barcodeapp.data.mapper.product.mapToProductList
import com.example.barcodeapp.data.mapper.product.maptoProductList
import com.example.barcodeapp.data.room.AppDatabase
import com.example.barcodeapp.data.room.entities.ProductEntity
import com.example.barcodeapp.functions.NetworkHelper
import com.example.barcodeapp.repository.CodeRepository
import com.example.barcodeapp.resource.CategoryResource
import com.example.barcodeapp.resource.ProductResource
import com.example.barcodeapp.resource.SearchResource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class CategoryViewModel(
    private val repository: CodeRepository,
    private val networkHelper: NetworkHelper,
    appDatabase: AppDatabase,
    context: Context
) : ViewModel() {
    private val state = MutableStateFlow<ProductState>(ProductState.Init)
    val mState: StateFlow<ProductState> get() = state

    private val _state = MutableStateFlow<ProductState>(ProductState.Init)
    val _mState: StateFlow<ProductState> get() = _state


    init {
//        getAllCategories()
//        getProducts()
//        getProduct()
    }


    fun getProductsChanges(time: Int) {
        viewModelScope.launch {
            repository.getProductsChanges(time)
                .onStart {

                }
                .catch {
                    Log.e("changeError", it.message.toString())
                }
                .collect {
                    if (it.isSuccessful) {
                        val body = it.body()
                        if (body != null) {
                            if (body.isNotEmpty()) {
                                repository.addDbProducts(
                                    it.body()?.maptoProductList() ?: emptyList()
                                )
                                getProductsChanges(1)

                            } else {
                                getProductsChanges(1)
                            }
                        }
                    }


                }
        }
    }


//    val flow = Pager(
//        PagingConfig(
//            pageSize = 10,
//            initialLoadSize = 10,
//            enablePlaceholders = false
//        )
//    ) {
//        ProductPagingSource(appDatabase, context)
//    }.flow.cachedIn(viewModelScope)


    fun searchByBarCode(barcode: String?): StateFlow<SearchResource> {
        val stateFlow = MutableStateFlow<SearchResource>(SearchResource.Loading)

        viewModelScope.launch {
            repository.searchByBarCode(barcode).catch {
                stateFlow.value = SearchResource.Error(it.message.toString())
            }.collect {
                Log.d("TAG", "searchByBarCode: ${it?.barcodes}")
                stateFlow.value = SearchResource.Success(it!!)
            }
        }
        return stateFlow
    }

    fun getOneProduct(barcode: String): StateFlow<ProductResource> {
        val stateFlow = MutableStateFlow<ProductResource>(ProductResource.Loading)
        viewModelScope.launch {
            if (networkHelper.isNetworkConnected()) {
                repository.getOneProduct(barcode).catch {

                }
                    .collect {
                        if (it.isSuccessful) {
                            val body = it.body()
                            stateFlow.value =
                                ProductResource.Success(body?.mapToProductList() ?: emptyList())
                        } else {
                            stateFlow.value = ProductResource.Error("Maxsulot topilmadi")
                        }

                    }
            } else {
                stateFlow.value = ProductResource.Error("Internet mavjud emas")
            }
        }
        return stateFlow
    }


    fun getProducts(): StateFlow<ProductResource> {
        val stateFlow = MutableStateFlow<ProductResource>(ProductResource.Loading)
        viewModelScope.launch {
            if (networkHelper.isNetworkConnected()) {
                repository.getAllProduct()
                    .onStart {
                        state.emit(ProductState.IsLoading(true))
                    }
                    .catch {
                        state.emit(ProductState.IsLoading(false))
                        state.emit(ProductState.ShowToast(it.message.toString()))

                        stateFlow.value = ProductResource.Error(it.message)
                    }.collect {
                        state.emit(ProductState.IsLoading(false))
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
                            repository.addDbProducts(body?.mapToProductList() ?: emptyList())
//                        repository.addDbProducts(list2)
//                        stateFlow.value = ProductResource.Success(list2)
//                        stateFlow.emit(ProductResource.Success(list2))
                            stateFlow.value =
                                ProductResource.Success(body?.mapToProductList() ?: emptyList())

                            state.emit(ProductState.SuccessProduct(body?.mapToProductList()))
//                            state.emit(ProductState.ShowToast("Mufaqqiyatli yuklandi!!!"))
//                        stateFlow.emit(ProductResource.Success(body?.mapToProductList()?:emptyList()))
                        } else {
                            when {
                                it.code() in 400..499 -> {
                                    stateFlow.value = ProductResource.Error("Client Error")
                                    state.emit(ProductState.ShowToast("Client error code 400..499"))
                                }
                                it.code() in 500..599 -> {
                                    stateFlow.value = ProductResource.Error("Server Error")
                                    state.emit(ProductState.ShowToast("Server error"))
                                }
                                else -> {
                                    state.emit(ProductState.ShowToast("Other error"))
                                    stateFlow.value = ProductResource.Error("Other Error")
                                }
                            }
                            state.emit(ProductState.IsLoading(false))
                        }
                    }
            } else {
                state.emit(ProductState.IsLoading(false))
                repository.getDBProducts().catch {
                    stateFlow.value = ProductResource.Error("Bazada malumot yuq")
                }.collect {
                    stateFlow.value = ProductResource.Success(it)
                }
            }

        }
        return stateFlow
    }

    var page = 1
    fun getProduct(): StateFlow<ProductState> {
        val stateFlow = MutableStateFlow<ProductState>(ProductState.Init)
//        var page = 1
        viewModelScope.launch {
            if (networkHelper.isNetworkConnected()) {

                repository.getProducts(page)
                    .onStart {
                        stateFlow.emit(ProductState.IsLoading(true))
                    }
                    .catch {
                        _state.emit(ProductState.IsLoading(false))
                        stateFlow.emit(ProductState.IsLoading(false))
                        stateFlow.emit(ProductState.ErrorProduct(it.message.toString()))
                    }
                    .collect {
                        if (it.isSuccessful) {
                            val body = it.body()
                            if (body?.data?.isNotEmpty() == true) {
                                repository.addDbProducts(body.data.maptoProductList())
                                page += 1
                                getProduct()
                            } else {
                                _state.emit(ProductState.IsLoading(false))

//                                stateFlow.emit(ProductState.IsLoading(false))
//                                page = 1
                                stateFlow.emit(
                                    ProductState.SuccessProduct(
                                        body?.data?.maptoProductList() ?: emptyList()
                                    )
                                )
                                //log.e error
                            }
                        }
                    }
            } else {
                stateFlow.emit(ProductState.IsLoading(false))
                // network error
            }
        }
        return stateFlow
    }

    fun getAllCategories(): StateFlow<CategoryResource> {
        val stateFlow = MutableStateFlow<CategoryResource>(CategoryResource.Loading)

        viewModelScope.launch {
            if (networkHelper.isNetworkConnected()) {
                repository.getAllCategory().catch {
                    repository.getDBCategories().catch {
                        stateFlow.value = CategoryResource.Error("Bazada malumot yuq")
                    }.collect {
                        stateFlow.value = CategoryResource.Success(it)
                    }
//                    stateFlow.value = CategoryResource.Error(it.message ?: "")
                }.collect { response ->
                    if (response.isSuccessful) {
                        val list = response.body()
//                        val list1 = ArrayList<CategoryEntity>()
//                        list?.forEach {
//                            list1.add(CategoryEntity(it.id, it.productCount, it.name, it.imageUrl))
//                        }

//                        list?.mapToCategory()
                        repository.addDbCategories(list?.mapToCategory() ?: emptyList())
//                        stateFlow.value = CategoryResource.Success(repository.getDBCategories())

                        stateFlow.value =
                            CategoryResource.Success(list?.mapToCategory() ?: emptyList())

//                        stateFlow.emit(CategoryResource.Success(list?.mapToCategory()?: emptyList()))
                    } else {
//                        when {
//                            response.code() in 400..499 -> {
//                                stateFlow.value = CategoryResource.Error("Client Error")
//                            }
//                            response.code() in 500..599 -> {
//                                stateFlow.value = CategoryResource.Error("Server Error")
//                            }
//                            else -> {
//                                stateFlow.value = CategoryResource.Error("Other Error")
//                            }
//                        }
                        repository.getDBCategories().catch {
                            stateFlow.value = CategoryResource.Error("Bazada malumot yuq")
                        }.collect {
                            stateFlow.value = CategoryResource.Success(it)
                        }
                    }
                }
            } else {
                repository.getDBCategories().catch {
                    stateFlow.value = CategoryResource.Error("Bazada malumot yuq")
                }.collect {
                    stateFlow.value = CategoryResource.Success(it)
                }
            }
        }
        return stateFlow
    }
}

sealed class ProductState {
    object Init : ProductState()
    data class IsLoading(val isLoading: Boolean) : ProductState()
    data class ShowToast(val message: String) : ProductState()
    data class SuccessProduct(val status: List<ProductEntity>?) : ProductState()
    data class ErrorProduct(val rawResponse: String) : ProductState()
}