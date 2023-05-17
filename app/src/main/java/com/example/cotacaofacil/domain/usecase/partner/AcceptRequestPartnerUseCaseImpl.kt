package com.example.cotacaofacil.domain.usecase.partner

import com.example.cotacaofacil.data.repository.bodyCompany.contract.BodyCompanyRepository
import com.example.cotacaofacil.data.repository.history.contract.HistoryRepository
import com.example.cotacaofacil.data.repository.partner.contract.PartnerRepository
import com.example.cotacaofacil.data.service.Date.contract.DateCurrent
import com.example.cotacaofacil.domain.Extensions.Companion.ifNotEmpty
import com.example.cotacaofacil.domain.exception.NoConnectionInternetException
import com.example.cotacaofacil.domain.model.HistoryModel
import com.example.cotacaofacil.domain.model.PartnerModel
import com.example.cotacaofacil.domain.model.TypeHistory
import com.example.cotacaofacil.domain.usecase.partner.contract.AcceptRequestPartnerUseCase
import java.util.*

class AcceptRequestPartnerUseCaseImpl(
    val repository: PartnerRepository,
    private val historyRepository: HistoryRepository,
    private val bodyCompanyRepository: BodyCompanyRepository,
    private val dateCurrent : DateCurrent
) : AcceptRequestPartnerUseCase {
    override suspend fun invoke(cnpj: String, partner: PartnerModel): Result<Boolean> {
        var date = Date().time
        dateCurrent.invoke()
            .onSuccess {
                date = it
            }
            .onFailure {
                Result.failure<java.lang.Exception>(NoConnectionInternetException())
            }
        bodyCompanyRepository.getBodyCompanyModel(cnpj).onSuccess {bodyCompanyModel ->
            addHistoryModel(TypeHistory.NEW_PARTNER_ADD, date,  partner.nameFantasy.ifNotEmpty(), cnpj)
            addHistoryModel(TypeHistory.NEW_PARTNER_ADD, date,  bodyCompanyModel.fantasia .ifNotEmpty(), partner.cnpjCorporation)
        }
        return repository.acceptPartner(cnpj, partner)
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