package com.example.cotacaofacil.domain.usecase.date.contract

import com.example.cotacaofacil.domain.model.PartnerModel
import com.example.cotacaofacil.domain.model.PriceModel

interface ValidationNextCreatePriceUseCase {
    fun invoke(autoClose: Boolean, allowAllPartners: Boolean, date: Long?, dateDelivery: Long, partners: MutableList<PartnerModel>, priority: Int, currentDate: Long) : Result<PriceModel>
}