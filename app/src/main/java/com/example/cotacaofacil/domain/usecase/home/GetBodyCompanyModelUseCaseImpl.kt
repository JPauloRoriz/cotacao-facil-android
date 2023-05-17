package com.example.cotacaofacil.domain.usecase.home

import com.example.cotacaofacil.data.repository.bodyCompany.contract.BodyCompanyRepository
import com.example.cotacaofacil.domain.exception.DefaultException
import com.example.cotacaofacil.domain.model.BodyCompanyModel
import com.example.cotacaofacil.domain.usecase.home.contract.GetBodyCompanyModelUseCase

class GetBodyCompanyModelUseCaseImpl(
    private val bodyCompanyRepository: BodyCompanyRepository
) : GetBodyCompanyModelUseCase {
    override suspend fun invoke(cnpjUser: String): Result<BodyCompanyModel> {
        if(cnpjUser.isNotEmpty()){
            return bodyCompanyRepository.getBodyCompanyModel(cnpjUser)
        } else {
            return Result.failure(DefaultException())
        }
    }
}