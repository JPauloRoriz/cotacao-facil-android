package com.example.cotacaofacil.data.service.history.contract

import com.example.cotacaofacil.data.model.HistoricResponse
import com.example.cotacaofacil.domain.model.PartnerModel

interface HistoryService {
    suspend fun getAllHistoryResponseByCnpj(cnpj : String) : Result<MutableList<HistoricResponse>>
    suspend fun addHistoryResponse(toHistoricResponse: HistoricResponse, cnpj: String): Result<Any?>?
}