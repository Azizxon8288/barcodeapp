package com.example.barcodeapp.functions

import androidx.navigation.NavOptions
import com.example.barcodeapp.R

fun navOptions(): NavOptions {
    return NavOptions.Builder()
        .setEnterAnim(R.anim.enter)
        .setExitAnim(R.anim.exit)
        .setPopEnterAnim(R.anim.pop_enter)
        .setPopExitAnim(R.anim.pop_exit).build()
}