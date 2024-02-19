package com.example.cotacaofacil.domain.usecase.price

import com.example.cotacaofacil.data.repository.price.PriceRepository
import com.example.cotacaofacil.domain.exception.DefaultException
import com.example.cotacaofacil.domain.model.PriceModel
import com.example.cotacaofacil.domain.model.StatusPrice
import com.example.cotacaofacil.domain.usecase.price.contract.ValidationPricesProviderUseCase

class ValidationPricesProviderUseCaseImpl(
    private val priceRepository: PriceRepository
) : ValidationPricesProviderUseCase {
    override suspend fun invoke(prices: MutableList<PriceModel>, cnpjProvider: String, dateCurrent : Long): MutableList<PriceModel> {
        prices.forEach { priceModel ->
            if (priceModel.closeAutomatic && priceModel.status == StatusPrice.OPEN && (priceModel.dateFinishPrice ?: 0) < dateCurrent
            ) {
                priceModel.status = StatusPrice.FINISHED
                priceRepository.editPrice(priceModel)
            }
        }
        return prices.filter {
            it.status == StatusPrice.OPEN || validationPricesFinished(it, cnpjProvider)
        }.toMutableList()
    }

    private fun validationPricesFinished(price: PriceModel, cnpjProvider: String): Boolean {
        price.productsPrice.forEach {
            it.usersPrice.forEach {
                if(it.cnpjProvider == cnpjProvider) return true
            }
        }
        return false
    }
}