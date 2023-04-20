package com.example.cotacaofacil.data.repository.partner.contract

import com.example.cotacaofacil.domain.model.PartnerModel
import com.example.cotacaofacil.domain.model.UserModel
import com.example.cotacaofacil.presentation.viewmodel.register.model.UserTypeSelected

interface PartnerRepository {
    suspend fun getPartnerByCnpj(userTypeSelected: UserTypeSelected, user: UserModel, cnpj: String): Result<PartnerModel?>
    suspend fun getAllPartnerModel(
        userTypeSelected: UserTypeSelected,
        idUser: String,
        cnpjUser: String
    ): Result<MutableList<PartnerModel>>

    suspend fun addRequestPartner(cnpj: String, partner: PartnerModel): Result<Unit?>
    suspend fun rejectPartner(cnpj: String, partnerModel: PartnerModel): Result<Boolean>
    suspend fun acceptPartner(cnpj: String, partner: PartnerModel): Result<Boolean>
}