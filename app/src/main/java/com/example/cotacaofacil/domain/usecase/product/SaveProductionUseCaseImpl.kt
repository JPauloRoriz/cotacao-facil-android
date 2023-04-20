package com.example.cotacaofacil.domain.usecase.product

import android.content.Context
import com.example.cotacaofacil.data.repository.product.contract.ProductRepository
import com.example.cotacaofacil.domain.Extensions.Companion.isNetworkConnected
import com.example.cotacaofacil.domain.exception.EmptyFildException
import com.example.cotacaofacil.domain.exception.SaveDataEmptyConfirmationException
import com.example.cotacaofacil.domain.usecase.product.contract.SaveProductionUseCase
import java.net.ConnectException

class SaveProductionUseCaseImpl(
    val repository: ProductRepository
) : SaveProductionUseCase {
    override suspend fun invoke(
        name: String,
        description: String,
        brand: String,
        typeMeasurements: String,
        cnpjUser: String,
        quantity : String,
        context: Context,
        isConfirmationDataEmpty: Boolean
    ): Result<Any?> {
        return if (context.isNetworkConnected()) {
            if (name.isEmpty()) {
                Result.failure<Exception>(EmptyFildException())
            } else if (isConfirmationDataEmpty) {
                repository.saveProduct(name, description, brand, typeMeasurements, cnpjUser, quantity)
            } else {
                if (description.isEmpty() || brand.isEmpty()) {
                    Result.failure(SaveDataEmptyConfirmationException())
                } else {
                    repository.saveProduct(name, description, brand, typeMeasurements, cnpjUser, quantity)
                }
            }

        } else {
            Result.failure(ConnectException())
        }
    }
}