package com.example.cotacaofacil.domain.usecase.product

import android.content.Context
import com.example.cotacaofacil.R
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
        typeMeasurements: String?,
        cnpjUser: String,
        quantity: String,
        context: Context,
        isConfirmationDataEmpty: Boolean,
        isFavorite: Boolean,
        currentDate : Long
    ): Result<Any?> {
        var typeMeasurementsNotNull = ""
        if(typeMeasurements.isNullOrEmpty()){
            typeMeasurementsNotNull = context.getString(R.string.kg)
        }
        return if (context.isNetworkConnected()) {
            if (name.isEmpty()) {
                Result.failure<Exception>(EmptyFildException())
            } else if (isConfirmationDataEmpty) {
                repository.saveProduct(name, description, brand, typeMeasurementsNotNull, cnpjUser, quantity, isFavorite, currentDate)
            } else {
                if (description.isEmpty() || brand.isEmpty()) {
                    Result.failure(SaveDataEmptyConfirmationException())
                } else {
                    repository.saveProduct(name, description, brand, typeMeasurementsNotNull, cnpjUser, quantity, isFavorite, currentDate)
                }
            }

        } else {
            Result.failure(ConnectException())
        }
    }
}