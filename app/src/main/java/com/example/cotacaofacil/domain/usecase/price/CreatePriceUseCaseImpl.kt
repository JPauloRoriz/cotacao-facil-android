package com.example.cotacaofacil.domain.usecase.price

import com.example.cotacaofacil.data.repository.price.PriceRepository
import com.example.cotacaofacil.domain.model.PriceModel
import com.example.cotacaofacil.domain.usecase.price.contract.CreatePriceUseCase

class CreatePriceUseCaseImpl(
    private val repository : PriceRepository
) : CreatePriceUseCase {
    override suspend fun invoke(priceModel: PriceModel): Result<String> {
       return repository.savePrice(priceModel)
    }
}