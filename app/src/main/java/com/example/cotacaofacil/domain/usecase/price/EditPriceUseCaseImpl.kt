package com.example.cotacaofacil.domain.usecase.price

import com.example.cotacaofacil.data.repository.price.PriceRepository
import com.example.cotacaofacil.domain.model.PriceModel
import com.example.cotacaofacil.domain.usecase.price.contract.EditPriceUseCase

class EditPriceUseCaseImpl(
    val repository : PriceRepository
) : EditPriceUseCase {
    override suspend fun invoke(priceModel : PriceModel) = repository.editPrice(priceModel)
}