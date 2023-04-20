package com.example.cotacaofacil.domain.usecase.partner

import android.content.Context
import com.example.cotacaofacil.data.repository.partner.contract.PartnerRepository
import com.example.cotacaofacil.domain.Extensions.Companion.convertCnpj
import com.example.cotacaofacil.domain.Extensions.Companion.isNetworkConnected
import com.example.cotacaofacil.domain.exception.CnpjIncompleteException
import com.example.cotacaofacil.domain.exception.CnpjOwnException
import com.example.cotacaofacil.domain.exception.EmptyFildException
import com.example.cotacaofacil.domain.exception.NoConnectionInternetException
import com.example.cotacaofacil.domain.model.PartnerModel
import com.example.cotacaofacil.domain.model.UserModel
import com.example.cotacaofacil.domain.usecase.partner.contract.ValidationCnpjUseCase
import com.example.cotacaofacil.presentation.viewmodel.register.model.UserTypeSelected

class ValidationCnpjUseCaseImpl(
    private val repositoryPartner: PartnerRepository
    ) : ValidationCnpjUseCase {
    override suspend fun invoke(userTypeSelected: UserTypeSelected, user: UserModel, cnpj: String, context: Context): Result<PartnerModel?> {
        val cnpjConvert = cnpj.convertCnpj()
        return if(cnpjConvert.isEmpty()){
            Result.failure(EmptyFildException())
        } else if(cnpjConvert.length < 14){
            Result.failure(CnpjIncompleteException())
        } else if(user.cnpj == cnpjConvert){
            Result.failure(CnpjOwnException())
        } else if (!context.isNetworkConnected()){
            Result.failure(NoConnectionInternetException())
        }
        else {
            repositoryPartner.getPartnerByCnpj(userTypeSelected, user, cnpjConvert)
        }
    }
}