package com.example.cotacaofacil.data.repository.product.contract

import com.example.cotacaofacil.data.model.ProductResponse
import com.example.cotacaofacil.domain.model.ProductModel

interface ProductRepository {
    fun getAllListSpinnerOption() : MutableList<String>
    suspend fun saveProduct(
        name: String,
        description: String,
        brand: String,
        typeMeasurements: String,
        cnpjUser: String,
        quantity: String,
        isFavorite: Boolean
    ): Result<Any?>
    suspend fun getAllProducts(cnpjUser : String) : Result<MutableList<ProductModel>>
    suspend fun editProduct(product: ProductModel): Result<Unit>
    suspend fun deleteProduct(productModel: ProductModel): Result<Unit>?
}