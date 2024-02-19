package com.example.cotacaofacil.domain.usecase.price.contract

import com.example.cotacaofacil.domain.model.PriceModel

interface EditPriceUseCase {
    suspend fun invoke(priceModel : PriceModel) : Result<String>
}