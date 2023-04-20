package com.example.cotacaofacil.data.service.cnpj.contract

import com.example.cotacaofacil.data.model.BodyCompanyResponse
import com.example.cotacaofacil.presentation.viewmodel.register.model.UserTypeSelected

interface BodyCompanyService {
   suspend fun addBodyCompanyResponse(userTypeSelected: UserTypeSelected, cnpj: String, bodyCompanyResponse: BodyCompanyResponse?): Result<Any?>
}