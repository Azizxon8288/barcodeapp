package com.example.barcodeapp.broadCast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class MyReceiver : BroadcastReceiver() {
    private val TAG = "MyReceiver"
    override fun onReceive(context: Context, intent: Intent) {
        val scanResult1 = intent.getStringExtra("SCAN_BARCODE1")
        val scanStatus = intent.getStringExtra("SCAN_STATE")
        if ("ok" == scanStatus) {
            Log.d(TAG, "onReceive: $scanResult1")
            Toast.makeText(context, scanResult1, Toast.LENGTH_SHORT).show()
        } else {
            Log.d(TAG, "onReceive: Uxshamadi")
            Toast.makeText(context, "uxshamadi", Toast.LENGTH_SHORT).show()
        }
    }
}