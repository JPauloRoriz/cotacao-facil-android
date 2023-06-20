package com.example.cotacaofacil.presentation.viewmodel.provider.home.contract

data class HomeProviderState(
    val isLoading : Boolean = false,
    val fone : String = "",
    val email : String = "",
    val nameFantasy : String = "",
    val nameCorporation : String = "",
    val cnpj : String = "",
    val quantityProducts : String = "0",
    val quantityPrice : String = "0",
)
