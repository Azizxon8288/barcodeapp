package com.example.barcodeapp.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.barcodeapp.data.room.AppDatabase
import com.example.barcodeapp.data.room.entities.CategoryEntity
import com.example.barcodeapp.data.room.entities.ProductEntity
import com.example.barcodeapp.data.service.ApiClient
import com.example.barcodeapp.functions.NetworkHelper
import com.example.barcodeapp.repository.CodeRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class UploadWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {
    private lateinit var repository: CodeRepository
    private lateinit var networkHelper: NetworkHelper
    override fun doWork(): Result {

        networkHelper = NetworkHelper(applicationContext)

        repository =
            CodeRepository(AppDatabase.getInstance(applicationContext), ApiClient.webservice)
        GlobalScope.launch {
            if (networkHelper.isNetworkConnected()) {

                repository.getAllCategory().catch {

                }.collect {
                    if (it.isSuccessful) {
                        val body = it.body()
                        val list = ArrayList<CategoryEntity>()

                        body?.forEach { it1 ->
                            list.add(
                                CategoryEntity(
                                    it1.id,
                                    it1.productCount,
                                    it1.name,
                                    it1.imageUrl
                                )
                            )
                        }
                        repository.addDbCategories(list)
                    }
                }


                repository.getAllProduct().catch {

                }.collect {
                    if (it.isSuccessful) {
                        val list = ArrayList<ProductEntity>()
                        it.body()?.forEach {
                            list.add(
                                ProductEntity(
                                    it.id,
                                    it.barcode ?: "",
                                    it.code ?: 0,
                                    it.measurement ?: "",
                                    it.salesPrice ?: 0.0,
                                    it.name ?: "",
                                    it.description ?: "",
                                    it.category.id
                                )
                            )
                        }
                        repository.addDbProducts(list)
                    }
                }

            }


        }
        // Do the work here--in this case, upload the images.

        // Indicate whether the work finished successfully with the Result
        return Result.success()
    }

}