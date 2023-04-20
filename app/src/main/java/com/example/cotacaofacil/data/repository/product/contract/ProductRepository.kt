package com.example.cotacaofacil.data.repository.product.contract

import com.example.cotacaofacil.data.model.SpinnerListHelper
import com.example.cotacaofacil.domain.model.ProductModel

interface ProductRepository {
    fun getAllListSpinnerOption() : MutableList<String>
    suspend fun saveProduct(
        name: String,
        description: String,
        brand: String,
        typeMeasurements: String,
        cnpjUser: String,
        quantity: String
    ): Result<Any?>
    suspend fun getAllProducts(cnpjUser : String) : Result<MutableList<ProductModel>>
}