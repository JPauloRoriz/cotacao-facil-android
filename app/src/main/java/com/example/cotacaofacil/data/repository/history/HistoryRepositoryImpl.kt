package com.example.cotacaofacil.data.repository.history

import com.example.cotacaofacil.data.model.util.toHistoricModel
import com.example.cotacaofacil.data.repository.history.contract.HistoryRepository
import com.example.cotacaofacil.data.service.history.contract.HistoryService
import com.example.cotacaofacil.domain.mapper.toHistoricResponse
import com.example.cotacaofacil.domain.model.HistoryModel

class HistoryRepositoryImpl(
    private val service : HistoryService
) : HistoryRepository {
    override suspend fun getAllHistoryModelByCnpj(cnpj: String): Result<Any> {
        return service.getAllHistoryResponseByCnpj(cnpj).toHistoricModel()
    }

    override suspend fun addHistory(historicModel: HistoryModel, cnpj: String): Result<Any?>? {
        return service.addHistoryResponse(historicModel.toHistoricResponse(), cnpj)
    }

    override suspend fun deleteHistoric(historicModel: HistoryModel): Result<Any?>? {
        return service.deleteHistoric(historicModel.toHistoricResponse())
    }


}