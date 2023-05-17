package com.example.cotacaofacil.domain.usecase.product.contract

import com.example.cotacaofacil.domain.model.ProductModel

interface EditProductUseCase {
    suspend fun invoke(productModel: ProductModel?) : Result<Unit>?
}