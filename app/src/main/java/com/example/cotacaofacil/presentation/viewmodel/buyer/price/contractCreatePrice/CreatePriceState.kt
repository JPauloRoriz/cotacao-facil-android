package com.example.cotacaofacil.presentation.viewmodel.buyer.price.contractCreatePrice

import com.example.cotacaofacil.domain.model.PartnerModel

data class CreatePriceState(
    var messageErrorPartners : String = "",
    var messageError : String = "",
    val listPartnersSelect : MutableList<PartnerModel> = mutableListOf(),
    val showProgressBar : Boolean = false,
    val dateFinishPrice : String = "",
    val dateDelivery : String = "",
)
