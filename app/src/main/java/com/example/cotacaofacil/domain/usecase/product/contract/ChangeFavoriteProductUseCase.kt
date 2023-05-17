package com.example.cotacaofacil.domain.usecase.product.contract

import com.example.cotacaofacil.domain.model.ProductModel

interface ChangeFavoriteProductUseCase {
    suspend fun invoke(product : ProductModel) : Result<Unit>
}