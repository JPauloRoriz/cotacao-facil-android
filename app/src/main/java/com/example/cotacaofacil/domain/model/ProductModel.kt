package com.example.cotacaofacil.domain.model

data class ProductModel(
    val name: String = "",
    val description: String = "",
    val brand: String = "",
    val typeMeasurement: String,
    val cnpjBuyer: String,
    var code: String = "",
    val quantity: String = "",
    var isFavorite: Boolean,
    var date: Long = 0
)
