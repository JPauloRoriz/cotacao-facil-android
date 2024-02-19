package com.example.cotacaofacil.presentation.viewmodel.buyer.price.contractPrice

import com.example.cotacaofacil.domain.model.PriceModel

sealed class PriceEvent {
    object CreatePrice : PriceEvent()
    data class ShowDialogSuccess(val code : String) : PriceEvent()
    data class TapOnPriceOpen(val priceModel: PriceModel) : PriceEvent()
    data class TapOnPriceFinishedOrCanceled(val priceModel: PriceModel) : PriceEvent()
}