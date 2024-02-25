package com.example.cotacaofacil.presentation.viewmodel.buyer.home.contract

data class HomeBuyerState(
     val isLoading : Boolean = false,
     val fone : String = "",
     val email : String = "",
     val nameFantasy : String = "",
     val nameCorporation : String = "",
     val cnpj : String = "",
     val quantityProducts : String = "0",
     val quantityPrice : String = "0",
     val loadingImageProfile : Boolean = false,
     val imageProfile : String? = null,
)
