package com.example.cotacaofacil.presentation.viewmodel.buyer.price.contractPrice

sealed class PriceEvent {
    object CreatePrice : PriceEvent()
    data class ShowDialogSuccess(val code : String) : PriceEvent()
}