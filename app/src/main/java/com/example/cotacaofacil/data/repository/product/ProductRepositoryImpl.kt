package com.example.cotacaofacil.data.repository.product

import android.content.Context
import com.example.cotacaofacil.data.helper.SpinnerListHelper
import com.example.cotacaofacil.data.model.ProductResponse
import com.example.cotacaofacil.data.repository.product.contract.ProductRepository
import com.example.cotacaofacil.data.service.Date.contract.DateCurrent
import com.example.cotacaofacil.data.service.product.contract.ProductService
import com.example.cotacaofacil.domain.Extensions.Companion.toProductModel
import com.example.cotacaofacil.domain.Extensions.Companion.toProductResponse
import com.example.cotacaofacil.domain.model.ProductModel

class ProductRepositoryImpl(
    val context: Context,
    private val spinnerListHelper: SpinnerListHelper,
    private val productService: ProductService,
    private val dateCurrent: DateCurrent

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
        quantity: String,
        isFavorite: Boolean
    ): Result<Any?> {
        return dateCurrent.invoke()
            .onSuccess {date ->
                productService.saveProduct(
                    ProductResponse(
                        name = name,
                        description = description,
                        brand = brand,
                        typeMeasurement = typeMeasurements,
                        cnpjBuyer = cnpjUser,
                        quantity = quantity,
                        date = date,
                        favorite = isFavorite
                    )
                )
            }.onFailure {
                Result.failure<java.lang.Exception>(it)
            }
    }

    override suspend fun getAllProducts(cnpjUser: String): Result<MutableList<ProductModel>> {
        val result = productService.getAllProduct(cnpjUser)
        return result.map {
            it.toProductModel()
        }
    }

    override suspend fun editProduct(product: ProductModel): Result<Unit> {
       return productService.editProduct(product.toProductResponse())
    }

    override suspend fun deleteProduct(productModel: ProductModel): Result<Unit>? {
        return productService.deleteProduct(productModel.toProductResponse())
    }

}