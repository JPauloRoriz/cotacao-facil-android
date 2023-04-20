package com.example.cotacaofacil.presentation.viewmodel.product.model


data class ProductAddState(
    val messageError: String = "",
    val isLoading: Boolean = false,
    val listSpinner: MutableList<String> = arrayListOf(),
)