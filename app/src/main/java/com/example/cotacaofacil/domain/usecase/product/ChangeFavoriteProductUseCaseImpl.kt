package com.example.cotacaofacil.domain.usecase.product

import com.example.cotacaofacil.data.repository.product.contract.ProductRepository
import com.example.cotacaofacil.domain.model.ProductModel
import com.example.cotacaofacil.domain.usecase.product.contract.ChangeFavoriteProductUseCase

class ChangeFavoriteProductUseCaseImpl(
    private val productRepository: ProductRepository
) : ChangeFavoriteProductUseCase {
    override suspend fun invoke(product: ProductModel): Result<Unit> {
        product.isFavorite = !product.isFavorite
        return productRepository.editProduct(product)
    }
}