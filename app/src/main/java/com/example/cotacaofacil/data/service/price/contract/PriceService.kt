package com.example.cotacaofacil.data.service.price.contract

import com.example.cotacaofacil.domain.model.PriceModel

interface PriceService {
    suspend fun savePrice(priceModel: PriceModel) : Result<String>
    suspend fun getPricesByCnpj(cnpjUser: String): Result<MutableList<PriceModel>>
}