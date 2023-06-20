package com.example.cotacaofacil.domain.usecase.product.contract

import android.content.Context

interface SaveProductionUseCase {
    suspend fun invoke(
        name: String,
        description: String,
        brand: String,
        typeMeasurements: String,
        cnpjUser: String,
        quantity: String,
        context: Context,
        isConfirmationDataEmpty: Boolean,
        isFavorite: Boolean,
        currentDate : Long
    ): Result<Any?>
}