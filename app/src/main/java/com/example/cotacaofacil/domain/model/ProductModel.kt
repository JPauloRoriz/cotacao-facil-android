package com.example.cotacaofacil.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductModel(
val name: String = "",
val description: String = "",
val brand: String = "",
val typeMeasurement: String = "",
val cnpjBuyer: String = "",
var code: String = "",
val quantity: String = "",
var isFavorite: Boolean = false,
var date: Long = 0
) : Parcelable
