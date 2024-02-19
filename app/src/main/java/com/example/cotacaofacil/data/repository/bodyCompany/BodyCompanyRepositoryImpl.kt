package com.example.cotacaofacil.data.repository.bodyCompany

import com.example.cotacaofacil.data.model.util.toBodyCompanyModel
import com.example.cotacaofacil.data.repository.bodyCompany.contract.BodyCompanyRepository
import com.example.cotacaofacil.data.service.cnpj.CnpjServiceImpl
import com.example.cotacaofacil.data.service.cnpj.contract.BodyCompanyService
import com.example.cotacaofacil.domain.exception.DefaultException
import com.example.cotacaofacil.domain.mapper.mapper
import com.example.cotacaofacil.domain.model.BodyCompanyModel
import com.example.cotacaofacil.presentation.viewmodel.register.model.UserTypeSelected

class BodyCompanyRepositoryImpl(
    private val cnpjService: CnpjServiceImpl,
    private val bodyCompanyService: BodyCompanyService
) : BodyCompanyRepository {
    override suspend fun getBodyCompanyModel(
        cnpj: String
    ): Result<BodyCompanyModel> {
        return try {
            Result.success(cnpjService.getCompanyByCnpj(cnpj).toBodyCompanyModel())

        } catch (e: Exception) {
            Result.failure(DefaultException())
        }

    }

    override suspend fun addBodyCompanyModel(
        userTypeSelected: UserTypeSelected,
        cnpj: String,
        bodyCompanyModel: BodyCompanyModel?
    ): Result<Any?> {
       val bodyCompanyResponse = bodyCompanyModel?.mapper()
        return bodyCompanyService.addBodyCompanyResponse(userTypeSelected,cnpj, bodyCompanyResponse)
    }
}