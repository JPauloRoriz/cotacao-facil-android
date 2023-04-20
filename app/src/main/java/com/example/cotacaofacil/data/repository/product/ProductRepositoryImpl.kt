package com.example.cotacaofacil.data.repository.product

import android.content.Context
import com.example.cotacaofacil.data.model.ProductResponse
import com.example.cotacaofacil.data.model.SpinnerListHelper
import com.example.cotacaofacil.data.repository.product.contract.ProductRepository
import com.example.cotacaofacil.data.service.product.contract.ProductService
import com.example.cotacaofacil.domain.Extensions.Companion.toProductModel
import com.example.cotacaofacil.domain.model.ProductModel

class ProductRepositoryImpl(
    val context: Context,
    private val spinnerListHelper: SpinnerListHelper,
    private val productService: ProductService
) : ProductRepository {


    override fun getAllListSpinnerOption(): MutableList<String> {
        return spinnerListHelper.getListSpinnerOptionsMeasurements(context)
    }

    override suspend fun saveProduct(
        name: String,
        description: String,
        brand: String,
        typeMeasurements: String,
        cnpjUser: String,
        quantity: String
    ): Result<Any?> {
        return productService.saveProduct(
            ProductResponse(
                name = name,
                description = description,
                brand = brand,
                typeMeasurement = typeMeasurements,
                cnpjBuyer = cnpjUser,
                quantity = quantity
            )
        )
    }

    override suspend fun getAllProducts(cnpjUser: String): Result<MutableList<ProductModel>> {
        val result = productService.getAllProduct(cnpjUser)
        return result.map {
            it.toProductModel()
        }
    }

}