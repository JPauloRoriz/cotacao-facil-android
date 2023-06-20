package com.example.cotacaofacil.data.repository.dete.contract

interface DateCurrentRepository {
    suspend fun invoke() : Result<Long>
}