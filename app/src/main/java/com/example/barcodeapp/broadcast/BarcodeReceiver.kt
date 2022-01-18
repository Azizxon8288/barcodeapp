package com.example.barcodeapp.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.barcodeapp.ProductDetailsFragment

class BarcodeReceiver : BroadcastReceiver() {
    private val TAG = "MyReceiver"
    override fun onReceive(context: Context, intent: Intent) {
        val scannedBarcode = intent.getStringExtra("SCAN_BARCODE1")
        val scanStatus = intent.getStringExtra("SCAN_STATE")
        if ("ok" == scanStatus) {
            Log.d(TAG, "onReceive: $scannedBarcode")
            Toast.makeText(context, scannedBarcode, Toast.LENGTH_SHORT).show()
//            val action = Intent(context, ProductDetailsFragment::class.java)

            // TODO: 1/17/2022 Shetta search(scannedBarcode); function chaqirishili kerak keyin
            // TODO: 1/17/2022 bu funksiya scannedBarcode ga teng bo'lga mahsulotlni databsedan izlashi va topilsa ProductDetails fragmentda topilgan mahsulot ma'lumotlarini chiqarishi kereak
            // TODO: 1/17/2022 aks holda bunday mahsulot topilmaganligini bildirish kerak
        } else {
            Log.d(TAG, "onReceive: Uxshamadi")
            Toast.makeText(context, "uxshamadi", Toast.LENGTH_SHORT).show()
        }
    }
}