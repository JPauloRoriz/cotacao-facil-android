package com.example.cotacaofacil.domain.usecase.date.contract

interface DateCurrentUseCase {
    suspend fun invoke() : Result<Long>
}