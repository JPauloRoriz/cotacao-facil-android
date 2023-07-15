package com.example.cotacaofacil.domain.usecase.historic

import com.example.cotacaofacil.data.repository.history.contract.HistoryRepository
import com.example.cotacaofacil.domain.model.HistoryModel
import com.example.cotacaofacil.domain.usecase.historic.contract.GetAllItemHistoricUseCase

class GetAllItemHistoricUseCaseImpl(
    private val repository: HistoryRepository
) : GetAllItemHistoricUseCase {
    override suspend fun invoke(cnpj: String): Result<Any> {
     return  repository.getAllHistoryModelByCnpj(cnpj).onSuccess {
             val historyModelList = mutableListOf<HistoryModel>()
           historyModelList.addAll(it as MutableList<HistoryModel>)
             Result.success(historyModelList)
         }.onFailure {
             Result.failure<Exception>(it)
         }
    }
}