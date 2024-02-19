package com.example.cotacaofacil.domain.usecase.date.contract

import com.example.cotacaofacil.domain.model.PartnerModel
import com.example.cotacaofacil.domain.model.PriceModel
import com.example.cotacaofacil.domain.model.PriorityPrice

interface ValidationNextCreatePriceUseCase {
    fun invoke(
        autoClose: Boolean,
        allowAllPartners: Boolean,
        date: Long,
        dateDelivery: Long,
        partners: MutableList<PartnerModel>,
        description: String,
        priority: PriorityPrice?,
        currentDate: Long,
        nameCompanyCreator : String
    ): Result<PriceModel>
}