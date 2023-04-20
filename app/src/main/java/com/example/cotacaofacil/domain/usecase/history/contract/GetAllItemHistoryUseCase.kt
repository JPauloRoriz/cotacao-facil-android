package com.example.cotacaofacil.domain.usecase.history.contract

import com.example.cotacaofacil.domain.model.HistoryModel

interface GetAllItemHistoryUseCase {
    suspend fun invoke(cnpj : String) : Result<Any>
}