package com.example.barcodeapp.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.barcodeapp.ProductDetailsFragment
import com.example.barcodeapp.data.room.AppDatabase
import com.example.barcodeapp.data.service.ApiClient
import com.example.barcodeapp.functions.NetworkHelper
import com.example.barcodeapp.repository.CodeRepository
import com.example.barcodeapp.viewmodels.CategoryViewModel
import com.example.barcodeapp.viewmodels.ViewModelFactory
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class BarcodeReceiver : BroadcastReceiver() {
    private val TAG = "MyReceiver"
    private lateinit var appDatabase: AppDatabase
//    private lateinit var networkHelper: NetworkHelper
//    private lateinit var categoryViewModel: CategoryViewModel
    override fun onReceive(context: Context, intent: Intent) {
        appDatabase = AppDatabase.getInstance(context)
//        networkHelper = NetworkHelper(context)


//        categoryViewModel = ViewModelProvider(context, ViewModelFactory(CodeRepository(appDatabase, ApiClient.webservice), networkHelper))[CategoryViewModel::class.java]

        val scannedBarcode = intent.getStringExtra("SCAN_BARCODE1")
        val scanStatus = intent.getStringExtra("SCAN_STATE")
        if ("ok" == scanStatus) {
            Log.d(TAG, "onReceive: $scannedBarcode")
            Toast.makeText(context, scannedBarcode, Toast.LENGTH_SHORT).show()
            val productEntity = appDatabase.productDao().searchByBarCode(scannedBarcode)
            val searchByBarCodeBoolean =
                appDatabase.productDao().searchByBarCodeBoolean(scannedBarcode)
            if (searchByBarCodeBoolean) {
                val action = Intent(context, ProductDetailsFragment::class.java)
                action.putExtra("search", productEntity)
                context.startActivity(action)
            } else {
                Toast.makeText(context, "Bunday malumot topilmadi", Toast.LENGTH_SHORT).show()
            }
//            GlobalScope.launch {
//                categoryViewModel.searchByBarCode(scannedBarcode).collect {
//
//                }



            // 1/17/2022 Shetta search(scannedBarcode); function chaqirishili kerak keyin
            // 1/17/2022 bu funksiya scannedBarcode ga teng bo'lga mahsulotlni databsedan izlashi va topilsa ProductDetails fragmentda topilgan mahsulot ma'lumotlarini chiqarishi kereak
            // 1/17/2022 aks holda bunday mahsulot topilmaganligini bildirish kerak
        } else {
            Log.d(TAG, "onReceive: Uxshamadi")
            Toast.makeText(context, "Scanner bulmadi", Toast.LENGTH_SHORT).show()
        }
    }
}