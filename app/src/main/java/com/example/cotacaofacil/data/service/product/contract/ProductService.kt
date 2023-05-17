package com.example.cotacaofacil.data.service.product.contract

import com.example.cotacaofacil.data.model.ProductResponse

interface ProductService {
    suspend fun saveProduct(product : ProductResponse) : Result<Any?>
    suspend fun getAllProduct(cnpjUser : String) : Result<MutableList<ProductResponse>>
    suspend fun editProduct(productResponse: ProductResponse): Result<Unit>
    suspend fun deleteProduct(productResponse: ProductResponse): Result<Unit>?
}