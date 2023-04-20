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
import com.example.cotacaofacil.domain.usecase.partner.contract.RejectRequestPartnerUseCase
import com.example.cotacaofacil.domain.usecase.partner.util.TypeDeletePartner
import java.util.*

class RejectRequestPartnerUseCaseImpl(
    private val partnerRepository: PartnerRepository,
    private val historyRepository: HistoryRepository,
    private val bodyCompanyRepository: BodyCompanyRepository
) : RejectRequestPartnerUseCase {
    override suspend fun invoke(cnpj: String, partnerModel: PartnerModel, context: Context, typeDelete: TypeDeletePartner): Result<Boolean> {
        return if (context.isNetworkConnected()) {
            bodyCompanyRepository.getBodyCompanyModel(cnpj).onSuccess { bodyCompanyModel ->
                val date = Date().time
                when(typeDelete){
                    TypeDeletePartner.DELETE_PARTNER -> {
                        addHistoryModel(TypeHistory.PARTNER_DELETED, date, partnerModel.nameFantasy.ifNotEmpty(), cnpj)
                    }
                    TypeDeletePartner.REJECT_PARTNER -> {
                        addHistoryModel(TypeHistory.REQUEST_PARTNER_REJECT, date, partnerModel.nameFantasy.ifNotEmpty(), cnpj)
                        addHistoryModel(TypeHistory.MY_REQUEST_PARTNER_REJECT, date, bodyCompanyModel.fantasia.ifNotEmpty(), partnerModel.cnpjCorporation)
                    }
                    TypeDeletePartner.CANCEL_REQUEST_PARTNER -> {
                        addHistoryModel(TypeHistory.REQUEST_PARTNER_CANCEL, date, partnerModel.nameFantasy.ifNotEmpty(), cnpj)
                    }
                }
            }
            partnerRepository.rejectPartner(cnpj, partnerModel)
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