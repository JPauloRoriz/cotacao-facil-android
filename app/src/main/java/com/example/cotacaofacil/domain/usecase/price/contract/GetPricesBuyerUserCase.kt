package com.example.cotacaofacil.domain.usecase.price.contract

import com.example.cotacaofacil.domain.model.PriceModel

interface GetPricesBuyerUserCase {
    suspend fun invoke(cnpjUser : String) : Result<MutableList<PriceModel>>
}