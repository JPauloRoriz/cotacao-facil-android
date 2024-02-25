package com.example.cotacaofacil.domain.usecase.home

import com.example.cotacaofacil.data.repository.storage.contract.StorageRepository
import com.example.cotacaofacil.domain.usecase.home.contract.GetAllImageProfileUseCase

class GetAllImageProfileUseCaseImpl(private val repository: StorageRepository) : GetAllImageProfileUseCase {
    override suspend fun invoke(cnpjs: MutableList<String>): Result<MutableList<String>> {
        return repository.getAllImagesByCnpj(cnpjs)
    }
}