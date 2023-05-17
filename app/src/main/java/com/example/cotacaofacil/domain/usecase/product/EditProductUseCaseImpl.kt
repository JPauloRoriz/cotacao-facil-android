package com.example.cotacaofacil.domain.usecase.product

import com.example.cotacaofacil.data.repository.product.contract.ProductRepository
import com.example.cotacaofacil.domain.model.ProductModel
import com.example.cotacaofacil.domain.usecase.product.contract.EditProductUseCase

class EditProductUseCaseImpl(private val repository: ProductRepository) : EditProductUseCase {
    override suspend fun invoke(productModel: ProductModel?): Result<Unit>? {
        return productModel?.let { repository.editProduct(it) }
    }

}