package com.example.cotacaofacil.presentation.viewmodel.buyer.price.contractPrice

import com.example.cotacaofacil.domain.model.PriceModel

data class PriceState(
    var messageError : String = "",
    var showProgressBar : Boolean = true,
    var pricesModel : MutableList<PriceModel> = mutableListOf(),
)
