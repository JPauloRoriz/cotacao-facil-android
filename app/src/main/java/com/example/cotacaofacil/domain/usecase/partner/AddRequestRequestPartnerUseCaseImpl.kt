package com.example.cotacaofacil.domain.usecase.partner

import android.content.Context
import com.example.cotacaofacil.data.repository.bodyCompany.contract.BodyCompanyRepository
import com.example.cotacaofacil.data.repository.history.contract.HistoryRepository
import com.example.cotacaofacil.data.repository.partner.contract.PartnerRepository
import com.example.cotacaofacil.domain.Extensions.Companion.ifNotEmpty
import com.example.cotacaofacil.domain.Extensions.Companion.isNetworkConnected
import com.example.cotacaofacil.domain.exception.NoConnectionInternetException
import com.example.cotacaofacil.domain.model.HistoryModel
import com.example.cotacaofacil.domain.model.PartnerModel
import com.example.cotacaofacil.domain.model.TypeHistory
import com.example.cotacaofacil.domain.model.UserModel
import com.example.cotacaofacil.domain.usecase.partner.contract.AddRequestPartnerUseCase

class AddRequestRequestPartnerUseCaseImpl(
    private val repositoryPartner: PartnerRepository,
    private val historyRepository: HistoryRepository,
    private val bodyCompanyRepository: BodyCompanyRepository
) : AddRequestPartnerUseCase {

    override suspend fun invoke(userModel: UserModel, partner: PartnerModel, currentDate: Long,context: Context): Result<Unit?> {
        return if (context.isNetworkConnected()) {
            bodyCompanyRepository.getBodyCompanyModel(userModel.cnpj).onSuccess { bodyCompanyModel ->
                addHistoryModel(TypeHistory.SEND_REQUEST_PARTNER, currentDate,  partner.nameFantasy.ifNotEmpty(), userModel.cnpj)
                addHistoryModel(TypeHistory.SEND_RECEIVE_PARTNER, currentDate,  bodyCompanyModel.fantasia.ifNotEmpty(), partner.cnpjCorporation)
            }
            if (currentDate != 0L){
                partner.date = currentDate
            }
            repositoryPartner.addRequestPartner(userModel.cnpj, partner)
        } else {
            Result.failure(NoConnectionInternetException())
        }

    }
    private suspend fun addHistoryModel(typeHistory: TypeHistory, date: Long, namePartner: String, cnpj: String) {
        historyRepository.addHistory(
            HistoryModel(
                typeHistory = typeHistory,
                date = date,
                nameAssistant = namePartner
            ), cnpj = cnpj
        )
    }
}