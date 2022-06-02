package com.example.barcodeapp.functions

import android.app.Activity
import android.content.Context.MODE_PRIVATE
import androidx.navigation.NavOptions
import com.example.barcodeapp.R

fun navOptions(): NavOptions {
    return NavOptions.Builder()
        .setEnterAnim(R.anim.enter)
        .setExitAnim(R.anim.exit)
        .setPopEnterAnim(R.anim.pop_enter)
        .setPopExitAnim(R.anim.pop_exit).build()
}

fun setIpAddress(ipAddress: String, activity: Activity) {
    val sharedPreferences = activity.getSharedPreferences("IP_ADDRESS", MODE_PRIVATE)
    val edit = sharedPreferences.edit()
    edit.putString("IP_ADDRESS", ipAddress)
    edit.apply()
}

fun getIpAddress(activity:Activity): String? {
    val sharedPreferences = activity.getSharedPreferences("IP_ADDRESS", MODE_PRIVATE)
    return sharedPreferences.getString("IP_ADDRESS", Constants.ip)

}

fun setFirst(screen: String, activity: Activity) {
    val sharedPreferences = activity.getSharedPreferences("SCREEN", MODE_PRIVATE)
    val edit = sharedPreferences.edit()
    edit.putString("SCREEN", screen)
    edit.apply()
}

fun getFirst(activity: Activity): String? {
    val sharedPreferences = activity.getSharedPreferences("SCREEN", MODE_PRIVATE)
    return sharedPreferences.getString("SCREEN", Constants.screen)

}