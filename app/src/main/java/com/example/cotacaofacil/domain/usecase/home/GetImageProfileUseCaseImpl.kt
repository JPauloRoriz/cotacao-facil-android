package com.example.cotacaofacil.domain.usecase.home

import com.example.cotacaofacil.data.repository.storage.contract.StorageRepository
import com.example.cotacaofacil.domain.usecase.home.contract.GetImageProfileUseCase

class GetImageProfileUseCaseImpl(private val repository: StorageRepository) : GetImageProfileUseCase {
    override suspend fun invoke(cnpj: String): Result<String> {
        return repository.getImageProfile(cnpj = cnpj)
    }
}