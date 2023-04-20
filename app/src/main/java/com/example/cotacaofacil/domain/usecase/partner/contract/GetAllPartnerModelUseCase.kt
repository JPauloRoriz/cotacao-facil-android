package com.example.cotacaofacil.domain.usecase.partner.contract

import com.example.cotacaofacil.domain.model.PartnerModel
import com.example.cotacaofacil.presentation.viewmodel.register.model.UserTypeSelected

interface GetAllPartnerModelUseCase {
    suspend fun invoke(userTypeSelected: UserTypeSelected, idUser : String, cnpj: String): Result<MutableList<PartnerModel>>
}