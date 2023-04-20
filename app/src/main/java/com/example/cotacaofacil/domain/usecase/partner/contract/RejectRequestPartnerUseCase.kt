package com.example.cotacaofacil.domain.usecase.partner.contract

import android.content.Context
import com.example.cotacaofacil.domain.model.PartnerModel
import com.example.cotacaofacil.domain.usecase.partner.util.TypeDeletePartner

interface RejectRequestPartnerUseCase {
    suspend fun invoke(cnpj: String, partnerModel: PartnerModel, context: Context, typeDelete: TypeDeletePartner) : Result<Boolean>
}