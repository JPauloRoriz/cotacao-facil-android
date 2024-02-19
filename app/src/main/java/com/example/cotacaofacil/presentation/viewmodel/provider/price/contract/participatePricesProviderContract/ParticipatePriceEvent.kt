package com.example.cotacaofacil.presentation.viewmodel.provider.price.contract.participatePricesProviderContract

sealed class ParticipatePriceEvent {
data class EditPriceSuccess(val codePrice: String) : ParticipatePriceEvent()
data class ErrorSetPrice(val message: String) : ParticipatePriceEvent()
}