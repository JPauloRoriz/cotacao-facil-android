package com.example.cotacaofacil.domain.usecase.product

import com.example.cotacaofacil.data.repository.product.contract.ProductRepository
import com.example.cotacaofacil.domain.model.ProductModel
import com.example.cotacaofacil.domain.usecase.product.contract.GetAllProductsUseCase

class GetAllProductsUseCaseImpl(
    private val repository: ProductRepository
) : GetAllProductsUseCase {
    override suspend fun invoke(cnpjUser : String): Result<MutableList<ProductModel>> {
        return repository.getAllProducts(cnpjUser)
    }
}