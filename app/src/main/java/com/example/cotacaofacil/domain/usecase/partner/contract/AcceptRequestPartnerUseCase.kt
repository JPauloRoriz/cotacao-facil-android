package com.example.cotacaofacil.domain.usecase.partner.contract

import com.example.cotacaofacil.domain.model.PartnerModel

interface AcceptRequestPartnerUseCase {
    suspend fun invoke(cnpj: String, partner: PartnerModel, currentDate : Long) : Result<Boolean>
}