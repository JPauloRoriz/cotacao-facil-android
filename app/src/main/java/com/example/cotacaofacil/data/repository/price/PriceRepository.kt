package com.example.cotacaofacil.data.repository.price

import com.example.cotacaofacil.domain.model.PriceModel

interface PriceRepository {
    suspend fun savePrice(priceModel: PriceModel) : Result<String>
    suspend fun getPricesByCnpj(cnpjUser: String): Result<MutableList<PriceModel>>
}