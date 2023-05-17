package com.example.cotacaofacil.data.service.history.contract

import com.example.cotacaofacil.data.model.HistoricResponse

interface HistoryService {
    suspend fun getAllHistoryResponseByCnpj(cnpj : String) : Result<MutableList<HistoricResponse>>
    suspend fun addHistoryResponse(toHistoricResponse: HistoricResponse, cnpj: String): Result<Any?>?
   suspend fun deleteHistoric(historicResponse: HistoricResponse): Result<Any?>?
}