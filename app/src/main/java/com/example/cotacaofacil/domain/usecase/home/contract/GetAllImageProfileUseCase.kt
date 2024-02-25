package com.example.cotacaofacil.domain.usecase.home.contract

interface GetAllImageProfileUseCase {
    suspend fun invoke(cnpjs: MutableList<String>) : Result<MutableList<String>>
}