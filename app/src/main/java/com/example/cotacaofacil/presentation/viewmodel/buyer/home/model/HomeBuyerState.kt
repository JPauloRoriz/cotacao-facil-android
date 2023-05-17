package com.example.cotacaofacil.presentation.viewmodel.buyer.home.model

data class HomeBuyerState(
     val isLoading : Boolean,
     val fone : String = "",
     val email : String = "",
     val nameFantasy : String = "",
     val nameCorporation : String = "",
     val cnpj : String = "",
     val quantityProducts : String = "0",
     val quantityPrice : String = "0",
)
