package com.example.cotacaofacil.domain.usecase.historic.contract

interface GetAllItemHistoricUseCase {
    suspend fun invoke(cnpj : String) : Result<Any>
}