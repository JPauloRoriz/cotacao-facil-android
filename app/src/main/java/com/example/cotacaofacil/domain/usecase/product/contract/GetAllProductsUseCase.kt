package com.example.cotacaofacil.domain.usecase.product.contract

import com.example.cotacaofacil.domain.model.ProductModel

interface GetAllProductsUseCase {
    suspend fun invoke(cnpjUser : String) : Result<MutableList<ProductModel>>
}