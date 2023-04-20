package com.example.cotacaofacil.data.repository.bodyCompany.contract

import com.example.cotacaofacil.domain.model.BodyCompanyModel
import com.example.cotacaofacil.presentation.viewmodel.register.model.UserTypeSelected

interface BodyCompanyRepository {
    suspend fun getBodyCompanyModel(cnpj: String) : Result<BodyCompanyModel>
    suspend fun addBodyCompanyModel(userTypeSelected: UserTypeSelected, cnpj: String, bodyCompanyModel: BodyCompanyModel?) : Result<Any?>
}