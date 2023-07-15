package com.example.cotacaofacil.domain.usecase.historic

import com.example.cotacaofacil.data.repository.history.contract.HistoryRepository
import com.example.cotacaofacil.domain.model.HistoryModel
import com.example.cotacaofacil.domain.model.TypeHistory
import com.example.cotacaofacil.domain.usecase.historic.contract.AddHistoricUseCase

class AddHistoricUseCaseImpl(
    private val repository: HistoryRepository
) : AddHistoricUseCase{
    override suspend fun addHistoricAddPrice(date: Long, cnpjUser: String, codePrice: String) {
        addHistoryModel(TypeHistory.CREATE_PRICE, date, "", cnpjUser)
    }

    private suspend fun addHistoryModel(typeHistory: TypeHistory, date: Long, codePrice: String, cnpj: String) {
        repository.addHistory(
            HistoryModel(
                typeHistory = typeHistory,
                date = date,
                nameAssistant = codePrice
            ), cnpj = cnpj
        )
    }
}