package com.example.cotacaofacil.data.service.product

import com.example.cotacaofacil.data.model.ProductResponse
import com.example.cotacaofacil.data.service.product.contract.ProductService
import com.example.cotacaofacil.domain.exception.DefaultException
import com.example.cotacaofacil.domain.exception.ListEmptyException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlin.random.Random

class ProductServiceImpl(
    private val firebaseFirestore: FirebaseFirestore
) : ProductService {

    override suspend fun saveProduct(product: ProductResponse): Result<Any?> {
        product.code = createProductCode(product.cnpjBuyer)
        val result = firebaseFirestore.collection(PRODUCT).document(product.cnpjBuyer)
            .collection(MY_PRODUCT).document(product.code)
            .set(product)
        result.await()
        return if (result.isSuccessful) {
            Result.success(null)
        } else {
            Result.failure(DefaultException())
        }
    }

    override suspend fun getAllProduct(cnpjUser: String): Result<MutableList<ProductResponse>> {
        val result = firebaseFirestore.collection(PRODUCT).document(cnpjUser)
            .collection(MY_PRODUCT).get().await()
       return try {
            if (result.documents.isEmpty())
                Result.failure(ListEmptyException())
            else
                Result.success(result.toObjects(ProductResponse::class.java))
        } catch (e: Exception) {
            Result.failure(DefaultException())
        }
    }

    private suspend fun createProductCode(cnpjBuyer: String): String {
        var productCode = Random.nextInt(100000, 999999)
        val productsRef = firebaseFirestore.collection(PRODUCT).document(cnpjBuyer).collection(MY_PRODUCT)
        var codeExists = true

        while (codeExists) {
            val query = productsRef.whereEqualTo("code", productCode)
            val result = query.get().await()

            if (result.isEmpty) {
                codeExists = false
            } else {
                productCode = Random.nextInt(100000, 999999)
            }
        }

        return productCode.toString()
    }

    companion object {
        private const val PRODUCT: String = "product"
        private const val MY_PRODUCT: String = "my_product"
    }
}