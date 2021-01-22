package com.paulomenezes.fruits.utils

import android.content.Context
import android.util.TypedValue

fun Int.toDP(context: Context): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics).toInt()
}