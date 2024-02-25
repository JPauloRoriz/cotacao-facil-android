package com.example.cotacaofacil.domain.usecase.home.contract


interface GetImageProfileUseCase {
    suspend fun invoke(cnpj : String) : Result<String>
}