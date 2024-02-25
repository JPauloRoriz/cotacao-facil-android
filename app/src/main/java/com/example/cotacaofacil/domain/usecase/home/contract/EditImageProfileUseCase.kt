package com.example.cotacaofacil.domain.usecase.home.contract

import android.graphics.Bitmap

interface EditImageProfileUseCase {
    suspend fun invoke(cnpj : String, imageBitmap : Bitmap): Result<String>
}