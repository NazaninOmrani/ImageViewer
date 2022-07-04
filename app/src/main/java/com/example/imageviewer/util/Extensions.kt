package com.example.imageviewer.util

import android.content.res.Resources
import kotlin.math.ceil

fun Int.dp() : Int{
    return if (this == 0) {
        0
    } else ceil((Resources.getSystem().displayMetrics.density * this).toDouble()).toInt()
}