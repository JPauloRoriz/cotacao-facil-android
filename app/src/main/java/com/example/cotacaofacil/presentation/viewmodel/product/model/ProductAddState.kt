package com.example.cotacaofacil.presentation.viewmodel.product.model


data class ProductAddState(
    val messageError: String = "",
    val nameText: String = "",
    val descriptionText: String = "",
    val brandText: String = "",
    val typeFilter: String = "" ,
    val quantityText: String = "1",
    val titleBottomNavigation: String = "",
    val textButton: String = "",
    val isFavorite: Boolean = false,
    val trashIsGone: Boolean = false,
    val listSpinner: MutableList<String> = arrayListOf(),

)