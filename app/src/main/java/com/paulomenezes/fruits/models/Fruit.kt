package com.paulomenezes.fruits.models

import android.graphics.Bitmap
import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Fruit(var name: String, var benefits: String, var image: Bitmap?): Parcelable