package com.example.cotacaofacil.domain.usecase.product

import com.example.cotacaofacil.data.repository.product.contract.ProductRepository
import com.example.cotacaofacil.domain.usecase.product.contract.GetAllListSpinnerOptionsUseCase

class GetAllListSpinnerOptionsUseCaseImpl(
   val repository: ProductRepository
) : GetAllListSpinnerOptionsUseCase {
    override fun invoke(): MutableList<String> {
       return repository.getAllListSpinnerOption()
    }
}