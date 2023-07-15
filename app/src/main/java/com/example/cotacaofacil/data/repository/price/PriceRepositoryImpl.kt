package com.example.cotacaofacil.data.repository.price

import com.example.cotacaofacil.data.service.price.contract.PriceService
import com.example.cotacaofacil.domain.model.PriceModel

class PriceRepositoryImpl(
    private val priceService : PriceService
) : PriceRepository {
    override suspend fun savePrice(priceModel: PriceModel): Result<String> {
        return priceService.savePrice(priceModel)
    }

    override suspend fun getPricesByCnpj(cnpjUser: String): Result<MutableList<PriceModel>> {
       return priceService.getPricesByCnpj(cnpjUser)
    }
}