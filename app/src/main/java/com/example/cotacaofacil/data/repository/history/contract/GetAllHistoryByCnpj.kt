package com.example.cotacaofacil.data.repository.history.contract

import com.example.cotacaofacil.domain.model.HistoryModel
import com.example.cotacaofacil.domain.model.PartnerModel

interface HistoryRepository {
    suspend fun getAllHistoryModelByCnpj(cnpj : String) : Result<Any>
    suspend fun addHistory(historicModel: HistoryModel, cnpj: String): Result<Any?>?
    suspend fun deleteHistoric(historicModel: HistoryModel) : Result<Any?>?
}