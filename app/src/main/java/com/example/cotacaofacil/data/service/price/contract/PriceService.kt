package com.example.cotacaofacil.data.service.price.contract

import com.example.cotacaofacil.data.model.PriceResponse

interface PriceService {
    suspend fun savePrice(priceResponse: PriceResponse) : Result<String>
    suspend fun editPrice(priceResponse: PriceResponse) : Result<String>
    suspend fun getPricesByCnpj(cnpjUser: String): Result<MutableList<PriceResponse>>
    suspend fun getPriceByCnpj(code: String, cnpjBuyer: String): Result<PriceResponse>
}