package com.example.cotacaofacil.data.repository.dete

import com.example.cotacaofacil.data.repository.dete.contract.DateCurrentRepository
import com.example.cotacaofacil.data.service.date.contract.DateCurrentService

class DateCurrentRepositoryImpl(
    private val dateCurrentService: DateCurrentService
) : DateCurrentRepository {
    override suspend fun invoke(): Result<Long> {
        return dateCurrentService.invoke()
    }
}