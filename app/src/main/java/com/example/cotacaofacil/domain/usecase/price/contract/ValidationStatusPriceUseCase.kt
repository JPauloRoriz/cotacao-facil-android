package com.example.cotacaofacil.domain.usecase.price.contract

import com.example.cotacaofacil.domain.model.PriceModel

interface ValidationStatusPriceUseCase {
    suspend fun invoke(prices : MutableList<PriceModel>) : Result<MutableList<PriceModel>>
}