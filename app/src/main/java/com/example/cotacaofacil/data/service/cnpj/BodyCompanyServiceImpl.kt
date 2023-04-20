package com.example.cotacaofacil.data.service.cnpj

import com.example.cotacaofacil.data.model.BodyCompanyResponse
import com.example.cotacaofacil.data.service.cnpj.contract.BodyCompanyService
import com.example.cotacaofacil.domain.exception.ErrorSaveBodyCompany
import com.example.cotacaofacil.presentation.viewmodel.register.model.UserTypeSelected
import com.google.firebase.firestore.FirebaseFirestore

class BodyCompanyServiceImpl(
    private val firestore: FirebaseFirestore
) : BodyCompanyService {
    override suspend fun addBodyCompanyResponse(
        userTypeSelected: UserTypeSelected,
        cnpj: String,
        bodyCompanyResponse: BodyCompanyResponse?
    ): Result<Any?> {
        return if (bodyCompanyResponse != null) {
            bodyCompanyResponse.typeUser = userTypeSelected
            bodyCompanyResponse.cnpj = cnpj
            val result = firestore.collection(BODY_COMPANY).document(cnpj).set(bodyCompanyResponse)
            if (result.isSuccessful) {
                Result.success(null)
            } else {
                Result.failure(ErrorSaveBodyCompany())
            }
        } else {
            return Result.failure(ErrorSaveBodyCompany())
        }
    }

    companion object {
        const val BODY_COMPANY = "body_company"
    }
}