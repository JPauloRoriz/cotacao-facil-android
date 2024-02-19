package com.example.cotacaofacil.domain.usecase.partner

import com.example.cotacaofacil.data.repository.bodyCompany.contract.BodyCompanyRepository
import com.example.cotacaofacil.data.repository.history.contract.HistoryRepository
import com.example.cotacaofacil.data.repository.partner.contract.PartnerRepository
import com.example.cotacaofacil.domain.Extensions.Companion.ifNotEmpty
import com.example.cotacaofacil.domain.exception.DefaultException
import com.example.cotacaofacil.domain.model.HistoryModel
import com.example.cotacaofacil.domain.model.PartnerModel
import com.example.cotacaofacil.domain.model.TypeHistory
import com.example.cotacaofacil.domain.usecase.partner.contract.AcceptRequestPartnerUseCase

class AcceptRequestPartnerUseCaseImpl(
    val repository: PartnerRepository,
    private val historyRepository: HistoryRepository,
    private val bodyCompanyRepository: BodyCompanyRepository
) : AcceptRequestPartnerUseCase {
    override suspend fun invoke(cnpj: String, partner: PartnerModel, currentDate: Long): Result<Boolean> {
        return bodyCompanyRepository.getBodyCompanyModel(cnpj)
            .map { bodyCompanyModel ->
                addHistoryModel(TypeHistory.NEW_PARTNER_ADD, currentDate, partner.nameFantasy.ifNotEmpty(), cnpj)
                addHistoryModel(TypeHistory.NEW_PARTNER_ADD, currentDate, bodyCompanyModel.fantasia.ifNotEmpty(), partner.cnpjCorporation)
                val result = repository.acceptPartner(cnpj, partner)
                result.getOrNull() ?: throw DefaultException()
            }
            .onFailure {
                Result.failure<Boolean>(DefaultException())
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