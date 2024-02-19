package com.example.cotacaofacil.domain.usecase.price

import com.example.cotacaofacil.data.repository.price.PriceRepository
import com.example.cotacaofacil.domain.model.PriceEditModel
import com.example.cotacaofacil.domain.usecase.price.contract.SetPricePartnerUseCase

class SetPricePartnerUseCaseUseCaseImpl(
    val repository : PriceRepository
) : SetPricePartnerUseCase {
    override suspend fun invoke(cnpjPartner: String, productsEditPrice: PriceEditModel): Result<Any>  {
        return repository.setPricesPartner(cnpjPartner, productsEditPrice)
    }
}