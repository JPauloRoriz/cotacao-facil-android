package com.example.cotacaofacil.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductResponse(
    val name: String = "",
    val description: String = "",
    val brand: String = "",
    val typeMeasurement: String = "",
    val cnpjBuyer: String = "",
    var code: String = "",
    val quantity: String = "",
    val date : Long = 0

) : Parcelable
