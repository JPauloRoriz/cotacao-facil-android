package com.example.cotacaofacil.domain.usecase.product

import com.example.cotacaofacil.data.repository.product.contract.ProductRepository
import com.example.cotacaofacil.domain.model.ProductModel
import com.example.cotacaofacil.domain.usecase.product.contract.GetAllByCnpjProductsUseCase

class GetAllByCnpjProductsUseCaseImpl(
    private val repository: ProductRepository
) : GetAllByCnpjProductsUseCase {
    override suspend fun invoke(cnpjUser : String): Result<MutableList<ProductModel>> {
        return repository.getAllProducts(cnpjUser)
    }
}