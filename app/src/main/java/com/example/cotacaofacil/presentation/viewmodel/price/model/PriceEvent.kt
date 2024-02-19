package com.example.cotacaofacil.presentation.viewmodel.price.model

import com.example.cotacaofacil.presentation.viewmodel.partner.model.PartnerEvent

sealed class PriceEvent {
    object TapOnFinishPrice : PriceEvent()
    object TapOnCancelPrice : PriceEvent()
}