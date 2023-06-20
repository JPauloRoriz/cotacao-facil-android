package com.example.cotacaofacil.presentation.viewmodel.buyer.price.contractCreatePrice

import com.example.cotacaofacil.domain.model.PartnerModel
import com.example.cotacaofacil.domain.model.PriceModel

sealed class CreatePriceEvent {
    object CreatePrice : CreatePriceEvent()
    object AutoChecked : CreatePriceEvent()
    object NotAutoChecked : CreatePriceEvent()
    object AllPartners : CreatePriceEvent()
    object AllPartnersWithEffect : CreatePriceEvent()
    object NotAllPartnersWithEffect : CreatePriceEvent()
    object NotAllPartners : CreatePriceEvent()
    data class SuccessNext(val priceModel : PriceModel = PriceModel()) : CreatePriceEvent()
    data class UpdateListEvent(val partnerList : MutableList<PartnerModel> = arrayListOf()) : CreatePriceEvent()
    data class ChangeSwitch(val isChecked : Boolean = false) : CreatePriceEvent()
}