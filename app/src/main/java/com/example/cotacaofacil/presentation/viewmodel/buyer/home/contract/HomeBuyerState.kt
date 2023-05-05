package com.example.cotacaofacil.presentation.viewmodel.buyer.home.contract

data class HomeBuyerState(
     val isLoading : Boolean,
     val fone : String = "",
     val email : String,
     val nameFantasy : String,
     val nameCorporation : String,
)
