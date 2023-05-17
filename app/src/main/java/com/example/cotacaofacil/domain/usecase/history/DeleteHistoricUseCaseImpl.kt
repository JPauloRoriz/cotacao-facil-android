package com.example.cotacaofacil.domain.usecase.history

import com.example.cotacaofacil.data.repository.history.contract.HistoryRepository
import com.example.cotacaofacil.domain.model.HistoryModel
import com.example.cotacaofacil.domain.usecase.history.contract.DeleteHistoricUseCase

class DeleteHistoricUseCaseImpl(
    private val repository: HistoryRepository
) : DeleteHistoricUseCase {
    override suspend fun invoke(historicModel: HistoryModel): Result<Any?>? {
        return repository.deleteHistoric(historicModel)
    }
}