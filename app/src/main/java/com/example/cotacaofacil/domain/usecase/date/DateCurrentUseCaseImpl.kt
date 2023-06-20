package com.example.cotacaofacil.domain.usecase.date

import com.example.cotacaofacil.data.repository.dete.contract.DateCurrentRepository
import com.example.cotacaofacil.domain.usecase.date.contract.DateCurrentUseCase

class DateCurrentUseCaseImpl(
    private val repository : DateCurrentRepository
) : DateCurrentUseCase {
    override suspend fun invoke(): Result<Long> {
        return repository.invoke()
    }
}