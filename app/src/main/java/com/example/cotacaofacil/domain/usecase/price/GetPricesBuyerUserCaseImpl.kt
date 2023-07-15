package com.example.cotacaofacil.domain.usecase.price

import com.example.cotacaofacil.data.repository.price.PriceRepository
import com.example.cotacaofacil.domain.model.PriceModel
import com.example.cotacaofacil.domain.usecase.price.contract.GetPricesBuyerUserCase

class GetPricesBuyerUserCaseImpl(
    private val repository: PriceRepository
) : GetPricesBuyerUserCase {
    override suspend fun invoke(cnpjUser : String): Result<MutableList<PriceModel>> {
        return repository.getPricesByCnpj(cnpjUser)
    }
}