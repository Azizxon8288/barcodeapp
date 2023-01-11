package com.example.barcodeapp.data.pager

import android.content.Context
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.barcodeapp.data.mapper.product.maptoProductList
import com.example.barcodeapp.data.model.product.Data
import com.example.barcodeapp.data.room.AppDatabase
import com.example.barcodeapp.repository.PagerRepository
import kotlinx.coroutines.flow.catch

class ProductPagingSource(
    appDatabase: AppDatabase,
    context: Context
) : PagingSource<Int, Data>() {
    private val productRepository = PagerRepository(context, appDatabase)

    override fun getRefreshKey(state: PagingState<Int, Data>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Data> {
        try {
//            val pageSize = params.loadSize
//            val nextPage = params.key ?: FIRST_PAGE_INDEX
//             val response = webservice.getProduct(page = nextPage)
            val currentPage = params.key ?: 1

            var loadResult: LoadResult.Page<Int, Data>? = null
            if (params.key ?: 1 >= 1) {
                productRepository.getProducts(currentPage)
                    .catch {
                        loadResult = LoadResult.Page(
                            emptyList(),
                            currentPage - 1, currentPage + 1
                        )
                    }
                    .collect {
                        loadResult = LoadResult.Page(
                            it.body()?.data ?: emptyList(),
                            currentPage - 1, currentPage + 1
                        )
                        productRepository.addDbProducts(it.body()?.data?.maptoProductList()?: emptyList())
                        Log.e("sucess", it.body()?.data.toString())
                    }
            } else {
                loadResult = LoadResult.Page(
                    emptyList(),
                    null,
                    currentPage + 1
                )
            }

            return loadResult!!

//            if (response.isSuccessful) {
//                val responseBody = response.body()
//                responseBody?.data?.size
//            }
//            LoadResult.Page(
//                data = response.body()?.data ?: emptyList(),
//                prevKey = null,
//                nextKey = if (response.body()?.data?.size ?: 1 < pageSize) null else nextPage + 1
//            )

        } catch (e: Exception) {
            return LoadResult.Error(e.fillInStackTrace())
        }
    }

    companion object {
        private const val FIRST_PAGE_INDEX = 1
    }
}