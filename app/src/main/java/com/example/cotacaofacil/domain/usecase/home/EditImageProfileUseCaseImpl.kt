package com.example.cotacaofacil.domain.usecase.home

import android.graphics.Bitmap
import com.example.cotacaofacil.data.repository.storage.contract.StorageRepository
import com.example.cotacaofacil.domain.usecase.home.contract.EditImageProfileUseCase

class EditImageProfileUseCaseImpl(
   private val repository: StorageRepository
) : EditImageProfileUseCase {
    override suspend fun invoke(cnpj : String, imageBitmap : Bitmap): Result<String> {
        return repository.editImageUser(cnpj = cnpj, imageBitmap = imageBitmap)
    }
}