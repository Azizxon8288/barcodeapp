package com.example.barcodeapp.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.example.barcodeapp.ProductDetailsFragment
import com.example.barcodeapp.data.room.AppDatabase
import com.example.barcodeapp.data.service.ApiClient
import com.example.barcodeapp.functions.NetworkHelper
import com.example.barcodeapp.repository.CodeRepository
import com.example.barcodeapp.viewmodels.CategoryViewModel
import com.example.barcodeapp.viewmodels.ViewModelFactory
import kotlinx.coroutines.*


class BarcodeReceiver : BroadcastReceiver() {
    private val TAG = "MyReceiver"
    private lateinit var appDatabase: AppDatabase
    private lateinit var networkHelper: NetworkHelper
    private lateinit var categoryViewModel: CategoryViewModel
    override fun onReceive(context: Context, intent: Intent) {
        appDatabase = AppDatabase.getInstance(context)
//        networkHelper = NetworkHelper(context)


//        categoryViewModel = ViewModelProvider(, ViewModelFactory(CodeRepository(appDatabase, ApiClient.webservice), networkHelper))[CategoryViewModel::class.java]

        val scannedBarcode = intent.getStringExtra("SCAN_BARCODE1") ?: ""
        val scanStatus = intent.getStringExtra("SCAN_STATE")
        if ("ok" == scanStatus) {
            Log.d(TAG, "onReceive: $scannedBarcode")

            Toast.makeText(context, scannedBarcode, Toast.LENGTH_SHORT).show()

            GlobalScope.launch(Dispatchers.IO) {
                val productEntity = appDatabase.productDao().searchByBarCode(scannedBarcode)

                if (productEntity != null) {
                    val action = Intent(context, ProductDetailsFragment::class.java)
                    action.putExtra("search", productEntity)
                    context.startActivity(action)
                } else {
                    // TODO: 1/26/2022 Betda alohida not found 404 fragment ochilishi kerak
                }
            }



//            GlobalScope.launch {
//                categoryViewModel.searchByBarCode(scannedBarcode).collect {
//
//                }

        } else {
            Log.d(TAG, "onReceive: Uxshamadi")
            Toast.makeText(context, "Scanner bulmadi", Toast.LENGTH_SHORT).show()
        }
    }


}