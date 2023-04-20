package com.example.cotacaofacil.domain.usecase.history

import com.example.cotacaofacil.data.repository.history.contract.HistoryRepository
import com.example.cotacaofacil.domain.model.HistoryModel
import com.example.cotacaofacil.domain.usecase.history.contract.GetAllItemHistoryUseCase

class GetAllItemHistoryUseCaseImpl(
    private val repository: HistoryRepository
) : GetAllItemHistoryUseCase {
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