package com.example.cotacaofacil.domain.usecase.history.contract

import com.example.cotacaofacil.domain.model.HistoryModel

interface DeleteHistoricUseCase {
    suspend fun invoke(historicModel: HistoryModel) : Result<Any?>?
}