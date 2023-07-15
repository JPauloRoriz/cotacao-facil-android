package com.example.cotacaofacil.domain.usecase.price.contract

import com.example.cotacaofacil.domain.model.PriceModel

interface CreatePriceUseCase {
    suspend fun invoke(priceModel: PriceModel) : Result<String>
}