package com.example.cotacaofacil.domain.usecase.product

import com.example.cotacaofacil.data.repository.product.contract.ProductRepository
import com.example.cotacaofacil.domain.model.ProductModel
import com.example.cotacaofacil.domain.usecase.product.contract.DeleteProductUseCase

class DeleteProductUseCaseImpl(private val repository: ProductRepository
) : DeleteProductUseCase {

    override suspend fun invoke(productModel: ProductModel): Result<Unit>? {
        return repository.deleteProduct(productModel)
    }
}