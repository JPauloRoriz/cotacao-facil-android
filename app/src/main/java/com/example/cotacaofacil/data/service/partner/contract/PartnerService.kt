package com.example.cotacaofacil.data.service.partner.contract

import com.example.cotacaofacil.data.model.BodyCompanyResponse
import com.example.cotacaofacil.data.model.PartnerResponse
import com.example.cotacaofacil.domain.model.StatusIsMyPartner
import com.example.cotacaofacil.presentation.viewmodel.register.model.UserTypeSelected

interface PartnerService {
    suspend fun getBodyCompanyFirebaseByCnpj(
        userTypeSelected: UserTypeSelected,
        idUser: String,
        cnpj: String
    ): Result<BodyCompanyResponse?>

    suspend fun getPartnersByCnpj(cnpjUser: String): Result<MutableList<PartnerResponse>>
    suspend fun addRequestPartnerResponse(
        cnpjUser: String,
        partnerResponse: PartnerResponse
    ): Result<Unit?>

    suspend fun isMyPartner(cnpjUser: String, cnpjFind: String): StatusIsMyPartner
    suspend fun rejectPartner(cnpj: String, partnerResponse: PartnerResponse): Result<Boolean>
    suspend fun acceptPartner(cnpj: String, partnerResponse: PartnerResponse): Result<Boolean>
}