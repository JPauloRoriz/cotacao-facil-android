package com.example.cotacaofacil.domain.usecase.partner

import android.content.Context
import com.example.cotacaofacil.data.repository.bodyCompany.contract.BodyCompanyRepository
import com.example.cotacaofacil.data.repository.history.contract.HistoryRepository
import com.example.cotacaofacil.data.repository.partner.contract.PartnerRepository
import com.example.cotacaofacil.data.service.Date.contract.DateCurrent
import com.example.cotacaofacil.domain.Extensions.Companion.ifNotEmpty
import com.example.cotacaofacil.domain.Extensions.Companion.isNetworkConnected
import com.example.cotacaofacil.domain.exception.NoConnectionInternetException
import com.example.cotacaofacil.domain.model.HistoryModel
import com.example.cotacaofacil.domain.model.PartnerModel
import com.example.cotacaofacil.domain.model.TypeHistory
import com.example.cotacaofacil.domain.model.UserModel
import com.example.cotacaofacil.domain.usecase.partner.contract.AddRequestPartnerUseCase
import java.util.*

class AddRequestRequestPartnerUseCaseImpl(
    private val repositoryPartner: PartnerRepository,
    private val historyRepository: HistoryRepository,
    private val bodyCompanyRepository: BodyCompanyRepository,
    private val dateCurrent: DateCurrent
) : AddRequestPartnerUseCase {

    override suspend fun invoke(userModel: UserModel, partner: PartnerModel, context: Context): Result<Unit?> {
        return if (context.isNetworkConnected()) {
            bodyCompanyRepository.getBodyCompanyModel(userModel.cnpj).onSuccess { bodyCompanyModel ->
                var date = Date().time
                dateCurrent.invoke()
                    .onSuccess {
                        date = it
                    }
                    .onFailure {
                        Result.failure<java.lang.Exception>(NoConnectionInternetException())
                    }
                addHistoryModel(TypeHistory.SEND_REQUEST_PARTNER, date,  partner.nameFantasy.ifNotEmpty(), userModel.cnpj)
                addHistoryModel(TypeHistory.SEND_RECEIVE_PARTNER, date,  bodyCompanyModel.fantasia.ifNotEmpty(), partner.cnpjCorporation)
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