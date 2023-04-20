package com.example.cotacaofacil.domain.usecase.partner

import com.example.cotacaofacil.data.repository.partner.contract.PartnerRepository
import com.example.cotacaofacil.domain.exception.CnpjIncompleteException
import com.example.cotacaofacil.domain.exception.EmptyFildException
import com.example.cotacaofacil.domain.model.PartnerModel
import com.example.cotacaofacil.domain.usecase.partner.contract.GetAllPartnerModelUseCase
import com.example.cotacaofacil.presentation.viewmodel.register.model.UserTypeSelected

class GetAllPartnerModelUseCaseImpl(
    private val repositoryPartner: PartnerRepository
    ) : GetAllPartnerModelUseCase {
    override suspend fun invoke(userTypeSelected: UserTypeSelected, idUser: String, cnpj: String): Result<MutableList<PartnerModel>> {
        return if(cnpj.isEmpty()){
            Result.failure(EmptyFildException())
        } else if(cnpj.length < 14){
            Result.failure(CnpjIncompleteException())
        } else {
            repositoryPartner.getAllPartnerModel(userTypeSelected, idUser, cnpj)
        }
    }
}