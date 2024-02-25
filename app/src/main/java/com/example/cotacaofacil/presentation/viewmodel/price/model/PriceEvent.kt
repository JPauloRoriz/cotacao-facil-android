package com.example.cotacaofacil.presentation.viewmodel.price.model

import com.example.cotacaofacil.presentation.viewmodel.partner.model.PartnerEvent

sealed class PriceEvent {
   data class FinishActivity(val message : String) : PriceEvent()
}