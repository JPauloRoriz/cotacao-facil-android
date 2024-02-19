package com.example.cotacaofacil.presentation.viewmodel.provider.price.contract.pricesProviderContract

import com.example.cotacaofacil.domain.model.PriceModel

data class PricePartnerState (
    var messageError : String = "",
    var showProgressBar : Boolean = true,
    var pricesModel : MutableList<PriceModel> = mutableListOf(),
)