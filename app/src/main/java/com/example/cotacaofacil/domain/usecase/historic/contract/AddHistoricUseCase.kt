package com.example.cotacaofacil.domain.usecase.historic.contract

interface AddHistoricUseCase {
    suspend fun addHistoricAddPrice(date: Long, cnpjUser: String, codePrice: String)
}