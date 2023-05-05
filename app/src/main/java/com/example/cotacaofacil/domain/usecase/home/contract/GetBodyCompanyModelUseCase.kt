package com.example.cotacaofacil.domain.usecase.home.contract

import com.example.cotacaofacil.domain.model.BodyCompanyModel

interface GetBodyCompanyModelUseCase {
   suspend fun invoke(cnpjUser : String) : Result<BodyCompanyModel>
}