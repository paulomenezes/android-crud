package com.paulomenezes.fruits.models

import android.graphics.Bitmap
import android.net.Uri
import android.os.Parcelable
import android.util.Log
import kotlinx.parcelize.Parcelize

@Parcelize
data class Fruit(var id: Int, var name: String, var benefits: String, var image: Bitmap?): Parcelable {
    companion object {
        var idSequence = 0
    }
}