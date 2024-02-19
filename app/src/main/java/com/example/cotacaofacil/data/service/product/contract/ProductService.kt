package com.example.cotacaofacil.data.service.product.contract

import com.example.cotacaofacil.data.model.ProductResponse
import com.example.cotacaofacil.domain.model.ProductModel

interface ProductService {
    suspend fun saveProduct(product : ProductResponse) : Result<Any?>
    suspend fun getAllProduct(cnpjUser : String) : Result<MutableList<ProductResponse>>
    suspend fun editProduct(productResponse: ProductResponse): Result<Unit>
    suspend fun deleteProduct(productResponse: ProductResponse): Result<Unit>?
    suspend fun getProductsByCode(cnpjUser: String, codeProduct: String): Result<ProductResponse>
}