package com.example.cotacaofacil.domain.usecase.price.contract

import com.example.cotacaofacil.domain.model.PriceModel

interface ValidationPricesProviderUseCase {
    suspend fun invoke(prices : MutableList<PriceModel>, cnpjProvider : String, dateCurrent : Long): MutableList<PriceModel>
}